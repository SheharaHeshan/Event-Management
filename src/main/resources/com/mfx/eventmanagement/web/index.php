<?php
header('Content-Type: text/html; charset=utf-8');
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Database configuration
$host = '68.183.226.234';
$username = 'shehara';
$password = '123';
$database = 'main_event';

// Connect to database
try {
    $pdo = new PDO("mysql:host=$host;dbname=$database;charset=utf8mb4", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Database connection failed: " . $e->getMessage());
}

// Get event ID from URL parameter
$event_id = isset($_GET['event_id']) ? (int)$_GET['event_id'] : 0;

if ($event_id <= 0) {
    die("Invalid event ID");
}

// Handle form submission
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    handleRegistration($pdo);
    exit;
}

// Get event details
try {
    $stmt = $pdo->prepare("SELECT * FROM events WHERE event_id = :event_id");
    $stmt->bindParam(':event_id', $event_id, PDO::PARAM_INT);
    $stmt->execute();
    $event = $stmt->fetch(PDO::FETCH_ASSOC);
    
    if (!$event) {
        die("Event not found");
    }
} catch (PDOException $e) {
    die("Error fetching event: " . $e->getMessage());
}

// Determine template based on attendance type
$template_file = getTemplateFile($event['attendance_type']);

if (!file_exists($template_file)) {
    die("Template file not found: " . $template_file);
}

// Load and process template
$template_content = file_get_contents($template_file);
$processed_content = processTemplate($template_content, $event);

echo $processed_content;

/**
 * Determine which template file to use based on attendance type
 */
function getTemplateFile($attendance_type) {
    $template_dir = __DIR__ . '/templates/';
    $templates = [
        'Open' => $template_dir . 'openevent.html',
        'VIP' => $template_dir . 'vipevent.html',
        'Invite-only' => $template_dir . 'inviteevent.html',
        'Ticketed' => $template_dir . 'ticketevent.html'
    ];
    
    return isset($templates[$attendance_type]) ? $templates[$attendance_type] : $template_dir . 'openevent.html';
}

/**
 * Process template by replacing placeholders with actual data
 */
function processTemplate($template, $event) {
    // Format dates and times
    $start_date = date('M j, Y', strtotime($event['start_date']));
    $end_date = date('M j, Y', strtotime($event['end_date']));
    $start_time = date('g:i A', strtotime($event['start_time']));
    $end_time = date('g:i A', strtotime($event['end_time']));
    
    // Determine icon based on event type
    $event_type_icon = $event['event_type'] === 'Online' ? 'laptop' : 'map-marker-alt';
    
    // Create replacements array
    $replacements = [
        '{{EVENT_ID}}' => $event['event_id'],
        '{{EVENT_NAME}}' => htmlspecialchars($event['event_name'], ENT_QUOTES, 'UTF-8'),
        '{{EVENT_DESCRIPTION}}' => htmlspecialchars($event['event_description'], ENT_QUOTES, 'UTF-8'),
        '{{START_DATE}}' => $start_date,
        '{{END_DATE}}' => $end_date,
        '{{START_TIME}}' => $start_time,
        '{{END_TIME}}' => $end_time,
        '{{EVENT_TYPE}}' => htmlspecialchars($event['event_type'], ENT_QUOTES, 'UTF-8'),
        '{{EVENT_TYPE_ICON}}' => $event_type_icon,
        '{{ATTENDANCE_TYPE}}' => htmlspecialchars($event['attendance_type'], ENT_QUOTES, 'UTF-8')
    ];
    
    // Replace all placeholders
    foreach ($replacements as $placeholder => $value) {
        $template = str_replace($placeholder, $value, $template);
    }
    
    return $template;
}

/**
 * Handle registration form submission
 */
