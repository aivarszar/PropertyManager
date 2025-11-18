<?php

namespace PropertyManager;

class Auth
{
    private $db;

    public function __construct()
    {
        $this->db = Database::getInstance();

        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }
    }

    public function register($email, $password, $name, $phone, $role = 'tenant')
    {
        // Validate email
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            throw new \Exception('Invalid email address');
        }

        // Check if email already exists
        $existing = $this->db->fetch(
            "SELECT id FROM users WHERE email = ?",
            [$email]
        );

        if ($existing) {
            throw new \Exception('Email already registered');
        }

        // Hash password
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);

        // Insert user
        $this->db->query(
            "INSERT INTO users (email, password, name, phone, role, is_active, email_verified)
             VALUES (?, ?, ?, ?, ?, 1, 1)",
            [$email, $hashedPassword, $name, $phone, $role]
        );

        return $this->db->lastInsertId();
    }

    public function login($email, $password)
    {
        $user = $this->db->fetch(
            "SELECT * FROM users WHERE email = ? AND is_active = 1",
            [$email]
        );

        if (!$user) {
            throw new \Exception('Invalid credentials');
        }

        if (!password_verify($password, $user['password'])) {
            throw new \Exception('Invalid credentials');
        }

        // Set session
        $_SESSION['user_id'] = $user['id'];
        $_SESSION['user_email'] = $user['email'];
        $_SESSION['user_name'] = $user['name'];
        $_SESSION['user_role'] = $user['role'];
        $_SESSION['logged_in'] = true;

        // Create session record
        $sessionId = bin2hex(random_bytes(32));
        $this->db->query(
            "INSERT INTO sessions (id, user_id, ip_address, user_agent, expires_at)
             VALUES (?, ?, ?, ?, DATE_ADD(NOW(), INTERVAL 24 HOUR))",
            [
                $sessionId,
                $user['id'],
                $_SERVER['REMOTE_ADDR'] ?? null,
                $_SERVER['HTTP_USER_AGENT'] ?? null
            ]
        );

        return $user;
    }

    public function logout()
    {
        if (isset($_SESSION['user_id'])) {
            // Clean up old sessions
            $this->db->query(
                "DELETE FROM sessions WHERE user_id = ? OR expires_at < NOW()",
                [$_SESSION['user_id']]
            );
        }

        session_destroy();
        $_SESSION = [];
    }

    public function isLoggedIn()
    {
        return isset($_SESSION['logged_in']) && $_SESSION['logged_in'] === true;
    }

    public function requireLogin()
    {
        if (!$this->isLoggedIn()) {
            header('Location: /login.php');
            exit;
        }
    }

    public function requireRole($role)
    {
        $this->requireLogin();

        if ($_SESSION['user_role'] !== $role) {
            header('Location: /dashboard.php');
            exit;
        }
    }

    public function getCurrentUser()
    {
        if (!$this->isLoggedIn()) {
            return null;
        }

        return $this->db->fetch(
            "SELECT id, email, name, phone, role FROM users WHERE id = ?",
            [$_SESSION['user_id']]
        );
    }

    public function getUserId()
    {
        return $_SESSION['user_id'] ?? null;
    }

    public function getUserRole()
    {
        return $_SESSION['user_role'] ?? null;
    }
}
