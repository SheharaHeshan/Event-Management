-- phpMyAdmin SQL Dump -- version 5.2.1 -- https://www.phpmyadmin.net/
-- Host: localhost
-- Generation Time: Sep 28, 2025 at 09:03 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT /;
/!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS /;
/!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION /;
/!40101 SET NAMES utf8mb4 */;

-- -- Database: main_event
-- -- Table structure for table users -- (Added to support the registration and profile setup flow)
CREATE TABLE users (
user_id int(11) NOT NULL,
first_name varchar(100) NOT NULL,
last_name varchar(100) NOT NULL,
email varchar(255) NOT NULL,
password_hash varchar(255) NOT NULL,
age int(3) DEFAULT NULL,
gender varchar(50) DEFAULT NULL,
phone_number varchar(50) DEFAULT NULL,
address text DEFAULT NULL,
profile_picture_path varchar(255) DEFAULT NULL,
profile_complete tinyint(1) NOT NULL DEFAULT 0,
created_at timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- -- Indexes for table users
ALTER TABLE users
ADD PRIMARY KEY (user_id),
ADD UNIQUE KEY email (email);

-- -- Table structure for table attendance
CREATE TABLE attendance (
attendance_id int(11) NOT NULL,
event_id int(11) NOT NULL,
fullname varchar(255) NOT NULL,
age int(3) DEFAULT NULL,
gender varchar(50) DEFAULT NULL,
about text DEFAULT NULL,
address varchar(255) DEFAULT NULL,
email varchar(255) DEFAULT NULL,
phonenumber varchar(50) DEFAULT NULL,
log_timestamp timestamp NOT NULL DEFAULT current_timestamp(),
profile_picture_path varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- -- Dumping data for table attendance
INSERT INTO attendance (attendance_id, event_id, fullname, age, gender, about, address, email, phonenumber, log_timestamp, profile_picture_path) VALUES
(1, 1, 'John Doe', 30, 'Male', 'Software Developer', '123 Main St', 'john@example.com', '555-1234', '2025-09-27 15:58:34', NULL);

-- -- Table structure for table events
CREATE TABLE events (
event_id int(11) NOT NULL,
event_title varchar(255) NOT NULL,
event_description text DEFAULT NULL,
start_date date NOT NULL,
end_date date NOT NULL,
start_time time NOT NULL,
end_time time NOT NULL,
event_type varchar(50) NOT NULL,
access_type varchar(50) NOT NULL,
created_at timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- -- Dumping data for table events
INSERT INTO events (event_id, event_title, event_description, start_date, end_date, start_time, end_time, event_type, access_type, created_at) VALUES
(1, 'Workshop on Web Dev', 'A detailed workshop on modern web development frameworks.', '2025-10-15', '2025-10-16', '09:00:00', '17:00:00', 'Online', 'Open', '2025-09-27 15:58:34'),
(2, 'Annual Tech Conference', 'The biggest tech conference of the year.', '2025-11-05', '2025-11-07', '08:30:00', '18:00:00', 'Physical', 'Ticketed', '2025-09-27 15:59:34'),
(3, 'Test Event', 'Test event description', '2025-09-02', '2025-09-04', '10:17:07', '12:17:11', 'Online', 'Open', '2025-09-27 17:47:19'),
(5, 'jkjkjkjkjjjj', 'kjkjkjkj', '2025-09-10', '2025-09-18', '11:18:02', '17:18:08', 'Physical', 'Ticketed', '2025-09-27 17:48:17'),
(6, 'yyyyyyyyyyyyyyyyyy', 'yyyyyyyyyyyyyyyy', '2025-09-02', '2025-09-10', '10:38:32', '15:38:36', 'Physical', 'Invite-only', '2025-09-27 18:08:50'),
(7, 'Inoka', 'test inoka', '2025-09-01', '2025-09-10', '04:35:25', '18:35:29', 'Physical', 'Open', '2025-09-27 21:06:04');

-- -- Indexes for dumped tables
-- -- Indexes for table attendance
ALTER TABLE attendance
ADD PRIMARY KEY (attendance_id),
ADD KEY event_id (event_id);

-- -- Indexes for table events
ALTER TABLE events
ADD PRIMARY KEY (event_id);

-- -- AUTO_INCREMENT for dumped tables
-- -- AUTO_INCREMENT for table users
ALTER TABLE users
MODIFY user_id int(11) NOT NULL AUTO_INCREMENT;

-- -- AUTO_INCREMENT for table attendance
ALTER TABLE attendance
MODIFY attendance_id int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

-- -- AUTO_INCREMENT for table events
ALTER TABLE events
MODIFY event_id int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

-- -- Constraints for dumped tables
-- -- Constraints for table attendance
ALTER TABLE attendance
ADD CONSTRAINT attendance_ibfk_1 FOREIGN KEY (event_id) REFERENCES events (event_id);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT /;
/!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS /;
/!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;