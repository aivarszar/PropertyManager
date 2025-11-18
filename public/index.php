<?php
require_once __DIR__ . '/../src/Database.php';
require_once __DIR__ . '/../src/Auth.php';

use PropertyManager\Auth;
use PropertyManager\Database;

$auth = new Auth();

// Redirect to appropriate dashboard if logged in
if ($auth->isLoggedIn()) {
    header('Location: /dashboard.php');
    exit;
}
?>
<!DOCTYPE html>
<html lang="lv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Property Manager - Sākums</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="hero">
        <div class="container">
            <h1>🏠 Property Manager</h1>
            <p class="subtitle">Īpašumu un īrnieku pārvaldības sistēma</p>

            <div class="role-cards">
                <div class="card">
                    <div class="card-icon">🏢</div>
                    <h2>Īpašnieks</h2>
                    <p>Pārvaldiet īpašumus, īrniekus un maksājumus</p>
                    <a href="/register.php?role=owner" class="btn btn-primary">Reģistrēties</a>
                </div>

                <div class="card">
                    <div class="card-icon">🏠</div>
                    <h2>Īrnieks</h2>
                    <p>Skatiet maksājumus un ziņojiet par problēmām</p>
                    <a href="/register.php?role=tenant" class="btn btn-primary">Reģistrēties ar kodu</a>
                </div>
            </div>

            <div class="login-link">
                Jau ir konts? <a href="/login.php">Pieslēgties</a>
            </div>
        </div>
    </div>

    <div class="features">
        <div class="container">
            <h2>Galvenās Funkcijas</h2>
            <div class="feature-grid">
                <div class="feature">
                    <h3>💰 Maksājumu Uzskaite</h3>
                    <p>Sekojiet maksājumiem un ģenerējiet pārskatus</p>
                </div>
                <div class="feature">
                    <h3>👥 Īrnieku Pārvaldība</h3>
                    <p>Uzaiciniet īrniekus ar unikāliem kodiem</p>
                </div>
                <div class="feature">
                    <h3>🔧 Problēmu Ziņošana</h3>
                    <p>Ziņojiet par bojājumiem ar attēliem</p>
                </div>
                <div class="feature">
                    <h3>📊 Pārskati</h3>
                    <p>Ģenerējiet mēneša un gada pārskatus</p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
