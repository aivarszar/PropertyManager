<?php
/**
 * Property Manager - Configuration File Example
 *
 * Copy this file to config.php and update with your settings
 */

return [
    // Database Configuration
    'database' => [
        'host' => 'localhost',
        'port' => '3306',
        'database' => 'property_manager',
        'username' => 'root',
        'password' => '',
        'charset' => 'utf8mb4',
    ],

    // Application Settings
    'app' => [
        'name' => 'Property Manager',
        'url' => 'http://localhost',
        'timezone' => 'Europe/Riga',
        'locale' => 'lv_LV',
        'debug' => true,
    ],

    // Security
    'security' => [
        'session_lifetime' => 86400, // 24 hours in seconds
        'password_min_length' => 8,
        'max_login_attempts' => 5,
        'lockout_duration' => 900, // 15 minutes in seconds
    ],

    // Email Configuration
    'email' => [
        'from_address' => 'noreply@propertymanager.local',
        'from_name' => 'Property Manager',
        // SMTP Settings (optional)
        'smtp' => [
            'enabled' => false,
            'host' => 'smtp.example.com',
            'port' => 587,
            'username' => '',
            'password' => '',
            'encryption' => 'tls', // tls or ssl
        ],
    ],

    // File Upload
    'upload' => [
        'max_size' => 5242880, // 5MB in bytes
        'allowed_types' => ['jpg', 'jpeg', 'png', 'pdf'],
        'path' => __DIR__ . '/../public/uploads/',
    ],

    // Paths
    'paths' => [
        'root' => dirname(__DIR__),
        'public' => dirname(__DIR__) . '/public',
        'logs' => dirname(__DIR__) . '/logs',
        'uploads' => dirname(__DIR__) . '/public/uploads',
    ],
];
