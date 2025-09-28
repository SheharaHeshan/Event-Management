-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 28, 2025 at 03:45 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `main_event`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `attendance_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `fullname` varchar(255) NOT NULL,
  `age` int(3) DEFAULT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `about` text DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phonenumber` varchar(50) DEFAULT NULL,
  `log_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `profile_picture_path` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`attendance_id`, `event_id`, `fullname`, `age`, `gender`, `about`, `address`, `email`, `phonenumber`, `log_timestamp`, `profile_picture_path`) VALUES
(1, 1, 'Alice Smith', 35, 'Female', NULL, NULL, 'alice@example.com', '555-0101', '2025-09-28 04:42:22', NULL),
(2, 2, 'Bob Johnson', 28, 'Male', NULL, NULL, 'bob@example.com', '555-0102', '2025-09-28 04:42:22', NULL),
(3, 1, 'shehara Heshan', 20, 'Male', 'I am developer', 'ketangoda watta', 'shehara@gmail.com', '555-777-888', '2025-09-28 05:41:51', NULL),
(4, 1, 'gfgfgfg', 66, 'male', 'hghghg', 'hgghgh', 'fddf@gmail.com', '666-777-999', '2025-09-28 06:10:43', NULL),
(5, 1, 'fdfdfdf', 66, 'male', 'sasas', 'dsasa', 'dsds@gmail.com', '333-5555-666', '2025-09-28 06:54:36', NULL),
(6, 3, 'test', 0, 'male', 'jhhjhjh', 'jhh', 'ss@hmail.com', '6669990000', '2025-09-28 06:56:13', NULL),
(7, 7, 'Manoj chathuranga', 55, 'Male', 'I am a developer that like about tech.', 'main street 21/4 hambanthota thissamaharama.', 'manoj@gmail.com', '999-777-555', '2025-09-28 07:31:29', NULL),
(8, 7, 'test name 2', 30, 'Male', 'dsdsdsdsdsdsds', '452, Main Street Ambalangoda', 'manov@gmail.com', '999-777-5558', '2025-09-28 08:07:18', 'uploads/profile_pictures/profile_7_68d8ecb6626c4.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE `events` (
  `event_id` int(11) NOT NULL,
  `event_name` varchar(255) NOT NULL,
  `event_description` text DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `event_type` varchar(100) NOT NULL,
  `attendance_type` varchar(100) NOT NULL,
  `log_timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`event_id`, `event_name`, `event_description`, `start_date`, `end_date`, `start_time`, `end_time`, `event_type`, `attendance_type`, `log_timestamp`) VALUES
(1, 'fdfdfdf', 'fdfdffdf', '2025-09-01', '2025-09-11', '03:41:57', '06:42:01', 'Online', 'Invite-only', '2025-09-27 14:12:10'),
(2, 'Test Event 2', 'Test Event Description ', '2025-09-01', '2025-09-05', '04:01:54', '20:02:00', 'Physical', 'Open', '2025-09-27 17:32:20'),
(3, 'ddssd', 'dsdsds', '2025-09-02', '2025-09-07', '13:06:24', '18:06:28', 'Online', 'VIP', '2025-09-27 17:36:36'),
(4, 'gfgfgfg', 'gfgfgfg', '2025-09-02', '2025-09-04', '10:17:07', '12:17:11', 'Online', 'Open', '2025-09-27 17:47:19'),
(5, 'jkjkjkjkjjjj', 'kjkjkjkj', '2025-09-10', '2025-09-18', '11:18:02', '17:18:08', 'Physical', 'Ticketed', '2025-09-27 17:48:17'),
(6, 'yyyyyyyyyyyyyyyyyy', 'yyyyyyyyyyyyyyyy', '2025-09-02', '2025-09-10', '10:38:32', '15:38:36', 'Physical', 'Invite-only', '2025-09-27 18:08:50'),
(7, 'Inoka', 'test inoka', '2025-09-01', '2025-09-10', '04:35:25', '18:35:29', 'Physical', 'Open', '2025-09-27 21:06:04');

-- --------------------------------------------------------

--
-- Table structure for table `event_members`
--

CREATE TABLE `event_members` (
  `member_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `role` varchar(50) NOT NULL DEFAULT 'Collaborator',
  `joined_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `profile_picture_path` varchar(512) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` int(3) DEFAULT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`attendance_id`),
  ADD KEY `event_id` (`event_id`);

--
-- Indexes for table `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`event_id`);

--
-- Indexes for table `event_members`
--
ALTER TABLE `event_members`
  ADD PRIMARY KEY (`member_id`),
  ADD UNIQUE KEY `uk_user_event` (`user_id`,`event_id`),
  ADD KEY `fk_member_event` (`event_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `uk_email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `attendance_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `events`
--
ALTER TABLE `events`
  MODIFY `event_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `event_members`
--
ALTER TABLE `event_members`
  MODIFY `member_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`) ON DELETE CASCADE;

--
-- Constraints for table `event_members`
--
ALTER TABLE `event_members`
  ADD CONSTRAINT `fk_member_event` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_member_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
