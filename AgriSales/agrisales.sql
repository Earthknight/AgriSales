-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 06, 2018 at 08:20 AM
-- Server version: 10.1.33-MariaDB
-- PHP Version: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `agrisales`
--

-- --------------------------------------------------------

--
-- Table structure for table `customertable`
--

CREATE TABLE `customertable` (
  `Id` int(11) NOT NULL,
  `Name` varchar(30) NOT NULL,
  `Contact` varchar(30) NOT NULL,
  `Address` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customertable`
--

INSERT INTO `customertable` (`Id`, `Name`, `Contact`, `Address`) VALUES
(1, 'smith', '1234567899', 'xx yy zz xx yy zz');

-- --------------------------------------------------------

--
-- Table structure for table `inventorytable`
--

CREATE TABLE `inventorytable` (
  `Id` int(11) NOT NULL,
  `ProductType` varchar(50) NOT NULL,
  `ProductName` varchar(50) NOT NULL,
  `Quantity` varchar(50) NOT NULL,
  `PurchaseRate` double NOT NULL DEFAULT '0',
  `SellingRate` double NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `inventorytable`
--

INSERT INTO `inventorytable` (`Id`, `ProductType`, `ProductName`, `Quantity`, `PurchaseRate`, `SellingRate`) VALUES
(1, 'Fresh Vegitables', 'mango', '95', 50, 55),
(2, 'Processed food', 'jellybeans', '200', 15, 20);

-- --------------------------------------------------------

--
-- Table structure for table `purchasetable`
--

CREATE TABLE `purchasetable` (
  `Id` int(11) NOT NULL,
  `ProductType` varchar(50) NOT NULL,
  `ProductName` varchar(50) NOT NULL,
  `SellerName` varchar(100) NOT NULL,
  `Quantity` varchar(50) NOT NULL,
  `PurchaseDate` date DEFAULT NULL,
  `PurchaseRate` double NOT NULL DEFAULT '0',
  `SellingRate` double NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `purchasetable`
--

INSERT INTO `purchasetable` (`Id`, `ProductType`, `ProductName`, `SellerName`, `Quantity`, `PurchaseDate`, `PurchaseRate`, `SellingRate`) VALUES
(2, 'Processed food', 'jellybeans', 'john', '200', '2018-09-06', 15, 20);

-- --------------------------------------------------------

--
-- Table structure for table `salesitemtable`
--

CREATE TABLE `salesitemtable` (
  `SaleId` int(11) NOT NULL,
  `ProductType` varchar(50) NOT NULL,
  `ProductName` varchar(50) NOT NULL,
  `Rate` double NOT NULL,
  `Qty` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `salesitemtable`
--

INSERT INTO `salesitemtable` (`SaleId`, `ProductType`, `ProductName`, `Rate`, `Qty`) VALUES
(1, 'Fresh Vegitables', 'mango', 275, 5);

-- --------------------------------------------------------

--
-- Table structure for table `salestable`
--

CREATE TABLE `salestable` (
  `Id` int(11) NOT NULL,
  `CustomerName` varchar(30) NOT NULL,
  `Date` date NOT NULL,
  `Amount` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `salestable`
--

INSERT INTO `salestable` (`Id`, `CustomerName`, `Date`, `Amount`) VALUES
(1, 'smith', '2018-09-06', 275);

-- --------------------------------------------------------

--
-- Table structure for table `sellertable`
--

CREATE TABLE `sellertable` (
  `Id` int(11) NOT NULL,
  `Name` varchar(30) NOT NULL,
  `Contact` varchar(30) NOT NULL,
  `Address` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sellertable`
--

INSERT INTO `sellertable` (`Id`, `Name`, `Contact`, `Address`) VALUES
(1, 'john', '1234567891', 'xyz xyz xyz');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customertable`
--
ALTER TABLE `customertable`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `inventorytable`
--
ALTER TABLE `inventorytable`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `purchasetable`
--
ALTER TABLE `purchasetable`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `salesitemtable`
--
ALTER TABLE `salesitemtable`
  ADD KEY `SaleId` (`SaleId`),
  ADD KEY `par1_ind` (`SaleId`);

--
-- Indexes for table `salestable`
--
ALTER TABLE `salestable`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `sellertable`
--
ALTER TABLE `sellertable`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customertable`
--
ALTER TABLE `customertable`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `inventorytable`
--
ALTER TABLE `inventorytable`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `purchasetable`
--
ALTER TABLE `purchasetable`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `salestable`
--
ALTER TABLE `salestable`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `sellertable`
--
ALTER TABLE `sellertable`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `salesitemtable`
--
ALTER TABLE `salesitemtable`
  ADD CONSTRAINT `fk_SaleTable` FOREIGN KEY (`SaleId`) REFERENCES `salestable` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
