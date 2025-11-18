<!DOCTYPE html>
<html lang="lv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Property Manager - Instalācija</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .header h1 { margin-bottom: 10px; }
        .content { padding: 40px; }
        .step { margin-bottom: 30px; }
        .step h2 {
            color: #667eea;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f0f0f0;
        }
        .form-group { margin-bottom: 20px; }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #333;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            width: 100%;
            transition: transform 0.2s;
        }
        .btn:hover { transform: translateY(-2px); }
        .btn:active { transform: translateY(0); }
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .alert-success {
            background: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }
        .alert-error {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
        .alert-warning {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            color: #856404;
        }
        .alert-info {
            background: #d1ecf1;
            border: 1px solid #bee5eb;
            color: #0c5460;
        }
        .requirements {
            list-style: none;
            padding: 0;
        }
        .requirements li {
            padding: 10px;
            margin: 5px 0;
            background: #f8f9fa;
            border-radius: 5px;
            display: flex;
            align-items: center;
        }
        .requirements li::before {
            content: '✓';
            color: #28a745;
            font-weight: bold;
            margin-right: 10px;
            font-size: 18px;
        }
        .requirements li.error::before {
            content: '✗';
            color: #dc3545;
        }
        .progress {
            background: #f0f0f0;
            border-radius: 10px;
            height: 30px;
            margin: 20px 0;
            overflow: hidden;
        }
        .progress-bar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
            transition: width 0.3s;
        }
        .small-text { font-size: 12px; color: #666; margin-top: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏠 Property Manager</h1>
            <p>Instalācijas vednis</p>
        </div>
        <div class="content">
            <?php
            session_start();
            error_reporting(E_ALL);
            ini_set('display_errors', 1);

            $step = isset($_GET['step']) ? (int)$_GET['step'] : 1;
            $errors = [];
            $success = [];

            // Check if already installed
            if (file_exists(__DIR__ . '/config/config.php') && $step === 1) {
                echo '<div class="alert alert-warning">';
                echo '<strong>⚠️ Brīdinājums:</strong> Aplikācija jau ir instalēta!<br>';
                echo 'Konfigurācijas fails jau pastāv. Ja vēlaties reinstalēt, lūdzu dzēsiet <code>config/config.php</code> failu.';
                echo '</div>';
                echo '<a href="public/index.php" class="btn">Doties uz aplikāciju →</a>';
                exit;
            }

            // Step 1: Requirements Check
            if ($step === 1) {
                ?>
                <div class="step">
                    <h2>1. Sistēmas Prasību Pārbaude</h2>
                    <ul class="requirements">
                        <?php
                        $requirements = [
                            'PHP Version >= 7.4' => version_compare(PHP_VERSION, '7.4.0', '>='),
                            'PDO Extension' => extension_loaded('pdo'),
                            'PDO MySQL Extension' => extension_loaded('pdo_mysql'),
                            'MBString Extension' => extension_loaded('mbstring'),
                            'GD Extension (attēliem)' => extension_loaded('gd'),
                            'Session Support' => function_exists('session_start'),
                            'config/ direktorija rakstāma' => is_writable(__DIR__ . '/config'),
                            'public/uploads/ direktorija rakstāma' => is_writable(__DIR__ . '/public/uploads'),
                            'logs/ direktorija rakstāma' => is_writable(__DIR__ . '/logs'),
                        ];

                        $all_ok = true;
                        foreach ($requirements as $req => $status) {
                            $class = $status ? '' : ' class="error"';
                            $all_ok = $all_ok && $status;
                            echo "<li{$class}>{$req}</li>";
                        }
                        ?>
                    </ul>

                    <?php if ($all_ok): ?>
                        <div class="alert alert-success">
                            ✅ Visas prasības ir izpildītas! Varat turpināt instalāciju.
                        </div>
                        <a href="?step=2" class="btn">Turpināt uz Datubāzes Konfigurāciju →</a>
                    <?php else: ?>
                        <div class="alert alert-error">
                            ❌ Lūdzu atrisiniet norādītās problēmas pirms turpināt.
                        </div>
                    <?php endif; ?>
                </div>
                <?php
            }

            // Step 2: Database Configuration
            elseif ($step === 2) {
                if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['test_connection'])) {
                    $host = $_POST['db_host'] ?? '';
                    $port = $_POST['db_port'] ?? '3306';
                    $database = $_POST['db_name'] ?? '';
                    $username = $_POST['db_user'] ?? '';
                    $password = $_POST['db_pass'] ?? '';

                    try {
                        $dsn = "mysql:host={$host};port={$port};charset=utf8mb4";
                        $pdo = new PDO($dsn, $username, $password);
                        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                        // Try to create database if it doesn't exist
                        $pdo->exec("CREATE DATABASE IF NOT EXISTS `{$database}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                        $pdo->exec("USE `{$database}`");

                        $_SESSION['db_config'] = [
                            'host' => $host,
                            'port' => $port,
                            'database' => $database,
                            'username' => $username,
                            'password' => $password,
                        ];

                        echo '<div class="alert alert-success">✅ Datubāzes savienojums veiksmīgs!</div>';
                        echo '<a href="?step=3" class="btn">Turpināt uz Datubāzes Izveidošanu →</a>';
                    } catch (PDOException $e) {
                        echo '<div class="alert alert-error">❌ Datubāzes savienojums neizdevās: ' . htmlspecialchars($e->getMessage()) . '</div>';
                    }
                }
                ?>
                <div class="step">
                    <h2>2. Datubāzes Konfigurācija</h2>
                    <form method="POST">
                        <div class="form-group">
                            <label>Datubāzes Serveris (Host)</label>
                            <input type="text" name="db_host" value="localhost" required>
                            <div class="small-text">Parasti: localhost</div>
                        </div>
                        <div class="form-group">
                            <label>Ports</label>
                            <input type="text" name="db_port" value="3306" required>
                        </div>
                        <div class="form-group">
                            <label>Datubāzes Nosaukums</label>
                            <input type="text" name="db_name" value="property_manager" required>
                            <div class="small-text">Datubāze tiks izveidota automātiski, ja nepastāv</div>
                        </div>
                        <div class="form-group">
                            <label>Lietotājvārds</label>
                            <input type="text" name="db_user" value="root" required>
                        </div>
                        <div class="form-group">
                            <label>Parole</label>
                            <input type="password" name="db_pass">
                        </div>
                        <button type="submit" name="test_connection" class="btn">Pārbaudīt Savienojumu</button>
                    </form>
                </div>
                <?php
            }

            // Step 3: Create Database Tables
            elseif ($step === 3) {
                if (!isset($_SESSION['db_config'])) {
                    header('Location: ?step=2');
                    exit;
                }

                $config = $_SESSION['db_config'];

                try {
                    $dsn = "mysql:host={$config['host']};port={$config['port']};dbname={$config['database']};charset=utf8mb4";
                    $pdo = new PDO($dsn, $config['username'], $config['password']);
                    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                    // Read and execute schema.sql
                    $schema = file_get_contents(__DIR__ . '/database/schema.sql');
                    $pdo->exec($schema);

                    echo '<div class="alert alert-success">✅ Datubāzes tabulas izveidotas veiksmīgi!</div>';
                    echo '<a href="?step=4" class="btn">Turpināt uz Administratora Izveidošanu →</a>';
                } catch (PDOException $e) {
                    echo '<div class="alert alert-error">❌ Kļūda izveidojot tabulas: ' . htmlspecialchars($e->getMessage()) . '</div>';
                    echo '<a href="?step=2" class="btn">Atpakaļ uz Datubāzes Konfigurāciju</a>';
                }
            }

            // Step 4: Create Admin User
            elseif ($step === 4) {
                if (!isset($_SESSION['db_config'])) {
                    header('Location: ?step=2');
                    exit;
                }

                if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['create_admin'])) {
                    $name = trim($_POST['admin_name'] ?? '');
                    $email = trim($_POST['admin_email'] ?? '');
                    $phone = trim($_POST['admin_phone'] ?? '');
                    $password = $_POST['admin_password'] ?? '';
                    $password_confirm = $_POST['admin_password_confirm'] ?? '';

                    if (empty($name) || empty($email) || empty($password)) {
                        $errors[] = 'Visi lauki ir obligāti!';
                    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                        $errors[] = 'Nederīga e-pasta adrese!';
                    } elseif ($password !== $password_confirm) {
                        $errors[] = 'Paroles nesakrīt!';
                    } elseif (strlen($password) < 8) {
                        $errors[] = 'Parolei jābūt vismaz 8 simbolus garai!';
                    } else {
                        try {
                            $config = $_SESSION['db_config'];
                            $dsn = "mysql:host={$config['host']};port={$config['port']};dbname={$config['database']};charset=utf8mb4";
                            $pdo = new PDO($dsn, $config['username'], $config['password']);
                            $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                            $hashed_password = password_hash($password, PASSWORD_DEFAULT);

                            $stmt = $pdo->prepare("
                                INSERT INTO users (email, password, name, phone, role, is_active, email_verified)
                                VALUES (?, ?, ?, ?, 'owner', 1, 1)
                            ");
                            $stmt->execute([$email, $hashed_password, $name, $phone]);

                            $_SESSION['admin_created'] = true;
                            header('Location: ?step=5');
                            exit;
                        } catch (PDOException $e) {
                            $errors[] = 'Kļūda izveidojot lietotāju: ' . $e->getMessage();
                        }
                    }
                }
                ?>
                <div class="step">
                    <h2>4. Izveidot Administratora Kontu</h2>

                    <?php if (!empty($errors)): ?>
                        <div class="alert alert-error">
                            <?php foreach ($errors as $error): ?>
                                ❌ <?= htmlspecialchars($error) ?><br>
                            <?php endforeach; ?>
                        </div>
                    <?php endif; ?>

                    <form method="POST">
                        <div class="form-group">
                            <label>Vārds, Uzvārds *</label>
                            <input type="text" name="admin_name" required value="<?= htmlspecialchars($_POST['admin_name'] ?? '') ?>">
                        </div>
                        <div class="form-group">
                            <label>E-pasts *</label>
                            <input type="email" name="admin_email" required value="<?= htmlspecialchars($_POST['admin_email'] ?? '') ?>">
                        </div>
                        <div class="form-group">
                            <label>Telefons</label>
                            <input type="text" name="admin_phone" value="<?= htmlspecialchars($_POST['admin_phone'] ?? '') ?>">
                        </div>
                        <div class="form-group">
                            <label>Parole *</label>
                            <input type="password" name="admin_password" required>
                            <div class="small-text">Vismaz 8 simboli</div>
                        </div>
                        <div class="form-group">
                            <label>Apstipriniet Paroli *</label>
                            <input type="password" name="admin_password_confirm" required>
                        </div>
                        <button type="submit" name="create_admin" class="btn">Izveidot Administratoru</button>
                    </form>
                </div>
                <?php
            }

            // Step 5: Create Config File
            elseif ($step === 5) {
                if (!isset($_SESSION['db_config']) || !isset($_SESSION['admin_created'])) {
                    header('Location: ?step=2');
                    exit;
                }

                $config = $_SESSION['db_config'];

                // Check if config.php already exists
                $config_path = __DIR__ . '/config/config.php';
                if (file_exists($config_path)) {
                    echo '<div class="alert alert-warning">';
                    echo '⚠️ Konfigurācijas fails jau pastāv un NETIKA pārrakstīts.<br>';
                    echo 'Ja vēlaties izveidot jaunu konfigurāciju, lūdzu dzēsiet esošo <code>config/config.php</code> failu.';
                    echo '</div>';
                } else {
                    // Create config.php from template
                    $config_template = file_get_contents(__DIR__ . '/config/config.example.php');

                    // Replace placeholders
                    $config_content = str_replace(
                        [
                            "'host' => 'localhost',",
                            "'port' => '3306',",
                            "'database' => 'property_manager',",
                            "'username' => 'root',",
                            "'password' => '',",
                            "'debug' => true,",
                        ],
                        [
                            "'host' => '" . addslashes($config['host']) . "',",
                            "'port' => '" . addslashes($config['port']) . "',",
                            "'database' => '" . addslashes($config['database']) . "',",
                            "'username' => '" . addslashes($config['username']) . "',",
                            "'password' => '" . addslashes($config['password']) . "',",
                            "'debug' => false,",
                        ],
                        $config_template
                    );

                    if (file_put_contents($config_path, $config_content)) {
                        echo '<div class="alert alert-success">✅ Konfigurācijas fails izveidots veiksmīgi!</div>';
                    } else {
                        echo '<div class="alert alert-error">❌ Neizdevās izveidot konfigurācijas failu. Lūdzu pārbaudiet write permissions.</div>';
                    }
                }

                // Create .htaccess if doesn't exist
                $htaccess_path = __DIR__ . '/.htaccess';
                if (!file_exists($htaccess_path)) {
                    $htaccess_content = "# Deny access to sensitive files\n";
                    $htaccess_content .= "<FilesMatch \"\\.(sql|md|json|lock|log)$\">\n";
                    $htaccess_content .= "    Require all denied\n";
                    $htaccess_content .= "</FilesMatch>\n\n";
                    $htaccess_content .= "# Deny access to directories\n";
                    $htaccess_content .= "Options -Indexes\n";

                    file_put_contents($htaccess_path, $htaccess_content);
                }

                // Clear session
                session_destroy();

                ?>
                <div class="step">
                    <h2>🎉 Instalācija Pabeigta!</h2>

                    <div class="alert alert-success">
                        <strong>Apsveicam!</strong> Property Manager ir veiksmīgi instalēts.<br><br>
                        <strong>Svarīgi drošības pasākumi:</strong><br>
                        1. Dzēsiet vai pārvietojiet <code>install.php</code> failu<br>
                        2. Pārbaudiet, ka <code>config/config.php</code> nav publiski pieejams<br>
                        3. Iestatiet production režīmu: <code>'debug' => false</code> konfigurācijā
                    </div>

                    <div class="alert alert-info">
                        <strong>Nākamie soļi:</strong><br>
                        1. <a href="public/index.php">Doties uz aplikāciju</a><br>
                        2. Pieslēgties ar izveidotajiem administratora datiem<br>
                        3. Sākt pievienot īpašumus un īrniekus!
                    </div>

                    <a href="public/index.php" class="btn">Doties uz Property Manager →</a>
                </div>

                <div class="step">
                    <h2>📚 Noderīga Informācija</h2>
                    <p><strong>Projekta struktūra:</strong></p>
                    <ul>
                        <li><code>public/</code> - Publiski pieejamie faili (index.php, assets)</li>
                        <li><code>src/</code> - PHP aplikācijas kods</li>
                        <li><code>config/</code> - Konfigurācijas faili</li>
                        <li><code>database/</code> - SQL shēmas</li>
                        <li><code>logs/</code> - Aplikācijas logi</li>
                        <li><code>public/uploads/</code> - Augšupielādētie faili</li>
                    </ul>
                </div>
                <?php
            }
            ?>

            <div class="progress">
                <div class="progress-bar" style="width: <?= ($step / 5) * 100 ?>%">
                    Solis <?= $step ?> no 5
                </div>
            </div>
        </div>
    </div>
</body>
</html>