function handleRegistration($pdo) {
    header('Content-Type: application/json');
    
    try {
        // Validate required fields
        $event_id = isset($_POST['event_id']) ? (int)$_POST['event_id'] : 0;
        $fullname = isset($_POST['fullname']) ? trim($_POST['fullname']) : '';
        
        if ($event_id <= 0) {
            echo json_encode(['success' => false, 'message' => 'Invalid event ID']);
            return;
        }
        
        if (empty($fullname)) {
            echo json_encode(['success' => false, 'message' => 'Full name is required']);
            return;
        }
        
        // Verify event exists and is open
        $stmt = $pdo->prepare("SELECT * FROM events WHERE event_id = :event_id");
        $stmt->bindParam(':event_id', $event_id, PDO::PARAM_INT);
        $stmt->execute();
        $event = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$event) {
            echo json_encode(['success' => false, 'message' => 'Event not found']);
            return;
        }
        
        // Check if user already registered (by email OR phone number)
        $email = !empty($_POST['email']) ? trim($_POST['email']) : null;
        $phonenumber = !empty($_POST['phonenumber']) ? trim($_POST['phonenumber']) : null;
        
        // Check for duplicate email
        if (!empty($email)) {
            $stmt = $pdo->prepare("SELECT COUNT(*) FROM attendance WHERE event_id = :event_id AND email = :email");
            $stmt->bindParam(':event_id', $event_id, PDO::PARAM_INT);
            $stmt->bindParam(':email', $email);
            $stmt->execute();
            $email_count = $stmt->fetchColumn();
            
            if ($email_count > 0) {
                echo json_encode(['success' => false, 'message' => 'This email is already registered for this event']);
                return;
            }
        }
        
        // Check for duplicate phone number
        if (!empty($phonenumber)) {
            $stmt = $pdo->prepare("SELECT COUNT(*) FROM attendance WHERE event_id = :event_id AND phonenumber = :phonenumber");
            $stmt->bindParam(':event_id', $event_id, PDO::PARAM_INT);
            $stmt->bindParam(':phonenumber', $phonenumber);
            $stmt->execute();
            $phone_count = $stmt->fetchColumn();
            
            if ($phone_count > 0) {
                echo json_encode(['success' => false, 'message' => 'This phone number is already registered for this event']);
                return;
            }
        }
        
        // Validate email format if provided
        if (!empty($email) && !validateEmail($email)) {
            echo json_encode(['success' => false, 'message' => 'Please enter a valid email address']);
            return;
        }
        
        // Validate phone number format if provided
        if (!empty($phonenumber) && !validatePhone($phonenumber)) {
            echo json_encode(['success' => false, 'message' => 'Please enter a valid phone number (10-15 digits)']);
            return;
        }
        
        // Validate age if provided
        $age = !empty($_POST['age']) ? (int)$_POST['age'] : null;
        if (!empty($_POST['age']) && ($age < 1 || $age > 120)) {
            echo json_encode(['success' => false, 'message' => 'Please enter a valid age between 1 and 120']);
            return;
        }
        
        // Handle profile picture upload
        $profile_picture_path = null;
        if (isset($_FILES['profile_picture']) && $_FILES['profile_picture']['error'] === UPLOAD_ERR_OK) {
            $profile_picture_path = handleFileUpload($_FILES['profile_picture'], $event_id);
            if ($profile_picture_path === false) {
                echo json_encode(['success' => false, 'message' => 'Error uploading profile picture']);
                return;
            }
        }
        
        // Prepare data for insertion
        $gender = !empty($_POST['gender']) ? trim($_POST['gender']) : null;
        $about = !empty($_POST['about']) ? trim($_POST['about']) : null;
        $address = !empty($_POST['address']) ? trim($_POST['address']) : null;
        
        // Insert attendance record
        $stmt = $pdo->prepare("
            INSERT INTO attendance (
                event_id, fullname, age, gender, about, address, email, phonenumber, profile_picture_path
            ) VALUES (
                :event_id, :fullname, :age, :gender, :about, :address, :email, :phonenumber, :profile_picture_path
            )
        ");
        
        $stmt->bindParam(':event_id', $event_id, PDO::PARAM_INT);
        $stmt->bindParam(':fullname', $fullname, PDO::PARAM_STR);
        $stmt->bindParam(':age', $age, PDO::PARAM_INT);
        $stmt->bindParam(':gender', $gender, PDO::PARAM_STR);
        $stmt->bindParam(':about', $about, PDO::PARAM_STR);
        $stmt->bindParam(':address', $address, PDO::PARAM_STR);
        $stmt->bindParam(':email', $email, PDO::PARAM_STR);
        $stmt->bindParam(':phonenumber', $phonenumber, PDO::PARAM_STR);
        $stmt->bindParam(':profile_picture_path', $profile_picture_path, PDO::PARAM_STR);
        
        $stmt->execute();
        
        // Get the inserted attendance ID
        $attendance_id = $pdo->lastInsertId();
        
        echo json_encode([
            'success' => true, 
            'message' => 'Registration successful!',
            'attendance_id' => $attendance_id
        ]);
        
    } catch (PDOException $e) {
        error_log("Database error: " . $e->getMessage());
        echo json_encode(['success' => false, 'message' => 'Database error occurred. Please try again.']);
    } catch (Exception $e) {
        error_log("General error: " . $e->getMessage());
        echo json_encode(['success' => false, 'message' => 'An error occurred. Please try again.']);
    }
}

/**
 * Sanitize input data
 */
function sanitizeInput($data) {
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}

/**
 * Validate email format
 */
function validateEmail($email) {
    return filter_var($email, FILTER_VALIDATE_EMAIL) !== false;
}

/**
 * Validate phone number (basic validation)
 */
function validatePhone($phone) {
    // Remove all non-digit characters
    $phone = preg_replace('/[^0-9]/', '', $phone);
    // Check if it's between 10-15 digits
    return strlen($phone) >= 10 && strlen($phone) <= 15;
}

/**
 * Handle file upload for profile pictures
 */
function handleFileUpload($file, $event_id) {
    try {
        // Create uploads directory if it doesn't exist
        $upload_dir = __DIR__ . '/uploads/profile_pictures/';
        if (!is_dir($upload_dir)) {
            if (!mkdir($upload_dir, 0755, true)) {
                throw new Exception("Could not create upload directory");
            }
        }
        
        // Validate file type
        $allowed_types = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
        if (!in_array($file['type'], $allowed_types)) {
            throw new Exception("Invalid file type. Only JPG, PNG, and GIF are allowed.");
        }
        
        // Validate file size (5MB max)
        $max_size = 5 * 1024 * 1024; // 5MB
        if ($file['size'] > $max_size) {
            throw new Exception("File too large. Maximum size is 5MB.");
        }
        
        // Generate unique filename
        $file_extension = pathinfo($file['name'], PATHINFO_EXTENSION);
        $filename = 'profile_' . $event_id . '_' . uniqid() . '.' . $file_extension;
        $filepath = $upload_dir . $filename;
        
        // Move uploaded file
        if (!move_uploaded_file($file['tmp_name'], $filepath)) {
            throw new Exception("Failed to move uploaded file");
        }
        
        // Return relative path for database storage
        return 'uploads/profile_pictures/' . $filename;
        
    } catch (Exception $e) {
        error_log("File upload error: " . $e->getMessage());
        return false;
    }
}

/**
 * Get file MIME type
 */
function getMimeType($filepath) {
    $finfo = finfo_open(FILEINFO_MIME_TYPE);
    $mime = finfo_file($finfo, $filepath);
    finfo_close($finfo);
    return $mime;
}
?>