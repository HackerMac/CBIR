/*
Navicat MySQL Data Transfer

Source Server         : testjdbc
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : image_feature

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2017-07-09 09:33:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for color
-- ----------------------------
DROP TABLE IF EXISTS `color`;
CREATE TABLE `color` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `H1` float(100,30) NOT NULL,
  `H2` float(100,30) NOT NULL,
  `H3` float(100,30) NOT NULL,
  `S1` float(100,30) NOT NULL,
  `S2` float(100,30) NOT NULL,
  `S3` float(100,30) NOT NULL,
  `I1` float(100,30) NOT NULL,
  `I2` float(100,30) NOT NULL,
  `I3` float(100,30) NOT NULL,
  `Path` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1088 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shape
-- ----------------------------
DROP TABLE IF EXISTS `shape`;
CREATE TABLE `shape` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Shape0` float(100,30) NOT NULL,
  `Shape1` float(100,30) NOT NULL,
  `Shape2` float(100,30) NOT NULL,
  `Shape3` float(100,30) NOT NULL,
  `Shape4` float(100,30) NOT NULL,
  `Shape5` float(100,30) NOT NULL,
  `Shape6` float(100,30) NOT NULL,
  `Shape7` float(100,30) NOT NULL,
  `Path` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1088 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for texture
-- ----------------------------
DROP TABLE IF EXISTS `texture`;
CREATE TABLE `texture` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Exp1` float(100,30) NOT NULL,
  `Exp2` float(100,30) NOT NULL,
  `Exp3` float(100,30) NOT NULL,
  `Exp4` float(100,30) NOT NULL,
  `Stadv1` float(100,30) NOT NULL,
  `Stadv2` float(100,30) NOT NULL,
  `Stadv3` float(100,30) NOT NULL,
  `Stadv4` float(100,30) NOT NULL,
  `Path` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1088 DEFAULT CHARSET=utf8;
