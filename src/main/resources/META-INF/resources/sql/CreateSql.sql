/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50553
Source Host           : localhost:3306
Source Database       : temp_cloud

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2018-01-30 11:54:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for deploy
-- ----------------------------
DROP TABLE IF EXISTS `deploy`;
CREATE TABLE `deploy` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `namespace` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `memory` varchar(255) DEFAULT NULL,
  `cpu` varchar(255) DEFAULT NULL,
  `replicas` varchar(255) DEFAULT NULL,
  `container_port` varchar(255) DEFAULT NULL,
  `protocol` varchar(255) DEFAULT NULL,
  `ingress_domain` varchar(255) DEFAULT NULL,
  `ingress_ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ppl
-- ----------------------------
DROP TABLE IF EXISTS `ppl`;
CREATE TABLE `ppl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `script` text COMMENT '构建脚本,ppl脚本',
  `env` varchar(255) DEFAULT NULL COMMENT '环境变量也就是传参',
  `language` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ppl_rotation
-- ----------------------------
DROP TABLE IF EXISTS `ppl_rotation`;
CREATE TABLE `ppl_rotation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sad` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ppl_stage
-- ----------------------------
DROP TABLE IF EXISTS `ppl_stage`;
CREATE TABLE `ppl_stage` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ppl_task
-- ----------------------------
DROP TABLE IF EXISTS `ppl_task`;
CREATE TABLE `ppl_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `input` text,
  `windows_script` text,
  `linux_script` text COMMENT 'input输入为json数据串',
  `description` varchar(255) DEFAULT NULL,
  `stage_id` int(11) DEFAULT NULL,
  `workspace` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ppl_template
-- ----------------------------
DROP TABLE IF EXISTS `ppl_template`;
CREATE TABLE `ppl_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL COMMENT '所属开发语言',
  `tpl_stage_task` varchar(255) DEFAULT NULL COMMENT '用逗号隔开 1,2,3（表示stage有1 2 3）',
  `default_params` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ppl_trigger
-- ----------------------------
DROP TABLE IF EXISTS `ppl_trigger`;
CREATE TABLE `ppl_trigger` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t1
-- ----------------------------
DROP TABLE IF EXISTS `t1`;
CREATE TABLE `t1` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
