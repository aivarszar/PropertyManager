<!DOCTYPE html>
<html lang="lv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Property Manager - Atjaunināšana</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 900px;
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
        .version-box {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin: 20px 0;
            border-left: 4px solid #667eea;
        }
        .version-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e0e0e0;
        }
        .version-item:last-child { border-bottom: none; }
        .version-label { font-weight: 600; color: #666; }
        .version-value { color: #333; font-family: monospace; }
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            margin: 5px;
            transition: transform 0.2s;
            display: inline-block;
            text-decoration: none;
        }
        .btn:hover { transform: translateY(-2px); }
        .btn-danger {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
        }
        .btn-secondary {
            background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
        }
        .changes-list {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            max-height: 400px;
            overflow-y: auto;
            margin: 20px 0;
        }
        .commit-item {
            padding: 10px;
            margin: 5px 0;
            background: white;
            border-left: 3px solid #667eea;
            border-radius: 3px;
        }
        .commit-hash {
            font-family: monospace;
            color: #667eea;
            font-size: 12px;
        }
        .commit-message {
            margin-top: 5px;
            color: #333;
        }
        .commit-author {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
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
        .checkbox-confirm {
            margin: 20px 0;
            padding: 15px;
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 5px;
        }
        .checkbox-confirm label {
            display: flex;
            align-items: center;
            cursor: pointer;
        }
        .checkbox-confirm input[type="checkbox"] {
            margin-right: 10px;
            width: 20px;
            height: 20px;
            cursor: pointer;
        }
        .backup-info {
            background: #e7f3ff;
            padding: 15px;
            border-radius: 5px;
            margin: 15px 0;
            border-left: 4px solid #0066cc;
        }
        pre {
            background: #2d2d2d;
            color: #f8f8f2;
            padding: 15px;
            border-radius: 5px;
            overflow-x: auto;
            margin: 15px 0;
        }
        .file-list {
            list-style: none;
            padding: 0;
        }
        .file-list li {
            padding: 8px;
            margin: 3px 0;
            background: #f8f9fa;
            border-radius: 3px;
            font-family: monospace;
            font-size: 14px;
        }
        .file-list li.added { border-left: 3px solid #28a745; }
        .file-list li.modified { border-left: 3px solid #ffc107; }
        .file-list li.deleted { border-left: 3px solid #dc3545; }
        .step { margin-bottom: 30px; }
        .step h2 {
            color: #667eea;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f0f0f0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🔄 Property Manager</h1>
            <p>Atjaunināšanas Vednis</p>
        </div>
        <div class="content">
            <?php
            error_reporting(E_ALL);
            ini_set('display_errors', 1);

            $step = isset($_GET['step']) ? (int)$_GET['step'] : 1;
            $errors = [];

            // Helper functions
            function execCommand($command) {
                exec($command . ' 2>&1', $output, $returnCode);
                return [
                    'output' => implode("\n", $output),
                    'code' => $returnCode,
                    'success' => $returnCode === 0
                ];
            }

            function isGitRepository() {
                $result = execCommand('git rev-parse --git-dir');
                return $result['success'];
            }

            function getCurrentBranch() {
                $result = execCommand('git branch --show-current');
                return $result['success'] ? trim($result['output']) : 'unknown';
            }

            function getCurrentCommit() {
                $result = execCommand('git rev-parse HEAD');
                return $result['success'] ? trim($result['output']) : 'unknown';
            }

            function getRemoteCommit($branch) {
                execCommand("git fetch origin {$branch}");
                $result = execCommand("git rev-parse origin/{$branch}");
                return $result['success'] ? trim($result['output']) : 'unknown';
            }

            function getCommitsBehind($branch) {
                $result = execCommand("git rev-list HEAD..origin/{$branch} --oneline");
                return $result['success'] && !empty($result['output']) ?
                    explode("\n", trim($result['output'])) : [];
            }

            function getChangedFiles() {
                $result = execCommand('git diff --name-status HEAD origin/' . getCurrentBranch());
                $files = [];
                if ($result['success'] && !empty($result['output'])) {
                    foreach (explode("\n", trim($result['output'])) as $line) {
                        if (empty($line)) continue;
                        $parts = preg_split('/\s+/', $line, 2);
                        if (count($parts) === 2) {
                            $status = $parts[0];
                            $file = $parts[1];
                            $type = 'modified';
                            if ($status === 'A') $type = 'added';
                            elseif ($status === 'D') $type = 'deleted';
                            elseif ($status === 'M') $type = 'modified';
                            $files[] = ['status' => $status, 'file' => $file, 'type' => $type];
                        }
                    }
                }
                return $files;
            }

            function hasUncommittedChanges() {
                $result = execCommand('git status --porcelain');
                return !empty(trim($result['output']));
            }

            function createBackup() {
                $backupDir = __DIR__ . '/backups';
                if (!is_dir($backupDir)) {
                    mkdir($backupDir, 0755, true);
                }

                $timestamp = date('Y-m-d_H-i-s');
                $backupPath = "{$backupDir}/backup_{$timestamp}";

                // Create backup using rsync or cp
                $excludes = '--exclude=backups --exclude=.git --exclude=logs --exclude=public/uploads';
                $result = execCommand("rsync -av {$excludes} " . __DIR__ . "/ {$backupPath}/");

                if (!$result['success']) {
                    // Fallback to cp if rsync not available
                    $result = execCommand("cp -r " . __DIR__ . " {$backupPath}");
                }

                return $result['success'] ? $backupPath : false;
            }

            function performUpdate($branch) {
                // Stash any uncommitted changes
                execCommand('git stash');

                // Pull latest changes
                $result = execCommand("git pull origin {$branch}");

                // Apply stash if needed
                execCommand('git stash pop');

                return $result;
            }

            // Check if not installed
            if (!file_exists(__DIR__ . '/config/config.php')) {
                echo '<div class="alert alert-error">';
                echo '❌ <strong>Aplikācija nav instalēta!</strong><br>';
                echo 'Lūdzu vispirms palaidiet <a href="install.php">install.php</a>';
                echo '</div>';
                exit;
            }

            // Step 1: Check Git Status
            if ($step === 1) {
                ?>
                <div class="step">
                    <h2>1. Sistēmas Pārbaude</h2>

                    <?php
                    // Check if Git is available
                    $gitCheck = execCommand('git --version');
                    if (!$gitCheck['success']) {
                        echo '<div class="alert alert-error">';
                        echo '❌ Git nav pieejams! Instalējiet Git lai izmantotu automātisko atjaunināšanu.';
                        echo '</div>';
                        exit;
                    }

                    // Check if this is a Git repository
                    if (!isGitRepository()) {
                        echo '<div class="alert alert-error">';
                        echo '❌ Šis nav Git repository! Automātiskā atjaunināšana nav iespējama.<br>';
                        echo 'Lūdzu lejupielādējiet jaunāko versiju manuāli.';
                        echo '</div>';
                        exit;
                    }

                    $currentBranch = getCurrentBranch();
                    $currentCommit = getCurrentCommit();
                    $remoteCommit = getRemoteCommit($currentBranch);
                    $commitsBehind = getCommitsBehind($currentBranch);
                    $changedFiles = getChangedFiles();
                    $hasUncommitted = hasUncommittedChanges();

                    echo '<div class="alert alert-success">';
                    echo '✅ Git ir pieejams: ' . htmlspecialchars($gitCheck['output']);
                    echo '</div>';

                    if ($hasUncommitted) {
                        echo '<div class="alert alert-warning">';
                        echo '⚠️ <strong>Brīdinājums:</strong> Ir necommit\'otas izmaiņas!<br>';
                        echo 'Tās tiks automātiski saglabātas (stash) pirms atjaunināšanas.';
                        echo '</div>';
                    }
                    ?>

                    <div class="version-box">
                        <div class="version-item">
                            <span class="version-label">Pašreizējais Branch:</span>
                            <span class="version-value"><?= htmlspecialchars($currentBranch) ?></span>
                        </div>
                        <div class="version-item">
                            <span class="version-label">Pašreizējais Commit:</span>
                            <span class="version-value"><?= htmlspecialchars(substr($currentCommit, 0, 8)) ?></span>
                        </div>
                        <div class="version-item">
                            <span class="version-label">Jaunākais Remote Commit:</span>
                            <span class="version-value"><?= htmlspecialchars(substr($remoteCommit, 0, 8)) ?></span>
                        </div>
                        <div class="version-item">
                            <span class="version-label">Commits Aiz:</span>
                            <span class="version-value"><?= count($commitsBehind) ?> commit(s)</span>
                        </div>
                    </div>

                    <?php
                    if ($currentCommit === $remoteCommit && count($commitsBehind) === 0) {
                        echo '<div class="alert alert-success">';
                        echo '✅ <strong>Jūsu instalācija ir jau uz jaunākās versijas!</strong><br>';
                        echo 'Nav nepieciešama atjaunināšana.';
                        echo '</div>';
                        echo '<a href="public/index.php" class="btn">Doties uz aplikāciju →</a>';
                    } else {
                        echo '<div class="alert alert-info">';
                        echo '📦 <strong>Pieejama jauna versija!</strong><br>';
                        echo 'Atrasti ' . count($commitsBehind) . ' jauni commit(i).';
                        echo '</div>';
                        echo '<a href="?step=2" class="btn">Turpināt uz Izmaiņu Pārskatu →</a>';
                    }
                    ?>
                </div>
                <?php
            }

            // Step 2: Show Changes
            elseif ($step === 2) {
                $currentBranch = getCurrentBranch();
                $commitsBehind = getCommitsBehind($currentBranch);
                $changedFiles = getChangedFiles();
                ?>
                <div class="step">
                    <h2>2. Izmaiņu Pārskats</h2>

                    <div class="alert alert-info">
                        Tiks veiktas šādas izmaiņas:
                    </div>

                    <h3>Jaunie Commit(i):</h3>
                    <div class="changes-list">
                        <?php
                        if (empty($commitsBehind)) {
                            echo '<p>Nav jaunu commitu.</p>';
                        } else {
                            foreach ($commitsBehind as $commit) {
                                echo '<div class="commit-item">';
                                echo '<div class="commit-hash">' . htmlspecialchars(substr($commit, 0, 8)) . '</div>';
                                echo '<div class="commit-message">' . htmlspecialchars(substr($commit, 9)) . '</div>';
                                echo '</div>';
                            }
                        }
                        ?>
                    </div>

                    <h3>Izmainītie Faili:</h3>
                    <ul class="file-list">
                        <?php
                        if (empty($changedFiles)) {
                            echo '<li>Nav izmainītu failu.</li>';
                        } else {
                            foreach ($changedFiles as $file) {
                                $icon = $file['status'] === 'A' ? '➕' : ($file['status'] === 'D' ? '➖' : '📝');
                                echo '<li class="' . $file['type'] . '">';
                                echo $icon . ' ' . htmlspecialchars($file['file']);
                                echo '</li>';
                            }
                        }
                        ?>
                    </ul>

                    <a href="?step=3" class="btn">Turpināt uz Backup →</a>
                    <a href="?step=1" class="btn btn-secondary">← Atpakaļ</a>
                </div>
                <?php
            }

            // Step 3: Backup
            elseif ($step === 3) {
                ?>
                <div class="step">
                    <h2>3. Rezerves Kopijas Izveidošana</h2>

                    <div class="backup-info">
                        <strong>ℹ️ Svarīgi!</strong><br>
                        Pirms atjaunināšanas tiks izveidota pilna rezerves kopija.<br>
                        Tā tiks saglabāta <code>backups/</code> direktorijā.
                    </div>

                    <?php
                    if (isset($_POST['create_backup'])) {
                        echo '<div class="alert alert-info">📦 Veido rezerves kopiju...</div>';

                        $backupPath = createBackup();

                        if ($backupPath) {
                            echo '<div class="alert alert-success">';
                            echo '✅ Rezerves kopija izveidota veiksmīgi!<br>';
                            echo '<strong>Atrašanās vieta:</strong> <code>' . htmlspecialchars($backupPath) . '</code>';
                            echo '</div>';

                            // Store backup path in session for rollback
                            session_start();
                            $_SESSION['backup_path'] = $backupPath;

                            echo '<a href="?step=4" class="btn">Turpināt uz Apstiprinājumu →</a>';
                        } else {
                            echo '<div class="alert alert-error">';
                            echo '❌ Neizdevās izveidot rezerves kopiju!<br>';
                            echo 'Lūdzu pārbaudiet write permissions uz backups/ direktoriju.';
                            echo '</div>';
                            echo '<a href="?step=3" class="btn btn-secondary">Mēģināt vēlreiz</a>';
                        }
                    } else {
                        ?>
                        <form method="POST">
                            <button type="submit" name="create_backup" class="btn">Izveidot Rezerves Kopiju</button>
                        </form>
                        <a href="?step=2" class="btn btn-secondary">← Atpakaļ</a>
                        <?php
                    }
                    ?>
                </div>
                <?php
            }

            // Step 4: Confirm Update
            elseif ($step === 4) {
                $currentBranch = getCurrentBranch();
                ?>
                <div class="step">
                    <h2>4. Apstiprināt Atjaunināšanu</h2>

                    <div class="alert alert-warning">
                        <strong>⚠️ UZMANĪBU!</strong><br>
                        Jūs gatavojaties atjaunināt aplikāciju uz jaunāko versiju no Git branch.<br>
                        <strong>Visi faili tiks pārrakstīti ar jaunāko versiju!</strong>
                    </div>

                    <?php
                    session_start();
                    if (isset($_SESSION['backup_path'])) {
                        echo '<div class="backup-info">';
                        echo '✅ Rezerves kopija izveidota: <code>' . htmlspecialchars(basename($_SESSION['backup_path'])) . '</code>';
                        echo '</div>';
                    }
                    ?>

                    <div class="version-box">
                        <div class="version-item">
                            <span class="version-label">Branch:</span>
                            <span class="version-value"><?= htmlspecialchars($currentBranch) ?></span>
                        </div>
                        <div class="version-item">
                            <span class="version-label">Darbība:</span>
                            <span class="version-value">git pull origin <?= htmlspecialchars($currentBranch) ?></span>
                        </div>
                    </div>

                    <form method="POST" action="?step=5" id="confirmForm">
                        <div class="checkbox-confirm">
                            <label>
                                <input type="checkbox" name="confirm_update" id="confirmCheckbox" required>
                                Es saprotu, ka šī darbība pārrakstīs esošos failus ar jaunāko versiju no Git repository.
                                Rezerves kopija ir izveidota un es varu atgriezt izmaiņas ja nepieciešams.
                            </label>
                        </div>

                        <div class="checkbox-confirm" style="background: #f8d7da; border-color: #f5c6cb;">
                            <label>
                                <input type="checkbox" name="confirm_risk" id="confirmRisk" required>
                                Es apzinos riskus un vēlos turpināt ar atjaunināšanu.
                            </label>
                        </div>

                        <button type="submit" class="btn btn-danger" id="updateBtn" disabled>
                            🔄 Veikt Atjaunināšanu
                        </button>
                        <a href="?step=3" class="btn btn-secondary">← Atpakaļ</a>
                        <a href="public/index.php" class="btn btn-secondary">Atcelt</a>
                    </form>

                    <script>
                        const confirmCheckbox = document.getElementById('confirmCheckbox');
                        const confirmRisk = document.getElementById('confirmRisk');
                        const updateBtn = document.getElementById('updateBtn');

                        function checkConfirm() {
                            updateBtn.disabled = !(confirmCheckbox.checked && confirmRisk.checked);
                        }

                        confirmCheckbox.addEventListener('change', checkConfirm);
                        confirmRisk.addEventListener('change', checkConfirm);
                    </script>
                </div>
                <?php
            }

            // Step 5: Perform Update
            elseif ($step === 5) {
                if (!isset($_POST['confirm_update']) || !isset($_POST['confirm_risk'])) {
                    header('Location: ?step=4');
                    exit;
                }

                $currentBranch = getCurrentBranch();
                ?>
                <div class="step">
                    <h2>5. Veic Atjaunināšanu</h2>

                    <div class="alert alert-info">
                        🔄 Atjaunināšana notiek... Lūdzu uzgaidiet.
                    </div>

                    <?php
                    flush();
                    ob_flush();

                    // Perform update
                    $result = performUpdate($currentBranch);

                    if ($result['success']) {
                        $newCommit = getCurrentCommit();

                        echo '<div class="alert alert-success">';
                        echo '✅ <strong>Atjaunināšana pabeigta veiksmīgi!</strong><br><br>';
                        echo '<strong>Git pull output:</strong>';
                        echo '<pre>' . htmlspecialchars($result['output']) . '</pre>';
                        echo '<strong>Jaunais commit:</strong> <code>' . htmlspecialchars(substr($newCommit, 0, 8)) . '</code>';
                        echo '</div>';

                        // Check if database migrations needed
                        $migrationFile = __DIR__ . '/database/migrations.sql';
                        if (file_exists($migrationFile)) {
                            echo '<div class="alert alert-warning">';
                            echo '⚠️ Atrasts datubāzes migrācijas fails!<br>';
                            echo 'Iespējams nepieciešams atjaunināt datubāzi.<br>';
                            echo 'Lūdzu pārbaudiet <code>database/migrations.sql</code>';
                            echo '</div>';
                        }

                        // Clear any caches if they exist
                        if (is_dir(__DIR__ . '/cache')) {
                            array_map('unlink', glob(__DIR__ . '/cache/*'));
                            echo '<div class="alert alert-info">🧹 Cache notīrīts.</div>';
                        }

                        session_start();
                        if (isset($_SESSION['backup_path'])) {
                            echo '<div class="backup-info">';
                            echo '<strong>💾 Rezerves kopija saglabāta:</strong><br>';
                            echo '<code>' . htmlspecialchars($_SESSION['backup_path']) . '</code><br>';
                            echo '<small>Ja rodas problēmas, varat manuāli atjaunot no šīs kopijas.</small>';
                            echo '</div>';
                        }

                        echo '<div class="alert alert-success">';
                        echo '<strong>🎉 Viss gatavs!</strong><br>';
                        echo 'Property Manager ir veiksmīgi atjaunināts uz jaunāko versiju.';
                        echo '</div>';

                        echo '<a href="public/index.php" class="btn">Doties uz Aplikāciju →</a>';

                    } else {
                        echo '<div class="alert alert-error">';
                        echo '❌ <strong>Atjaunināšana neizdevās!</strong><br><br>';
                        echo '<strong>Kļūdas ziņojums:</strong>';
                        echo '<pre>' . htmlspecialchars($result['output']) . '</pre>';
                        echo '</div>';

                        session_start();
                        if (isset($_SESSION['backup_path'])) {
                            echo '<div class="alert alert-warning">';
                            echo '<strong>🔄 Atjaunošana no rezerves kopijas</strong><br>';
                            echo 'Jūsu iepriekšējā versija ir saglabāta:<br>';
                            echo '<code>' . htmlspecialchars($_SESSION['backup_path']) . '</code><br>';
                            echo 'Varat manuāli nokopēt failus atpakaļ ja nepieciešams.';
                            echo '</div>';
                        }

                        echo '<a href="?step=1" class="btn">Atgriezties uz Sākumu</a>';
                    }
                    ?>
                </div>
                <?php
            }
            ?>

            <div class="progress">
                <div class="progress-bar" style="width: <?= ($step / 5) * 100 ?>%">
                    Solis <?= $step ?> no 5
                </div>
            </div>

            <?php if ($step < 5): ?>
            <div style="margin-top: 30px; padding-top: 20px; border-top: 2px solid #f0f0f0;">
                <h3>ℹ️ Svarīga Informācija</h3>
                <ul style="line-height: 1.8;">
                    <li>Atjaunināšana izmantos <code>git pull</code> komandu</li>
                    <li>Tiks izveidota pilna rezerves kopija pirms update</li>
                    <li>Jūsu <code>config/config.php</code> un uploads/ netiks pārrakstīti</li>
                    <li>Necommit'otas izmaiņas tiks saglabātas (git stash)</li>
                    <li>Procesa laikā NESLĒDZIET šo logu!</li>
                </ul>
            </div>
            <?php endif; ?>
        </div>
    </div>
</body>
</html>
