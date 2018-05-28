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
-- Records of deploy
-- ----------------------------
INSERT INTO `deploy` VALUES ('6', 'nginx', 'java', 'default', 'nginx:1.7.9', '250', '0.25', '2', '80', 'TCP', 'nginx.yuan.com', '192.168.1.10');


-- ----------------------------
-- Table structure for ppl
-- ----------------------------
DROP TABLE IF EXISTS `ppl`;
CREATE TABLE `ppl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `script` text COMMENT '构建脚本,ppl脚本',
  `env` text DEFAULT NULL COMMENT '环境变量也就是传参',
  `language` varchar(255) DEFAULT NULL,
  `metadata` text DEFAULT NULL,
  `node` varchar(255) DEFAULT NULL,
  `webhook` boolean DEFAULT false,
  `authToken` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ppl
-- ----------------------------
INSERT INTO `ppl` VALUES ('3',
'java_licai',
'\nnode(\'master\'){\ndef dockerfile_path= \'\'\nif (isUnix()){\nstage(\'更新代码\'){\nparallel \'git下载\' : {\ndef scm_git_url= \'git@git.cloudos.yihecloud.com:paas/licai2.git\'\ndef scm_git_credentialId= \'licai2_repos_1515119049745\'\ndef scm_git_branch= \'master\'\n	checkout([$class : \'GitSCM\', branches : [[name : scm_git_branch]], doGenerateSubmoduleConfigurations : false, extensions : [], submoduleCfg : [], userRemoteConfigs : [[credentialsId : scm_git_credentialId, url : scm_git_url]]])\n}\n}\nstage(\'代码编译\'){\nparallel \'java代码编译j\' : {\ndef compiler_cmd= \'mvn -Dmaven.test.failure.ignore clean install cobertura:cobertura -Dcobertura.report.format=xml\'\n sh compiler_cmd\n}\n}\nstage(\'构建\'){\ndir(\"$dockerfile_path\"){\nparallel \'docker镜像打包\' : {\ndef docker_server= \'192.168.1.10:2375\'\ndef docker_credential= \'credential_default_docker\'\ndef docker_url= \'$docker_url\'\ndef docker_image= \'registry.service.ob.local:5001/t/paas/licai2:yy-$BUILD_TIMESTAMP\'\nwithDockerServer([uri: docker_server]) {\r\n withDockerRegistry([credentialsId: docker_credential, url: docker_url]) {\r\n    	def image = docker.build(docker_image)\r\n    		image.push();\r\n		}\r\n}\n}\n}\n}\n}else {\nstage(\'更新代码\'){\nparallel \'git下载\' : {\ndef scm_git_url= \'git@git.cloudos.yihecloud.com:paas/licai2.git\'\ndef scm_git_credentialId= \'licai2_repos_1515119049745\'\ndef scm_git_branch= \'master\'\ncheckout([$class : \'GitSCM\', branches : [[name : scm_git_branch]], doGenerateSubmoduleConfigurations : false, extensions : [], submoduleCfg : [], userRemoteConfigs : [[credentialsId : scm_git_credentialId, url : scm_git_url]]])\n}\n}\nstage(\'代码编译\'){\nparallel \'java代码编译j\' : {\ndef compiler_cmd= \'mvn -Dmaven.test.failure.ignore clean install cobertura:cobertura -Dcobertura.report.format=xml\'\n bat compiler_cmd\n}\n}\nstage(\'构建\'){\ndir(\"$dockerfile_path\"){\nparallel \'docker镜像打包\' : {\ndef docker_server= \'192.168.1.10:2375\'\ndef docker_credential= \'credential_default_docker\'\ndef docker_url= \'$docker_url\'\ndef docker_image= \'registry.service.ob.local:5001/t/paas/licai2:yy-$BUILD_TIMESTAMP\'\nwithDockerServer([uri: docker_server]) {\r\n withDockerRegistry([credentialsId: docker_credential, url: docker_url]) {\r\n    	def image = docker.build(docker_image)\r\n    		image.push();\r\n		}\r\n}\n}\n}\n}\n}\n}',
'{\"id\":1,\"language\":\"java\",\"name\":\"java构建打包\",\"stages\":[{\"id\":1,\"name\":\"更新代码\",\"tasks\":[{\"description\":\"下载git代码\",\"id\":1,\"input\":\"[{\\\"key\\\":\\\"scm_git_url\\\",\\\"value\\\":\\\"$scm_git_url\\\",\\\"description\\\":\\\"源码git_url\\\",\\\"is_gobal\\\":\\\"false\\\"},{\\\"key\\\":\\\"scm_git_credentialId\\\",\\\"value\\\":\\\"$scm_git_credentialId\\\",\\\"description\\\":\\\"源码仓的密钥\\\",\\\"is_gobal\\\":\\\"false\\\"},{\\\"key\\\":\\\"scm_git_branch\\\",\\\"value\\\":\\\"$scm_git_branch\\\",\\\"description\\\":\\\"分支\\\",\\\"is_gobal\\\":\\\"false\\\"}]\",\"linuxScript\":\"\\tcheckout([$class : \'GitSCM\', branches : [[name : scm_git_branch]], doGenerateSubmoduleConfigurations : false, extensions : [], submoduleCfg : [], userRemoteConfigs : [[credentialsId : scm_git_credentialId, url : scm_git_url]]])\",\"name\":\"git下载\",\"stageId\":1,\"taskParams\":[{\"description\":\"源码git_url\",\"gobal\":false,\"key\":\"scm_git_url\",\"value\":\"git@git.cloudos.yihecloud.com:paas/licai2.git\"},{\"description\":\"源码仓的密钥\",\"gobal\":false,\"key\":\"scm_git_credentialId\",\"value\":\"licai2_repos_1515119049745\"},{\"description\":\"分支\",\"gobal\":false,\"key\":\"scm_git_branch\",\"value\":\"master\"}],\"windowsScript\":\"checkout([$class : \'GitSCM\', branches : [[name : scm_git_branch]], doGenerateSubmoduleConfigurations : false, extensions : [], submoduleCfg : [], userRemoteConfigs : [[credentialsId : scm_git_credentialId, url : scm_git_url]]])\",\"workspace\":\"\"}]},{\"id\":2,\"name\":\"代码编译\",\"tasks\":[{\"description\":\"java代码编译\",\"id\":3,\"input\":\"[{\\\"key\\\":\\\"compiler_cmd\\\",\\\"value\\\":\\\"$compiler_cmd\\\",\\\"description\\\":\\\"编译命令\\\",\\\"is_gobal\\\":\\\"false\\\"}]\",\"linuxScript\":\" sh compiler_cmd\",\"name\":\"java代码编译j\",\"stageId\":2,\"taskParams\":[{\"description\":\"编译命令\",\"gobal\":false,\"key\":\"compiler_cmd\",\"value\":\"mvn -Dmaven.test.failure.ignore clean install cobertura:cobertura -Dcobertura.report.format=xml\"}],\"windowsScript\":\" bat compiler_cmd\"}]},{\"id\":3,\"name\":\"构建\",\"tasks\":[{\"description\":\"docker镜像打包\",\"id\":4,\"input\":\"[{\\\"key\\\":\\\"docker_server\\\",\\\"value\\\":\\\"$docker_server\\\",\\\"description\\\":\\\"docker 服务器地址\\\",\\\"is_gobal\\\":\\\"false\\\"},{\\\"key\\\":\\\"docker_credential\\\",\\\"value\\\":\\\"$docker_credential\\\",\\\"description\\\":\\\"docker访问密钥\\\",\\\"is_gobal\\\":\\\"false\\\"}, {\\\"key\\\":\\\"docker_url\\\",\\\"value\\\":\\\"$docker_url\\\",\\\"description\\\":\\\"镜像仓库地址\\\",\\\"is_gobal\\\" : \\\"false\\\"}, {\\\"key\\\":\\\"docker_image\\\",\\\"value\\\":\\\"$docker_image\\\",\\\"description\\\":\\\"镜像名称\\\",\\\"is_gobal\\\":\\\"false\\\"},{\\\"key\\\" : \\\"dockerfile_path\\\",\\\"value\\\" : \\\"\\\",\\\"description\\\" : \\\"dockerfile 相对路径\\\",\\\"is_gobal\\\" : \\\"true\\\"}]\",\"linuxScript\":\"withDockerServer([uri: docker_server]) {\\r\\n withDockerRegistry([credentialsId: docker_credential, url: docker_url]) {\\r\\n    \\tdef image = docker.build(docker_image)\\r\\n    \\t\\timage.push();\\r\\n\\t\\t}\\r\\n}\",\"name\":\"docker镜像打包\",\"stageId\":3,\"taskParams\":[{\"description\":\"docker 服务器地址\",\"gobal\":false,\"key\":\"docker_server\",\"value\":\"192.168.1.10:2375\"},{\"description\":\"docker访问密钥\",\"gobal\":false,\"key\":\"docker_credential\",\"value\":\"credential_default_docker\"},{\"description\":\"镜像仓库地址\",\"gobal\":false,\"key\":\"docker_url\",\"value\":\"$docker_url\"},{\"description\":\"镜像名称\",\"gobal\":false,\"key\":\"docker_image\",\"value\":\"registry.service.ob.local:5001/t/paas/licai2:yy-$BUILD_TIMESTAMP\"},{\"description\":\"dockerfile 相对路径\",\"gobal\":true,\"key\":\"dockerfile_path\",\"value\":\"\"}],\"windowsScript\":\"withDockerServer([uri: docker_server]) {\\r\\n withDockerRegistry([credentialsId: docker_credential, url: docker_url]) {\\r\\n    \\tdef image = docker.build(docker_image)\\r\\n    \\t\\timage.push();\\r\\n\\t\\t}\\r\\n}\",\"workspace\":\"$dockerfile_path\"}]}],\"tplStageTask\":\"[{\\\"stage_id\\\":1,task_ids:\\\"1\\\"},{\\\"stage_id\\\":2,task_ids :\\\"3\\\"},{\\\"stage_id\\\":3,task_ids:\\\"4\\\"}]\",\"tplStageTasks\":[{\"stageId\":1,\"taskIds\":[1]},{\"stageId\":2,\"taskIds\":[3]},{\"stageId\":3,\"taskIds\":[4]}]}',
'master',NULL,null,false,null);


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
-- Records of ppl_rotation
-- ----------------------------

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
-- Records of ppl_stage
-- ----------------------------
INSERT INTO `ppl_stage` VALUES ('1', '更新代码');
INSERT INTO `ppl_stage` VALUES ('2', '代码编译');
INSERT INTO `ppl_stage` VALUES ('3', '构建');
INSERT INTO `ppl_stage` VALUES ('4', '归档');
INSERT INTO `ppl_stage` VALUES ('5', '部署');
INSERT INTO `ppl_stage` VALUES ('6', '单元测试');

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
-- Records of ppl_task
-- ----------------------------
INSERT INTO `ppl_task` VALUES ('1', 'git下载', '[{\"key\":\"scm_git_url\",\"value\":\"$scm_git_url\",\"description\":\"源码git_url\",\"is_gobal\":\"false\"},{\"key\":\"scm_git_credentialId\",\"value\":\"$scm_git_credentialId\",\"description\":\"源码仓的密钥\",\"is_gobal\":\"false\"},{\"key\":\"scm_git_branch\",\"value\":\"$scm_git_branch\",\"description\":\"分支\",\"is_gobal\":\"false\"}]', 'checkout([$class : \'GitSCM\', branches : [[name : scm_git_branch]], doGenerateSubmoduleConfigurations : false, extensions : [], submoduleCfg : [], userRemoteConfigs : [[credentialsId : scm_git_credentialId, url : scm_git_url]]])', '	checkout([$class : \'GitSCM\', branches : [[name : scm_git_branch]], doGenerateSubmoduleConfigurations : false, extensions : [], submoduleCfg : [], userRemoteConfigs : [[credentialsId : scm_git_credentialId, url : scm_git_url]]])', '下载git代码', '1', '');
INSERT INTO `ppl_task` VALUES ('2', 'svn下载', null, null, null, null, '1', null);
INSERT INTO `ppl_task` VALUES ('3', 'java代码编译j', '[{\"key\":\"compiler_cmd\",\"value\":\"$compiler_cmd\",\"description\":\"编译命令\",\"is_gobal\":\"false\"}]', ' bat compiler_cmd', ' sh compiler_cmd', 'java代码编译', '2', null);
INSERT INTO `ppl_task` VALUES ('4', 'docker镜像打包', '[{\"key\":\"docker_server\",\"value\":\"$docker_server\",\"description\":\"docker 服务器地址\",\"is_gobal\":\"false\"},{\"key\":\"docker_credential\",\"value\":\"$docker_credential\",\"description\":\"docker访问密钥\",\"is_gobal\":\"false\"}, {\"key\":\"docker_url\",\"value\":\"$docker_url\",\"description\":\"镜像仓库地址\",\"is_gobal\" : \"false\"}, {\"key\":\"docker_image\",\"value\":\"$docker_image\",\"description\":\"镜像名称\",\"is_gobal\":\"false\"},{\"key\" : \"dockerfile_path\",\"value\" : \"\",\"description\" : \"dockerfile 相对路径\",\"is_gobal\" : \"true\"}]', 'withDockerServer([uri: docker_server]) {\r\n withDockerRegistry([credentialsId: docker_credential, url: docker_url]) {\r\n    	def image = docker.build(docker_image)\r\n    		image.push();\r\n		}\r\n}', 'withDockerServer([uri: docker_server]) {\r\n withDockerRegistry([credentialsId: docker_credential, url: docker_url]) {\r\n    	def image = docker.build(docker_image)\r\n    		image.push();\r\n		}\r\n}', 'docker镜像打包', '3', '$dockerfile_path');
INSERT INTO `ppl_task` VALUES ('5', 'java单元测试', null, ' junit allowEmptyResults: true, healthScaleFactor: 0.0, testResults: testResults', ' junit allowEmptyResults: true, healthScaleFactor: 0.0, testResults: testResults', null, null, null);


-- ----------------------------
-- Table structure for ppl_template
-- ----------------------------
DROP TABLE IF EXISTS `ppl_template`;
CREATE TABLE `ppl_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL COMMENT '所属开发语言',
  `tplStageTask` varchar(255) DEFAULT NULL COMMENT '用逗号隔开 1,2,3（表示stage有1 2 3）',
  `defaultParams` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ppl_template
-- ----------------------------
INSERT INTO `ppl_template` VALUES ('1', 'java构建打包', '更新代码+编译+构建镜像', 'java', '[{\"stage_id\":1,task_ids:\"1\"},{\"stage_id\":2,task_ids :\"3\"},{\"stage_id\":3,task_ids:\"4\"}]', null);

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
-- Records of ppl_trigger
-- ----------------------------

-- ----------------------------
-- Table structure for t1
-- ----------------------------
DROP TABLE IF EXISTS `t1`;
CREATE TABLE `t1` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sysconfig
-- ----------------------------
