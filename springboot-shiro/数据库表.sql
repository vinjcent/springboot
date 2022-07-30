-- 按照三大范式,建立数据库表
-- 什么样的用户就有什么样的角色
-- 什么样的角色就有什么样的权限
-- 第一范式：要求任何一张表必须有主键，每一个字段原子性不可再分
-- 第二范式：建立在第一范式的基础之上，要求所有非主键字段完全依赖主键，不要产生部分依赖
-- 第三范式：建立在第二范式的基础之上，要求所有非主键字段直接依赖主键，不要产生传递依赖

### 用户表
CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '姓名',
  `username` VARCHAR(40) DEFAULT NULL COMMENT '用户名',
  `password` VARCHAR(100) DEFAULT NULL COMMENT '密码',
  `salt` VARCHAR(100) DEFAULT NULL COMMENT '盐',
  `state` INT(11) DEFAULT '1' COMMENT '状态,0为冻结,1为正常',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8

### 角色表
CREATE TABLE `role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '角色名',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8

### 权限表
CREATE TABLE `permission` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '权限名',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8

### 角色记录表
CREATE TABLE `viewrole` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userId` INT(11) DEFAULT NULL,
  `roleId` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  KEY `roleId` (`roleId`),
  CONSTRAINT `viewrole_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `viewrole_ibfk_2` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8

### 角色权限表
CREATE TABLE `viewperm` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `roleId` INT(11) DEFAULT NULL,
  `permId` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `roleId` (`roleId`),
  KEY `permId` (`permId`),
  CONSTRAINT `viewperm_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  CONSTRAINT `viewperm_ibfk_2` FOREIGN KEY (`permId`) REFERENCES `permission` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8