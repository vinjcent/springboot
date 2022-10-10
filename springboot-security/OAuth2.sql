-- used in tests that use HSQL

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Table structure for oauth_client_details
DROP TABLE IF EXISTS `clientdetails`;
CREATE TABLE clientdetails (
  `appId` VARCHAR(256) NOT NULL PRIMARY KEY,
  `resourceIds` VARCHAR(256) DEFAULT NULL,
  `appSecret` VARCHAR(256) DEFAULT NULL,
  `scope` VARCHAR(256) DEFAULT NULL,
  `grantTypes` VARCHAR(256) DEFAULT NULL,
  `redirectUrl` VARCHAR(256) DEFAULT NULL,
  `authorities` VARCHAR(256) DEFAULT NULL,
  `access_token_validity` INTEGER DEFAULT NULL,
  `refresh_token_validity` INTEGER DEFAULT NULL,
  `additionalInformation` VARCHAR(4096) DEFAULT NULL,
  `autoApproveScopes` VARCHAR(256) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- Table structure for oauth_access_token
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE oauth_access_token (
  token_id VARCHAR(256) DEFAULT NULL,
  token BLOB,
  authentication_id VARCHAR(256) NOT NULL PRIMARY KEY,
  user_name VARCHAR(256) DEFAULT NULL,
  client_id VARCHAR(256) DEFAULT NULL,
  authentication BLOB,
  refresh_token VARCHAR(256) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- Table structure for oauth_approvals
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE oauth_approvals (
	`userId` VARCHAR(256) DEFAULT NULL,
	`clientId` VARCHAR(256) DEFAULT NULL,
	`scope` VARCHAR(256) DEFAULT NULL,
	`status` VARCHAR(10) DEFAULT NULL,
	`expiresAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`lastModifiedAt` DATE DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- Table structure for oauth_client_details
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE oauth_client_details (
  `client_id` VARCHAR(256) NOT NULL PRIMARY KEY,
  `resource_ids` VARCHAR(256) DEFAULT NULL,
  `client_secret` VARCHAR(256) DEFAULT NULL,
  `scope` VARCHAR(256) DEFAULT NULL,
  `authorized_grant_types` VARCHAR(256) DEFAULT NULL,
  `web_server_redirect_uri` VARCHAR(256) DEFAULT NULL,
  `authorities` VARCHAR(256) DEFAULT NULL,
  `access_token_validity` INT(11) DEFAULT NULL,
  `refresh_token_validity` INT(11) DEFAULT NULL,
  `additional_information` VARCHAR(4096),
  `autoapprove` VARCHAR(256) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- Table structure for oauth_client_token
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE oauth_client_token (
  `token_id` VARCHAR(256) DEFAULT NULL,
  `token` BLOB,
  `authentication_id` VARCHAR(256) NOT NULL PRIMARY KEY,
  `user_name` VARCHAR(256) DEFAULT NULL,
  `client_id` VARCHAR(256) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- Table structure for oauth_refresh_token
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE oauth_refresh_token (
  `token_id` VARCHAR(256) DEFAULT NULL,
  `token` BLOB,
  `authentication` BLOB
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- Table structure for oauth_code
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE oauth_code (
  `code` VARCHAR(256) DEFAULT NULL,
  `authentication` BLOB
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;



-- 再向`oauth_client_details`插入一条数据
insert into `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) values('clientId',NULL,'$2a$10$QCsINtuRfP8kM112xRVdvuI58MrefLlEP2mM0kzB5KZCPhnOf4392','read','authorization_code,refresh_token','http://www.baidu.com',NULL,NULL,NULL,NULL,NULL);
