-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 28, 2025 at 06:18 AM
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

--
-- Indexes for dumped tables
--

--
-- Indexes for table `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`event_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `events`
--
ALTER TABLE `events`
  MODIFY `event_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
