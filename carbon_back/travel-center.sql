/*
 Navicat MySQL Data Transfer

 Source Server         : 192.168.1.131_3306
 Source Server Type    : MySQL
 Source Server Version : 80018 (8.0.18)
 Source Host           : 192.168.3.88:3306
 Source Schema         : travel-center

 Target Server Type    : MySQL
 Target Server Version : 80018 (8.0.18)
 File Encoding         : 65001

 Date: 14/11/2022 14:23:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bd_sms_record
-- ----------------------------
DROP TABLE IF EXISTS `bd_sms_record`;
CREATE TABLE `bd_sms_record`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '短信发送记录编号',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短信平台账号',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接受短信的手机号',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '短信内容',
  `add_ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '添加记录ip',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `template` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '短信模板ID',
  `resultcode` int(6) UNSIGNED NULL DEFAULT NULL COMMENT '状态码 100=成功,130=失败,131=空号,132=停机,133=关机,134=无状态',
  `record_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '发送记录id',
  `memo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '短信平台返回信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '短信发送记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bd_sms_record
-- ----------------------------

-- ----------------------------
-- Table structure for bd_system_config
-- ----------------------------
DROP TABLE IF EXISTS `bd_system_config`;
CREATE TABLE `bd_system_config`  (
  `id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配置id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字段名称',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字段提示文字',
  `form_id` int(10) NULL DEFAULT 0 COMMENT '表单id',
  `value` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '值',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bd_system_config
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `id` int(11) NULL DEFAULT NULL,
  `client_id` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_secret_str` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `if_limit` int(11) NULL DEFAULT NULL,
  `limit_count` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES (1, 'app', NULL, '$2a$10$i3F515wEDiB4Gvj9ym9Prui0dasRttEUQ9ink4Wpgb4zEDCAlV8zO', 'app', 'app', 'password,refresh_token', NULL, NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (2, 'mobile', 'mobile,test', '$2a$10$ULxRssv/4NWOc388lZFbyus3IFfsbcpG/BZOq4TRxDhsx5HHIR7Jm', 'mobile', 'all', 'password,refresh_token', NULL, NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (4, 'webApp', NULL, '$2a$10$06msMGYRH8nrm4iVnKFNKOoddB8wOwymVhbUzw/d3ZixD7Nq8ot72', 'webApp', 'app', 'authorization_code,password,refresh_token,client_credentials', NULL, NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (5, 'beck', '', '$2a$10$56LGyH.2wOFNNp3ScUkspOMdyRnenYhnWEnfI0itIFfsXsd5ZhKh.', 'beck', 'all', 'authorization_code,password,refresh_token,client_credentials', 'http://www.baidu.com', '', 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (6, 'owen', NULL, '$2a$10$a1ZEXiZQr604LN.wVxet.etPm6RvDs.HIaXP48J2HKRaEnZORTVwe', 'owen', 'app', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:9997/dashboard/login', NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (8, 'testOne', '', '$2a$10$nI9kx19HHJTkJq0kMRPZ6uu/4uW7J9kPIpZ8YjFmbUlvwJmorc5Qa', 'testOne', 'all', 'authorization_code,password,refresh_token,client_credentials', 'http://bai.com', '', 18000, 18000, '{}', 'true', 0, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (9, 'gwapi', '', '$2a$10$l7plpxQk42cuKbB8tbNe8eAA6v2xA6xkPXsjGEezago239102LRL2', 'gwapi', 'all', 'authorization_code,password,refresh_token,client_credentials', 'https://www.baidu.co', '', 18000, 18000, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (10, 'testtwo', '', '$2a$10$49ESIYmzu1n.cGzwMLRgleQMk0.kBTMOYnW4WUBDKwu9V23qOBovG', 'testtwo', 'all', 'authorization_code,password,refresh_token,client_credentials', 'locahost:9090/test', '', 18000, 18000, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (11, 'uc-app', '', '$2a$10$8UxEUaT2D2vSTJvTA/7YbODgCbK44bozsNA1kvMFSz8R153Xat7UO', 'uc-app', 'all', 'authorization_code,password,refresh_token,client_credentials', 'www.baidu.com', '', 18000, 18000, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES (12, 'testtwo4', '', '$2a$10$Q8Qg5RQv1t0NFyL8Epfnj.wB/5NQnNJRMv5yIOVyeZ3ACXvzGwloq', 'testtwo4', 'all', 'implicit,password,refresh_token', 'locahost:9090/test', '', 18000, 18000, '{}', 'true', 1, 1, 10000);

-- ----------------------------
-- Table structure for sys_auth_role_org_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_role_org_rel`;
CREATE TABLE `sys_auth_role_org_rel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色Id',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_auth_role_org_rel
-- ----------------------------

-- ----------------------------
-- Table structure for sys_client_service
-- ----------------------------
DROP TABLE IF EXISTS `sys_client_service`;
CREATE TABLE `sys_client_service`  (
  `id` int(11) NULL DEFAULT NULL,
  `client_id` int(11) NULL DEFAULT NULL,
  `service_id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_client_service
-- ----------------------------
INSERT INTO `sys_client_service` VALUES (1, 4, 1);
INSERT INTO `sys_client_service` VALUES (2, 4, 2);
INSERT INTO `sys_client_service` VALUES (3, 4, 3);
INSERT INTO `sys_client_service` VALUES (4, 4, 4);
INSERT INTO `sys_client_service` VALUES (5, 4, 5);
INSERT INTO `sys_client_service` VALUES (6, 4, 6);
INSERT INTO `sys_client_service` VALUES (7, 4, 7);
INSERT INTO `sys_client_service` VALUES (8, 4, 8);
INSERT INTO `sys_client_service` VALUES (9, 4, 9);
INSERT INTO `sys_client_service` VALUES (10, 4, 10);
INSERT INTO `sys_client_service` VALUES (11, 4, 11);
INSERT INTO `sys_client_service` VALUES (12, 4, 12);
INSERT INTO `sys_client_service` VALUES (13, 4, 13);
INSERT INTO `sys_client_service` VALUES (14, 4, 14);
INSERT INTO `sys_client_service` VALUES (15, 4, 15);
INSERT INTO `sys_client_service` VALUES (16, 4, 16);
INSERT INTO `sys_client_service` VALUES (17, 4, 17);
INSERT INTO `sys_client_service` VALUES (18, 4, 18);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典编码',
  `status` int(1) NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_slovenian_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, '用户性别', 'sys_user_sex', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '用户性别列表');
INSERT INTO `sys_dict` VALUES (2, '菜单状态', 'sys_show_hide', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '菜单状态列表');
INSERT INTO `sys_dict` VALUES (3, '系统开关', 'sys_normal_disable', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统开关列表');
INSERT INTO `sys_dict` VALUES (4, '任务状态', 'sys_job_status', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '任务状态列表');
INSERT INTO `sys_dict` VALUES (5, '系统是否', 'sys_yes_no', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统是否列表');
INSERT INTO `sys_dict` VALUES (6, '通知类型', 'sys_notice_type', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知类型列表');
INSERT INTO `sys_dict` VALUES (7, '通知状态', 'sys_notice_status', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知状态列表');
INSERT INTO `sys_dict` VALUES (8, '操作类型', 'sys_oper_type', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '操作类型列表');
INSERT INTO `sys_dict` VALUES (9, '系统状态', 'sys_common_status', 0, 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '登录状态列表');
INSERT INTO `sys_dict` VALUES (35, '订单类型', 'orderType', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (36, '订单状态', 'orderState', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (37, '取消原因', 'cancelReason', 0, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '' COMMENT '字典类型',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_slovenian_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_slovenian_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '性别未知');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '是', 'Y', 'sys_yes_no', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统默认是');
INSERT INTO `sys_dict_data` VALUES (11, 2, '否', 'N', 'sys_yes_no', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '系统默认否');
INSERT INTO `sys_dict_data` VALUES (12, 1, '通知', '1', 'sys_notice_type', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '通知');
INSERT INTO `sys_dict_data` VALUES (13, 2, '公告', '2', 'sys_notice_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '公告');
INSERT INTO `sys_dict_data` VALUES (14, 1, '正常', '0', 'sys_notice_status', 'Y', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (15, 2, '关闭', '1', 'sys_notice_status', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '关闭状态');
INSERT INTO `sys_dict_data` VALUES (16, 1, '新增', '1', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '新增操作');
INSERT INTO `sys_dict_data` VALUES (17, 2, '修改', '2', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '修改操作');
INSERT INTO `sys_dict_data` VALUES (18, 3, '删除', '3', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '删除操作');
INSERT INTO `sys_dict_data` VALUES (19, 4, '授权', '4', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '授权操作');
INSERT INTO `sys_dict_data` VALUES (20, 5, '导出', '5', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '导出操作');
INSERT INTO `sys_dict_data` VALUES (21, 6, '导入', '6', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '导入操作');
INSERT INTO `sys_dict_data` VALUES (22, 7, '强退', '7', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '强退操作');
INSERT INTO `sys_dict_data` VALUES (23, 8, '生成代码', '8', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '生成操作');
INSERT INTO `sys_dict_data` VALUES (24, 9, '清空数据', '9', 'sys_oper_type', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '清空操作');
INSERT INTO `sys_dict_data` VALUES (25, 1, '成功', '0', 'sys_common_status', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '正常状态');
INSERT INTO `sys_dict_data` VALUES (26, 2, '失败', '1', 'sys_common_status', 'N', '0', 'admin', '2018-03-16 11:33:00', 'ry', '2018-03-16 11:33:00', '停用状态');
INSERT INTO `sys_dict_data` VALUES (132, 1, '快车', '快车', 'orderType', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (133, 2, '拼车', '拼车', 'orderType', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (135, 3, '出租车', '出租车', 'orderType', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (136, 1, '待接单', '待接单', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (137, 2, '已接单', '已接单', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (138, 3, '接送中', '接送中', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (140, 5, '待支付', '待支付', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (141, 6, '已完结', '已完结', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (142, 7, '客户取消', '客户取消', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (143, 8, '司机取消', '司机取消', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (144, 1, '平台原因', '平台原因', 'cancelReason', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (145, 2, '个人原因', '个人原因', 'cancelReason', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (146, 3, '司机原因', '司机原因', 'cancelReason', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (147, 5, '到达终点', '到达终点', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (148, 6, '司机到达', '司机到达', 'orderState', NULL, '0', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_enum
-- ----------------------------
DROP TABLE IF EXISTS `sys_enum`;
CREATE TABLE `sys_enum`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `enum_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '枚举类型KEY',
  `enum_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '枚举类型名称',
  `element_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '枚举元素KEY',
  `element_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '枚举元素值',
  `is_enable` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否启用',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属分类',
  `sort_no` int(4) NULL DEFAULT NULL COMMENT '排序号',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ukey1`(`enum_key` ASC, `element_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '枚举类型参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_enum
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `css` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `is_menu` int(11) NULL DEFAULT NULL COMMENT '是否菜单 1 是 2 不是',
  `hidden` int(11) NULL DEFAULT NULL COMMENT '是否隐藏,0 false 1 true',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, -1, '系统管理', 'javascript:;', '', 'layui-icon-set', 2, '2019-09-05 11:37:02', '2019-09-05 11:37:02', 1, 0);
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', '#!user', 'system/user.html', 'layui-icon-friends', 2, '2017-11-17 16:56:59', '2019-09-05 11:37:26', 1, 0);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', '#!role', 'system/role.html', 'layui-icon-friends', 3, '2017-11-17 16:56:59', '2019-09-05 11:37:38', 1, 0);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', '#!menus', 'system/menus.html', 'layui-icon-menu-fill', 4, '2017-11-17 16:56:59', '2019-09-05 11:37:57', 1, 0);
INSERT INTO `sys_menu` VALUES (5, 1, '权限管理', '#!permissions', 'system/permissions.html', 'layui-icon-password', 5, '2017-11-17 16:56:59', '2019-09-05 11:37:47', 1, 0);
INSERT INTO `sys_menu` VALUES (6, 1, '我的信息', '#!myInfo', 'system/myInfo.html', '', 10, '2017-11-17 16:56:59', '2018-09-02 06:12:24', 1, 1);
INSERT INTO `sys_menu` VALUES (7, -1, '认证中心', 'javascript:;', '', 'layui-icon-set', 1, '2017-11-17 16:56:59', '2019-03-26 06:56:46', 1, 0);
INSERT INTO `sys_menu` VALUES (8, 7, '应用管理', '#!app', 'attestation/app.html', 'layui-icon-app', 9, '2017-11-17 16:56:59', '2018-08-25 10:57:42', 1, 0);
INSERT INTO `sys_menu` VALUES (9, 7, '服务管理', '#!services', 'attestation/services.html', 'layui-icon-website', 8, '2017-11-17 16:56:59', '2018-09-02 09:34:13', 1, 0);
INSERT INTO `sys_menu` VALUES (10, 7, '令牌管理', '#!token', 'attestation/token.html', 'layui-icon-util', 11, '2018-09-08 13:19:56', '2019-05-27 09:28:39', 1, 0);
INSERT INTO `sys_menu` VALUES (11, -1, '系统监控', 'javascript:;', '', 'layui-icon-set', 3, '2018-08-25 10:41:58', '2018-08-25 10:41:58', 1, 0);
INSERT INTO `sys_menu` VALUES (12, 11, '注册中心', '#!register', 'http://127.0.0.1:1111/', 'layui-icon-engine', 2, '2017-11-17 16:56:59', '2019-05-25 20:34:50', 1, 0);
INSERT INTO `sys_menu` VALUES (13, 11, '服务治理', '#!eureka', 'eureka/list.html', 'layui-icon-engine', 1, '2018-08-30 15:30:19', '2019-05-25 20:34:40', 1, 0);
INSERT INTO `sys_menu` VALUES (14, 11, '慢查询', '#!sql', 'system/sql.html', 'layui-icon-util', 11, '2017-11-17 16:56:59', '2019-05-25 20:35:20', 1, 0);
INSERT INTO `sys_menu` VALUES (15, 11, '文件中心', '#!files', 'files/files.html', 'layui-icon-file', 10, '2017-11-17 16:56:59', '2018-08-25 10:43:33', 1, 0);
INSERT INTO `sys_menu` VALUES (16, 11, '文档中心', '#!swagger', 'https://api.yhofoodie.net/swagger-ui.html', 'layui-icon-app', 9, '2017-11-17 16:56:59', '2021-01-07 13:39:07', 1, 0);
INSERT INTO `sys_menu` VALUES (17, 11, '代码生成器', '#!generator', 'generator/list.html', 'layui-icon-app', 999, '2018-09-05 13:43:06', '2019-05-26 17:01:46', 1, 0);
INSERT INTO `sys_menu` VALUES (18, 11, '日志中心', '#!log', 'system/log.html', 'layui-icon-engine', 18, '2019-03-11 06:30:01', '2019-05-25 20:35:35', 1, 0);
INSERT INTO `sys_menu` VALUES (19, 11, 'prometheus监控', '#!prometheus', 'http://127.0.0.1:9090', 'layui-icon-engine', 1111, '2019-03-27 11:23:31', '2019-05-25 21:13:08', 1, 0);
INSERT INTO `sys_menu` VALUES (20, -1, '任务中心', 'javascript:;', '', 'layui-icon-set', 4, '2018-08-28 16:59:44', '2018-08-28 17:00:19', 1, 0);
INSERT INTO `sys_menu` VALUES (21, 20, '任务管理', '#!jobinfo', 'http://127.0.0.1:8888/jobinfo', 'layui-icon-senior', 1, '2018-08-28 17:02:00', '2018-08-28 18:24:23', 1, 0);
INSERT INTO `sys_menu` VALUES (22, 20, '调度日志', '#!joblog', 'http://127.0.0.1:8888/joblog', 'layui-icon-senior', 2, '2018-08-28 18:20:53', '2018-08-28 18:24:32', 1, 0);
INSERT INTO `sys_menu` VALUES (23, 20, '执行器管理', '#!jobgroup', 'http://127.0.0.1:8888/jobgroup', 'layui-icon-senior', 3, '2018-08-28 18:22:04', '2018-09-03 08:05:02', 1, 0);
INSERT INTO `sys_menu` VALUES (24, -1, '用户地图', '#!map', 'baiduMap/userMap.html', 'layui-icon-engine', 111111, '2019-06-14 21:28:54', '2019-06-14 21:28:54', 1, 0);
INSERT INTO `sys_menu` VALUES (25, -1, '路由管理', '#!route', 'route/route.html', 'layui-icon-engine', 111111, '2019-06-14 21:28:54', '2019-06-14 21:28:54', 1, 0);
INSERT INTO `sys_menu` VALUES (110, -1, '订单管理', 'javascript:;', '', 'layui-icon-set', 1, '2020-12-25 08:17:28', '2020-12-25 08:17:28', 2, 0);
INSERT INTO `sys_menu` VALUES (111, 110, '订单信息', '#!order', 'order/order.html', 'layui-icon-menu-fill', 1, '2020-12-25 16:44:42', '2020-12-25 16:44:42', 1, 0);
INSERT INTO `sys_menu` VALUES (114, 1, '字典管理', '#!sysdict', 'system/sysdict.html', 'layui-icon-menu-fill', 1, '2019-03-01 00:00:00', '2020-12-25 11:56:13', 1, 0);
INSERT INTO `sys_menu` VALUES (115, 1, '字典数据表', '#!sysdictdata', 'system/sysdictdata.html', 'layui-icon-menu-fill', 1, '2019-03-01 00:00:00', '2020-12-25 12:16:17', 1, 1);
INSERT INTO `sys_menu` VALUES (116, 110, '常用地址', '#!address', 'order/address.html', 'layui-icon-menu-fill', 1, '2021-01-06 21:17:09', '2021-01-06 21:17:09', 1, 0);

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `org_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织代码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织名称',
  `path_name` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '全路径',
  `level` int(4) NULL DEFAULT NULL COMMENT '节点级别',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织类型',
  `parent_id` int(10) NOT NULL COMMENT '父节点流水号',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点图标',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by_id` int(10) NOT NULL COMMENT '创建人ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key1`(`parent_id` ASC) USING BTREE,
  INDEX `sysorg_orgcode_index`(`org_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '组织机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES (1, '1', '宜州监狱', '宜州监狱', 0, '', -1, '无', '2019-03-20 10:08:16', 16, 'admin', '');
INSERT INTO `sys_org` VALUES (12, '1.12', '一分监区', '宜州监狱/第一分监区', 1, '', 1, NULL, '2019-03-20 17:44:53', 16, 'admin', '');
INSERT INTO `sys_org` VALUES (13, '1.12.13', '缝盘中队', '宜州监狱/第一分监区/缝盘组', 2, '', 12, NULL, '2019-03-20 17:49:23', 16, 'admin', '');
INSERT INTO `sys_org` VALUES (14, '1.14', '二分监区', '宜州监狱/第二分监区', 1, '', 1, NULL, '2019-03-26 15:30:40', 16, 'admin', '');
INSERT INTO `sys_org` VALUES (15, '1.12.15', '倒毛中队', '宜州监狱/第一分监区/倒毛组', 2, '', 12, NULL, '2019-03-26 15:31:32', 16, 'admin', '');
INSERT INTO `sys_org` VALUES (16, '1.14.16', '倒毛组', '宜州监狱/第二分监区/倒毛组', 2, NULL, 14, NULL, '2019-06-08 14:26:12', 1, 'admin', NULL);
INSERT INTO `sys_org` VALUES (17, '1.12.17', '织机中队', '宜州监狱/第一分监区/织机组', 2, NULL, 12, NULL, '2019-06-08 14:26:53', 1, 'admin', NULL);
INSERT INTO `sys_org` VALUES (18, '1.12.13.18', '第一缝盘组', NULL, 3, NULL, 13, NULL, '2019-07-10 16:44:11', 1, '管理员', NULL);

-- ----------------------------
-- Table structure for sys_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_param`;
CREATE TABLE `sys_param`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数名称',
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数键',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数值',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属分类',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ukey1`(`param_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '键值参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_param
-- ----------------------------
INSERT INTO `sys_param` VALUES (2, '111', '111', '111', NULL, '2019-06-09 16:23:40', '333333');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `permission` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `permission`(`permission` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1342385656983298058 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, NULL, 'permission:post/permissions', '保存权限标识', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (2, NULL, 'permission:put/permissions', '修改权限标识', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (3, NULL, 'permission:delete/permissions/{id}', '删除权限标识', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (4, NULL, 'permission:get/permissions', '查询权限标识', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (5, NULL, '/permissions/{roleId}/permissions', '查看角色权限', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (6, NULL, '/permissions/granted', '角色分配权限', '2018-01-18 17:06:39', '2018-01-18 17:06:39');
INSERT INTO `sys_permission` VALUES (7, NULL, 'role:post/roles', '添加角色', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (8, NULL, 'role:put/roles', '修改角色', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (9, NULL, 'role:delete/roles/{id}', '删除角色', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (10, NULL, 'role:post/roles/{id}/permissions', '给角色分配权限', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (11, NULL, 'role:get/roles', '查询角色', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (12, NULL, 'role:get/roles/{id}/permissions', '获取角色的权限', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (13, NULL, 'user:post/users/{id}/roles', '给用户分配角色', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (14, NULL, 'user:post/users/{id}/resetPassword', '用户重置密码', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (15, NULL, 'user:get/users', '用户查询', '2018-01-18 17:12:00', '2018-01-18 17:12:05');
INSERT INTO `sys_permission` VALUES (16, NULL, 'user:put/users/me', '修改用户', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (17, NULL, 'user:get/users/{id}/roles', '获取用户的角色', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (18, NULL, 'user:post/users/saveOrUpdate', '新增用户', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (19, NULL, 'user:post/users/exportUser', '导出用户', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (20, NULL, 'user:get/users/updateEnabled', '用户状态修改', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (21, NULL, 'user:put/users/password', '修改密码', '2018-01-18 17:06:39', '2018-01-18 17:06:39');
INSERT INTO `sys_permission` VALUES (22, NULL, 'menu:get/menus/all', '查询菜单', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (23, NULL, 'menu:post/menus/granted', '给角色分配菜单', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (24, NULL, 'menu:get/menus/tree', '树形显示', '2018-01-18 17:06:39', '2018-01-18 17:06:39');
INSERT INTO `sys_permission` VALUES (25, NULL, 'menu:get/menus/{roleId}/menus', '获取角色的菜单', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (26, NULL, 'menu:post/menus', '添加菜单', '2018-01-18 17:06:39', '2018-09-04 07:35:29');
INSERT INTO `sys_permission` VALUES (27, NULL, 'menu:put/menus', '修改菜单', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (28, NULL, 'menu:delete/menus/{id}', '删除菜单', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (29, NULL, 'menu:get/menus/current', '当前用户菜单', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (30, NULL, 'menu:get/menus/findAlls', '所有菜单', '2018-01-18 17:06:39', '2018-01-18 17:06:39');
INSERT INTO `sys_permission` VALUES (31, NULL, 'client:post/clients', '保存应用', '2018-01-18 17:06:39', '2018-01-18 17:06:39');
INSERT INTO `sys_permission` VALUES (32, NULL, 'client:get/clients', '应用列表', '2018-01-18 17:06:39', '2018-01-18 17:06:39');
INSERT INTO `sys_permission` VALUES (33, NULL, 'client:get/clients/{id}', '根据id获取应用', '2018-01-18 17:06:39', '2018-01-18 17:06:39');
INSERT INTO `sys_permission` VALUES (34, NULL, 'client:delete/clients', '删除应用', '2018-01-18 17:06:39', '2018-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES (35, NULL, 'service:get/service/findAlls', '查询所有服务', '2018-01-18 17:06:39', '2018-09-03 08:05:09');
INSERT INTO `sys_permission` VALUES (36, NULL, 'service:get/service/findOnes', '服务树', '2018-01-18 17:06:39', '2018-09-08 03:23:28');
INSERT INTO `sys_permission` VALUES (37, NULL, 'menu:get/menus/findOnes', '获取菜单以及顶层菜单', '2019-05-09 23:48:13', '2019-05-09 23:48:13');
INSERT INTO `sys_permission` VALUES (38, NULL, 'permission:get/permissions/{roleId}/permissions', '根据roleId获取权限', '2019-05-10 00:02:23', '2019-05-10 00:02:23');
INSERT INTO `sys_permission` VALUES (39, NULL, 'file:query', '获取文件列表', '2019-05-17 21:34:05', '2019-05-17 21:34:08');
INSERT INTO `sys_permission` VALUES (40, NULL, 'file:del', '删除文件', '2019-05-17 21:36:46', '2019-05-17 21:36:48');
INSERT INTO `sys_permission` VALUES (41, NULL, 'order#', '订单管理', '2020-12-25 08:24:16', '2020-12-25 08:24:16');
INSERT INTO `sys_permission` VALUES (1342385656983298050, 41, 'order', '订单信息', '2020-12-25 16:44:42', '2020-12-25 16:44:42');
INSERT INTO `sys_permission` VALUES (1342385656983298051, 1342385656983298050, 'order:order:list', '查询订单基本信息', '2020-12-25 16:44:42', '2020-12-25 16:44:42');
INSERT INTO `sys_permission` VALUES (1342385656983298052, 1342385656983298050, 'order:order:saveOrUpdate', '添加或修改订单基本信息', '2020-12-25 16:44:42', '2020-12-25 16:44:42');
INSERT INTO `sys_permission` VALUES (1342385656983298053, 1342385656983298050, 'order:order:delete', '删除订单基本信息', '2020-12-25 16:44:42', '2020-12-25 16:44:42');
INSERT INTO `sys_permission` VALUES (1342385656983298054, 179, 'address', '常用地址', '2021-01-06 21:17:09', '2021-01-06 21:17:09');
INSERT INTO `sys_permission` VALUES (1342385656983298055, 1342385656983298054, 'order:address:list', '查询常用地址', '2021-01-06 21:17:09', '2021-01-06 21:17:09');
INSERT INTO `sys_permission` VALUES (1342385656983298056, 1342385656983298054, 'order:address:saveOrUpdate', '添加或修改常用地址', '2021-01-06 21:17:09', '2021-01-06 21:17:09');
INSERT INTO `sys_permission` VALUES (1342385656983298057, 1342385656983298054, 'order:address:delete', '删除常用地址', '2021-01-06 21:17:09', '2021-01-06 21:17:09');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色code',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'ADMIN', '管理员', '2017-11-17 16:56:59', '2017-11-17 16:56:59');
INSERT INTO `sys_role` VALUES (3, '002', '普通用户', '2019-03-27 02:52:00', '2019-03-27 02:52:00');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_id`(`role_id` ASC, `menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (53, 1, 1);
INSERT INTO `sys_role_menu` VALUES (54, 1, 2);
INSERT INTO `sys_role_menu` VALUES (55, 1, 3);
INSERT INTO `sys_role_menu` VALUES (56, 1, 4);
INSERT INTO `sys_role_menu` VALUES (57, 1, 5);
INSERT INTO `sys_role_menu` VALUES (58, 1, 6);
INSERT INTO `sys_role_menu` VALUES (59, 1, 7);
INSERT INTO `sys_role_menu` VALUES (60, 1, 8);
INSERT INTO `sys_role_menu` VALUES (61, 1, 9);
INSERT INTO `sys_role_menu` VALUES (62, 1, 10);
INSERT INTO `sys_role_menu` VALUES (63, 1, 11);
INSERT INTO `sys_role_menu` VALUES (64, 1, 12);
INSERT INTO `sys_role_menu` VALUES (65, 1, 13);
INSERT INTO `sys_role_menu` VALUES (66, 1, 14);
INSERT INTO `sys_role_menu` VALUES (67, 1, 15);
INSERT INTO `sys_role_menu` VALUES (68, 1, 16);
INSERT INTO `sys_role_menu` VALUES (69, 1, 17);
INSERT INTO `sys_role_menu` VALUES (70, 1, 18);
INSERT INTO `sys_role_menu` VALUES (71, 1, 19);
INSERT INTO `sys_role_menu` VALUES (72, 1, 20);
INSERT INTO `sys_role_menu` VALUES (73, 1, 21);
INSERT INTO `sys_role_menu` VALUES (74, 1, 22);
INSERT INTO `sys_role_menu` VALUES (75, 1, 23);
INSERT INTO `sys_role_menu` VALUES (76, 1, 24);
INSERT INTO `sys_role_menu` VALUES (77, 1, 25);
INSERT INTO `sys_role_menu` VALUES (78, 1, 110);
INSERT INTO `sys_role_menu` VALUES (79, 1, 111);
INSERT INTO `sys_role_menu` VALUES (80, 1, 114);
INSERT INTO `sys_role_menu` VALUES (81, 1, 115);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_id`(`role_id` ASC, `permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1);
INSERT INTO `sys_role_permission` VALUES (2, 1, 2);
INSERT INTO `sys_role_permission` VALUES (3, 1, 3);
INSERT INTO `sys_role_permission` VALUES (4, 1, 4);
INSERT INTO `sys_role_permission` VALUES (5, 1, 5);
INSERT INTO `sys_role_permission` VALUES (6, 1, 6);
INSERT INTO `sys_role_permission` VALUES (7, 1, 7);
INSERT INTO `sys_role_permission` VALUES (8, 1, 8);
INSERT INTO `sys_role_permission` VALUES (9, 1, 9);
INSERT INTO `sys_role_permission` VALUES (10, 1, 10);
INSERT INTO `sys_role_permission` VALUES (11, 1, 11);
INSERT INTO `sys_role_permission` VALUES (12, 1, 12);
INSERT INTO `sys_role_permission` VALUES (13, 1, 13);
INSERT INTO `sys_role_permission` VALUES (14, 1, 14);
INSERT INTO `sys_role_permission` VALUES (15, 1, 15);
INSERT INTO `sys_role_permission` VALUES (16, 1, 16);
INSERT INTO `sys_role_permission` VALUES (17, 1, 17);
INSERT INTO `sys_role_permission` VALUES (18, 1, 18);
INSERT INTO `sys_role_permission` VALUES (19, 1, 19);
INSERT INTO `sys_role_permission` VALUES (20, 1, 20);
INSERT INTO `sys_role_permission` VALUES (21, 1, 21);
INSERT INTO `sys_role_permission` VALUES (22, 1, 22);
INSERT INTO `sys_role_permission` VALUES (23, 1, 23);
INSERT INTO `sys_role_permission` VALUES (24, 1, 24);
INSERT INTO `sys_role_permission` VALUES (25, 1, 25);
INSERT INTO `sys_role_permission` VALUES (26, 1, 26);
INSERT INTO `sys_role_permission` VALUES (27, 1, 27);
INSERT INTO `sys_role_permission` VALUES (28, 1, 29);
INSERT INTO `sys_role_permission` VALUES (29, 1, 30);
INSERT INTO `sys_role_permission` VALUES (30, 1, 31);
INSERT INTO `sys_role_permission` VALUES (31, 1, 32);
INSERT INTO `sys_role_permission` VALUES (32, 1, 33);
INSERT INTO `sys_role_permission` VALUES (33, 1, 34);
INSERT INTO `sys_role_permission` VALUES (34, 1, 35);
INSERT INTO `sys_role_permission` VALUES (35, 1, 36);
INSERT INTO `sys_role_permission` VALUES (36, 1, 37);
INSERT INTO `sys_role_permission` VALUES (37, 1, 38);
INSERT INTO `sys_role_permission` VALUES (38, 1, 39);

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES (1, 1, 1);
INSERT INTO `sys_role_user` VALUES (4, 1275397643669949952, 1);
INSERT INTO `sys_role_user` VALUES (3, 1277056689447719936, 1);
INSERT INTO `sys_role_user` VALUES (2, 1277120261867529984, 1);
INSERT INTO `sys_role_user` VALUES (12, 1348888433375936513, 3);
INSERT INTO `sys_role_user` VALUES (13, 1351196955153305602, 3);
INSERT INTO `sys_role_user` VALUES (14, 1351197070001737730, 3);
INSERT INTO `sys_role_user` VALUES (15, 1351201057098731521, 3);

-- ----------------------------
-- Table structure for sys_sequence
-- ----------------------------
DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sys_sequence`  (
  `seq_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `current_val` int(11) NOT NULL,
  `increment_val` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`seq_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_sequence
-- ----------------------------
INSERT INTO `sys_sequence` VALUES ('seq_tab_order', '', 246, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_img_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` tinyint(1) NULL DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `car_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `car_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `car_color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '颜色',
  `balance` decimal(10, 2) NULL DEFAULT 0.00,
  `carbon_currency` decimal(10, 2) NULL DEFAULT 0.00,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `level` int(4) NULL DEFAULT NULL COMMENT '等级',
  `union_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_num` int(11) NULL DEFAULT 0 COMMENT '接单数量',
  `mileage` decimal(20, 2) NULL DEFAULT 0.00,
  `greenness` decimal(20, 2) NULL DEFAULT 100.00,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$sqb3TBzYy4v/4w0Gm6LSVeqLB8RC1VAjsu3NorFSBGLnJIlOBC6Wa', '管理员', 'https://www.yhofoodie.net/image/laosiji.jpg', '13106975707', 1, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2017-11-17 16:56:59', '2020-12-24 00:42:19', NULL, NULL, 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (5, 's18007710009', '$2a$10$ghLiEgy8wE13uTv7Pu0v7ushcaMiI9E5S/MJi39Hufp0JGbJHPXU.', '陈师傅', 'https://www.yhofoodie.net/image/laosiji.jpg', '18007710009', 0, 1, 'BACKEND', '粤B99999', '劳斯莱斯', '黑色', 0.00, 0.00, '2021-01-12 07:03:58', '2021-01-12 07:03:58', NULL, NULL, 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (11, 's13481137304', '$2a$10$dnl2Qfq1yUj7.EUIkTG8k.JJrah8hXj0SMzOtHHcqj925megGGZMK', '陆师傅', 'https://www.yhofoodie.net/image/laosiji.jpg', '13481137304', 0, 1, 'BACKEND', '粤B55555', '兰搏基尼', '黄色', 0.00, 0.00, '2021-01-18 15:57:13', '2021-01-18 15:57:13', NULL, NULL, 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (12, 's18577833579', '$2a$10$ejFB7sh2TmWK6N/4QvM83uWwV3PJ2VLBYbGErkRgXQYUDBGLjW6Da', '龚师傅', 'https://www.yhofoodie.net/image/laosiji.jpg', '18577833579', 0, 1, 'BACKEND', '粤B66666', '桑塔纳', '蓝色', 0.00, 0.00, '2021-01-18 15:57:40', '2021-01-18 15:57:40', NULL, NULL, 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (13, 's18721439751', '$2a$10$q9m9IMwDGd1TMuUTMf.d7e3o..fO6yNkSmp6W8OpaE4Hj9U2AyceO', '马师傅', 'https://www.yhofoodie.net/image/laosiji.jpg', '18721439751', 0, 1, 'BACKEND', '粤B88888', '奥拓', '银色', 0.00, 0.00, '2021-01-18 16:13:31', '2021-01-18 16:13:31', NULL, NULL, 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (20, 'oV7Lu4kNucdYIbqxxpVmk1QhYky4', '$2a$10$5Xvzw884mukSBFq.f5tUv.Ur57aGMygkEaiw3v0aMQW6AOgHTfD2S', 'oV7Lu4kNucdYIbqxxpVmk1QhYky4', NULL, '18607712940', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 55.00, '2021-02-03 18:35:18', '2021-02-03 18:35:39', 0, 'null', 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (21, 'oV7Lu4tCnYi1w2ElmOOtqb8U6zSs', '$2a$10$16qk/4/0dCBZORc3X8jR7Oug9ORqlxSJTwGPyW8Qn7axoZO54P8rq', 'oV7Lu4tCnYi1w2ElmOOtqb8U6zSs', NULL, '18721439751', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 38.07, '2021-02-03 23:53:34', '2021-02-04 01:02:40', 0, 'null', 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (22, 'oV7Lu4qcPNIqdtCYQuiW62C0nWTI', '$2a$10$KzHBgmmO/ZU7LSQLCahU2u/gWCuVyP.mwEErYf20B7o5dRWxR1bUa', 'oV7Lu4qcPNIqdtCYQuiW62C0nWTI', NULL, '18007710009', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 0.79, '2021-02-04 10:45:24', '2021-02-04 10:45:32', 0, 'null', 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (23, 'oV7Lu4hkcOpvMKp2dNAaIYw67axI', '$2a$10$.8iOmdw21UULT0tALx9WZ.VE80Qht1ctj.4GnpviUewgYMFBYwYUu', 'oV7Lu4hkcOpvMKp2dNAaIYw67axI', NULL, '13481137304', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 1.71, '2021-02-04 14:49:39', '2021-02-04 15:07:17', 0, 'null', 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (24, 'oV7Lu4p9pXIJI-DKkwNTywWcwDjk', '$2a$10$UVdnCY12ODDuhEkf0m01vOOBZJ4oQeEHV.RxEkTcYPcTHIDHhc5MK', 'oV7Lu4p9pXIJI-DKkwNTywWcwDjk', NULL, '18607718485', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 0.00, '2021-02-04 15:43:43', '2021-02-04 15:45:16', 0, 'null', 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (25, 'oV7Lu4g2GTOBcXB5Hzt4KkCLMQNQ', '$2a$10$agkz6CFhu/5wrSK2g4P8Su7mdZ7oE5zDiRdkMD6qiFHg5NIe9zKJa', 'oV7Lu4g2GTOBcXB5Hzt4KkCLMQNQ', NULL, '18577833579', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 0.00, '2021-02-05 07:50:12', '2021-02-05 07:50:17', 0, 'null', 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (26, 'oV7Lu4nSu9YICEV7fgjAmU9_yf1E', '$2a$10$HTiNyNNIXUajhmfDz5w4g.Q2oPZNef.TTWhZgV5XJQ/zGqv/Xxoh2', 'oV7Lu4nSu9YICEV7fgjAmU9_yf1E', NULL, '13682386699', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 0.02, '2021-02-06 07:46:24', '2021-02-06 07:46:53', 0, 'null', 0, NULL, 100.00);
INSERT INTO `sys_user` VALUES (27, 'oV7Lu4iUuwi4duqCwKR-4ZnWuq_M', '$2a$10$9YrHjdImc8Iwg4916X3oMO3eZkHcijJX/Keo59dm5jNrQRe5iPVMy', 'oV7Lu4iUuwi4duqCwKR-4ZnWuq_M', NULL, NULL, NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 0.00, '2021-02-20 02:23:55', '2021-02-20 02:23:55', 0, 'null', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (28, '18516583073', '$2a$10$Abn6EOhAJXrpTzYR9jwfeeEcYT754FjdnxgI1N7IMT7DgAxiPeGsG', '18516583073', NULL, '18516583073', NULL, 1, 'APP', NULL, NULL, NULL, 100.00, 0.00, '2021-09-26 06:42:42', '2021-09-26 06:42:42', 0, '', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (29, 'olk4z6uENnyaTE_TvY5bl2sUa2Vo', '$2a$10$pU1IOgdh67KDP4501tb9sOHMSTyvulNK0kzAKj/KQlMnsKT90SOfO', '超级生', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIEvg5DUWPZxdiawMC7BUu59shSlwA1H5WqLmaPbytzGnGbrjoINajsYqaLXTPCzfbmQEaCuWTJiaiaw/132', NULL, NULL, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2022-02-10 11:04:28', '2022-02-10 11:04:28', 0, 'oVetU69V40dkCBVssFOKcGGSNAYI', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (30, 'olk4z6qvKffBWfQnXePSGNAjSE0k', '$2a$10$6RqAoVOnMXajrJcbOfdSFuNn2yxp/Rb3mwdsLrErlJcG1MSnX8jGi', 'Achilles Kung', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKOcoibZyj1ia14zVJqcicia8VibGVRysLJF1loeIxMPGSBaDO6wjVriceGbZTNiaHghanXu90P8ibUUGvnyw/132', NULL, NULL, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2022-02-07 12:31:36', '2022-02-07 12:31:36', 0, 'oVetU61IopfyS2WA7WTtJzSiX14k', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (31, '18620907791', '$2a$10$r8JTtwBQLzSIm9NxUqKEKeCPfRM1xUTHpfwJI6pcDhxan4yeJrvXy', '18620907791', NULL, '18620907791', NULL, 1, 'APP', NULL, NULL, NULL, 0.00, 0.00, '2021-09-26 10:30:19', '2021-09-26 10:30:19', 0, '', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (32, 'olk4z6oDFbgojt5FniX4Ng06_BpE', '$2a$10$YyfPCnlqT6B9QDSWLODll.wSTWQ0jMpCUBpEr18n10eMZ12ul5yiK', 'yxy', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKE8GJ4haMwlfUtXQaicD5ZMy2WUIKy0UmtV61cRDEAqHukargV3phRlIibnwus6qw9ZBxy8HwticXFA/132', NULL, NULL, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2021-09-27 05:19:59', '2021-09-27 05:19:59', 0, 'oVetU6wGjwSqUmY5SGzXScTFkWA8', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (33, 'oV7Lu4lRMt1ORMumlhbBfmtAdD14', '$2a$10$QtfvJB/4e3pAWBoHpDjSruvDp7W4TsRyuBZMg94.6JVZBGsef9.8a', 'oV7Lu4lRMt1ORMumlhbBfmtAdD14', NULL, NULL, NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 0.00, '2021-09-28 14:55:50', '2021-09-28 14:55:50', 0, 'null', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (34, 'oV7Lu4ni4qbq7bwlzC_dc62xPZ0k', '$2a$10$Og9z5wMbKoXQe0QQg4ru0eJDNqYYXMt7jSdpBJkgTALDCO18kUKOq', 'oV7Lu4ni4qbq7bwlzC_dc62xPZ0k', NULL, '18516583073', NULL, 1, 'WXUSER', NULL, NULL, NULL, 0.00, 0.00, '2021-09-28 15:06:53', '2021-09-28 15:06:59', 0, 'null', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (35, 'olk4z6uCBlQKz1dVtEJqgpXC2Ymo', '$2a$10$hKs0aNf9wzPdsFJSfikYce5EkwG4IGQx/m99JqXgcwfBceadtkZjG', '夏天summer', 'https://thirdwx.qlogo.cn/mmopen/vi_32/O1Gx6YFajwnAHaCdFR4PIHO2hz5dB4kpS5nLiaqfwnuwMic28TNeUhecGGAEtd4gEwiaZyIPpHA1tBDX2lF9U25aA/132', NULL, NULL, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2021-09-29 07:35:14', '2021-09-29 07:35:14', 0, 'oVetU65wFgcQPkVSJClZFnUsX7_E', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (36, '13918574009', '$2a$10$xSVrY2NnYfM07QAoWTU.Lu/MgHRXDGMUUH86PfQotpkKhp./.XL0m', '13918574009', NULL, '13918574009', NULL, 1, 'APP', NULL, NULL, NULL, 0.00, 0.00, '2021-10-06 15:31:06', '2021-10-06 15:31:06', 0, '', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (37, 'olk4z6haBivsOmGiURNnyU0PaX2U', '$2a$10$OAyVn.Ef5tfggGR6Gkvm2OP.to5kQAgEM6x2imwiI4LHzd38IMoSW', '月朗星稀', 'https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eptVc57aLqTfepNqMJ2oibhhHH6QKj6gw5ibcodqGhNk5GEuVN0qZ7whWdyjhxDzzK09DXk4hia05v5w/132', NULL, NULL, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2021-10-07 02:31:29', '2021-10-07 02:31:29', 0, 'oVetU620DwM9m7J1Jk8gfp_ZW6rQ', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (38, '18801776316', '$2a$10$z/iEMJRx4fleVxbqHBE.u.ennfAiYU7hIwq1jgFDqStGS8kwlWT92', '18801776316', NULL, '18801776316', NULL, 1, 'APP', NULL, NULL, NULL, 0.00, 0.00, '2021-11-17 12:15:25', '2021-11-17 12:15:25', 0, '', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (39, 'olk4z6uoft-RwY3iW8evXtpoExCo', '$2a$10$FfZEqKcEFral4RfB0R2Lw.gUWVrjxb.o8q0E95Z5g.rj9KwD.seea', '杨浩贤 Dennis', 'https://thirdwx.qlogo.cn/mmopen/vi_32/5nP35lE7wAFcHeO26eKBgWaP5ia2d1ibB5R0hvwBtIaXQWAA1s7CLdm7OcVibeF0Jnkn3l3jC8OQLv4jcSZjxe3AQ/132', NULL, NULL, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2022-01-14 04:55:25', '2022-01-14 04:55:25', 0, 'oVetU6zhvvK8Uj3l9CBg73Ga22Zc', 0, 0.00, 100.00);
INSERT INTO `sys_user` VALUES (40, 'olk4z6gwYeTY_Q_NRrVdW8-tcrbM', '$2a$10$5i8vArP7MZP3XoVhbeQb3.EmWohWGhVVW3vh5e/kXja4E8dDZeYXK', '李军', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJhO3J5v7icT6nYsSr51NX0gyz2Qm5yNnAeSIjnuof7VqAMrqxWTGMYkMffWGXUnFAXEVpaD0tCZvg/132', NULL, NULL, 1, 'BACKEND', NULL, NULL, NULL, 0.00, 0.00, '2022-01-20 15:58:16', '2022-01-20 15:58:16', 0, 'oVetU62VUs5FNzgSe5AU8j88bYU0', 0, 0.00, 100.00);

-- ----------------------------
-- Table structure for sys_user_extend
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_extend`;
CREATE TABLE `sys_user_extend`  (
  `id` int(11) NOT NULL COMMENT '用户ID',
  `gaode_tid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '高德地图对应的tid',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户扩展信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_extend
-- ----------------------------
INSERT INTO `sys_user_extend` VALUES (28, '474490493');
INSERT INTO `sys_user_extend` VALUES (29, '433080742');
INSERT INTO `sys_user_extend` VALUES (37, '433040214');
INSERT INTO `sys_user_extend` VALUES (39, '359274186');

-- ----------------------------
-- Table structure for tab_address
-- ----------------------------
DROP TABLE IF EXISTS `tab_address`;
CREATE TABLE `tab_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标识',
  `longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '纬度',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '常用地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_address
-- ----------------------------
INSERT INTO `tab_address` VALUES (27, 27, 'company', '121.645901', '31.025252', '新场古镇洪桥景区');
INSERT INTO `tab_address` VALUES (28, 20, 'home', '108.31659', '22.797879', '六一公寓平西店');

-- ----------------------------
-- Table structure for tab_carbon_detail
-- ----------------------------
DROP TABLE IF EXISTS `tab_carbon_detail`;
CREATE TABLE `tab_carbon_detail`  (
  `id` bigint(21) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(21) NULL DEFAULT NULL COMMENT '用户ID',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_id` bigint(21) NULL DEFAULT NULL COMMENT '订单ID',
  `carbon` decimal(20, 8) NULL DEFAULT NULL COMMENT '碳币',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `state` int(4) NULL DEFAULT 1 COMMENT '状态',
  `send_time` datetime NULL DEFAULT NULL,
  `send_count` int(4) NULL DEFAULT 0,
  `from_date` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `thru_date` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_carbon_detail
-- ----------------------------

-- ----------------------------
-- Table structure for tab_cycle
-- ----------------------------
DROP TABLE IF EXISTS `tab_cycle`;
CREATE TABLE `tab_cycle`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `tid` int(20) NULL DEFAULT NULL COMMENT '终端',
  `sid` int(20) NULL DEFAULT NULL COMMENT '服务ID',
  `trid` int(11) NULL DEFAULT NULL,
  `city_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `trname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '轨迹名称',
  `cust_name` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `cust_phone` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户电话',
  `start_place` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出发地',
  `start_longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '起点经度',
  `start_latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '终点纬度',
  `end_place` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '终点',
  `end_longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '终点经度',
  `end_latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '终点纬度',
  `create_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `mileage` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '里程',
  `carbon` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '碳币',
  `state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '记录状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '骑行1，步行2，驾车3，公交4，地铁5',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '骑行记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_cycle
-- ----------------------------
INSERT INTO `tab_cycle` VALUES (1, 29, 433080742, 286676, 6680, NULL, '', 'olk4z6uENnyaTE_TvY5bl2sUa2Vo', NULL, '万科魅力之城', '108.393392', '22.719664', '请选择终点', NULL, NULL, '2021-10-07 01:37:30', NULL, 0.00, 0.00, '已取消', NULL, '1');
INSERT INTO `tab_cycle` VALUES (2, 29, 433080742, 286676, 6700, NULL, '', 'olk4z6uENnyaTE_TvY5bl2sUa2Vo', NULL, '东华门大街小区', '116.405050', '39.914666', '菜鸟驿站', '108.391769', '22.720571', '2021-10-07 01:37:45', NULL, 0.00, 0.00, '行程开始', NULL, '3');
INSERT INTO `tab_cycle` VALUES (3, 37, 433040214, 286676, 6720, NULL, '', 'olk4z6haBivsOmGiURNnyU0PaX2U', NULL, '吉宝·汇锦城3期', '121.638859', '31.032503', '新环北路与新环西路交叉口', '121.637672', '31.031280', '2021-10-07 02:18:54', NULL, 0.00, 0.00, '已取消', NULL, '2');
INSERT INTO `tab_cycle` VALUES (4, 37, 433040214, 286676, 6740, NULL, '', 'olk4z6haBivsOmGiURNnyU0PaX2U', NULL, '吉宝·汇锦城1期', '121.636396', '31.032156', '肯德基(新环广场店)', '121.637543', '31.030535', '2021-10-07 02:24:17', NULL, 0.00, 0.00, '行程开始', NULL, '2');
INSERT INTO `tab_cycle` VALUES (5, 28, 474490493, 286676, 6760, NULL, '', '18516583073', '18516583073', '申晖小区', '121.468103', '31.197906', '环龙新苑', '121.469087', '31.195984', '2022-01-08 12:53:27', NULL, 0.00, 0.00, '行程开始', NULL, '1');
INSERT INTO `tab_cycle` VALUES (6, 39, 359274186, 286676, 6780, NULL, '', 'olk4z6uoft-RwY3iW8evXtpoExCo', NULL, '天誉半岛', '113.248144', '23.100340', '海天大厦', '113.247649', '23.102888', '2022-01-14 04:55:41', NULL, 0.00, 0.00, '行程开始', NULL, '1');

-- ----------------------------
-- Table structure for tab_driver
-- ----------------------------
DROP TABLE IF EXISTS `tab_driver`;
CREATE TABLE `tab_driver`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户名',
  `id_card` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `driver_license` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驾驶证',
  `diving_permit` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行驶证',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '住址',
  `car_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `drive_experience` int(4) NULL DEFAULT NULL COMMENT '驾龄',
  `order_num` int(8) NULL DEFAULT NULL COMMENT '接单量',
  `level` int(4) NULL DEFAULT NULL COMMENT '星级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户扩展信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_driver
-- ----------------------------

-- ----------------------------
-- Table structure for tab_order
-- ----------------------------
DROP TABLE IF EXISTS `tab_order`;
CREATE TABLE `tab_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `city_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '订单编号',
  `order_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单类型',
  `cust_name` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `cust_phone` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户电话',
  `start_place` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出发地',
  `start_longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '出发经度',
  `start_latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出发纬度',
  `end_place` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '目的地',
  `end_longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目的地经度',
  `end_latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目的地纬度',
  `driver_id` bigint(20) NULL DEFAULT NULL COMMENT '司机ID',
  `driver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '司机姓名',
  `driver_phone` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '司机号码',
  `car_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '叫车时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '接到乘客',
  `order_receive_time` datetime NULL DEFAULT NULL COMMENT '接单时间',
  `arrival_time` datetime NULL DEFAULT NULL COMMENT '司机到达',
  `arrivalend_time` datetime NULL DEFAULT NULL COMMENT '到达终点',
  `mileage` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '里程',
  `pay_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `start_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '起步价',
  `mileage_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '里程费',
  `duration_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '时长费',
  `other_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '其他费',
  `all_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '总费用',
  `coupon_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠券金额',
  `wx_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '微信金额',
  `carbon_money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '碳币金额',
  `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付交易号',
  `state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '订单状态',
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '取消原因',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `version` int(2) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 340 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单基本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tab_order
-- ----------------------------
INSERT INTO `tab_order` VALUES (223, 1352261906936676353, NULL, '20210121224033130', '出租车', NULL, NULL, '广西壮族自治区南宁市江南区亭洪路', '108.29801940917969', '22.789770126342773', '广西壮族自治区南宁市西乡塘区高科路和创新路的交叉口附近', '108.27872', '22.859331', 12, '18577833579', '18577833579', NULL, '2021-01-21 14:40:34', '2021-01-21 22:41:19', NULL, NULL, NULL, 12.42, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (224, 6, NULL, '20210121224230131', '出租车', NULL, '18078187592', '广西壮族自治区南宁市西乡塘区大化路6号', '108.26451958550348', '22.83626953125', '广西壮族自治区南宁市兴宁区明秀东路88号', '108.334408', '22.854623', NULL, NULL, NULL, NULL, '2021-01-21 14:42:30', NULL, NULL, NULL, NULL, 11.33, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (225, 8, NULL, '20210121224759132', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29801940917969', '22.789724349975586', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '18577833579', '18577833579', NULL, '2021-01-21 14:47:59', '2021-01-21 14:49:30', NULL, '2021-01-21 14:49:28', '2021-01-21 14:49:59', 5.67, '碳币支付', '2021-01-21 14:50:18', NULL, 0.01, NULL, 0.00, 0.01, 0.00, 0.00, 0.01, 'fa584e4d-6fc6-4f9e-a50e-26f7e9d6bfae', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (226, 2, NULL, '20210121224907133', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市良庆区飞云路6号', '108.382113', '22.76475', 5, '18007710009', '18007710009', NULL, '2021-01-21 14:49:08', '2021-01-21 22:49:33', NULL, NULL, NULL, 16.41, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (227, 2, NULL, '20210121225558134', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '18577833579', '18577833579', NULL, '2021-01-21 14:55:59', '2021-01-21 22:56:11', NULL, NULL, NULL, 22.21, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (228, 8, NULL, '20210121230845135', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29801177978516', '22.78971290588379', '广西壮族自治区南宁市青秀区青环路附近', '108.413182', '22.764634', 12, '龚师傅', '18577833579', NULL, '2021-01-21 15:08:45', '2021-01-21 15:10:20', NULL, '2021-01-21 15:10:19', '2021-01-21 15:10:21', 14.23, '碳币支付', '2021-01-21 15:10:35', NULL, 0.01, NULL, 0.00, 0.01, 0.00, 0.00, 0.01, 'b31774f8-1b25-45dd-96ea-c92074d72eef', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (229, 6, NULL, '20210121231630136', '出租车', NULL, '18078187592', '广西壮族自治区南宁市西乡塘区大化路6号', '108.26451958550348', '22.83626953125', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 5, '陈师傅', '18007710009', NULL, '2021-01-21 15:16:31', '2021-01-21 15:17:40', NULL, '2021-01-21 15:17:38', '2021-01-21 15:17:43', 8.40, '碳币支付', '2021-01-21 15:18:49', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, '60390e95-a795-4439-a5ad-d73d426c8fc7', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (230, 8, NULL, '20210122143625137', '出租车', NULL, '18577833579', '广西壮族自治区南宁市良庆区秋月路', '108.3810693359375', '22.747008192274304', '广西壮族自治区南宁市青秀区青环路附近', '108.413182', '22.764634', NULL, NULL, NULL, NULL, '2021-01-22 06:36:25', NULL, NULL, NULL, NULL, 8.20, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (231, 8, NULL, '20210122143638138', '出租车', NULL, '18577833579', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', '广西壮族自治区南宁市青秀区青环路附近', '108.413182', '22.764634', 12, '龚师傅', '18577833579', NULL, '2021-01-22 06:36:38', '2021-01-22 14:36:52', NULL, NULL, NULL, 12.06, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (232, 4, NULL, '20210122152106139', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '广东省深圳市福田区北环大道附近', '114.011241', '22.575703', NULL, NULL, NULL, NULL, '2021-01-22 07:21:06', NULL, NULL, NULL, NULL, 20.11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (233, 4, NULL, '20210122152820140', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '湖南省长沙市芙蓉区人民东路777号(合平村公交站附近)', '113.089968', '28.194111', NULL, NULL, NULL, NULL, '2021-01-22 07:28:20', NULL, NULL, NULL, NULL, 781.64, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (234, 4, NULL, '20210122152847141', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '广东省深圳市福田区红荔西路6028号附近', '114.060253', '22.550065', NULL, NULL, NULL, NULL, '2021-01-22 07:28:47', NULL, NULL, NULL, NULL, 6.89, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (235, 4, NULL, '20210122152936142', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '湖南省长沙市芙蓉区人民东路与望龙路交汇处西北角(红旗路口站附近)', '113.071314', '28.193133', NULL, NULL, NULL, NULL, '2021-01-22 07:29:37', NULL, NULL, NULL, NULL, 782.22, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (236, 4, NULL, '20210122153115143', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '湖南省长沙市芙蓉区人民东路与望龙路交汇处西北角(红旗路口站附近)', '113.071314', '28.193133', NULL, NULL, NULL, NULL, '2021-01-22 07:31:16', NULL, NULL, NULL, NULL, 782.22, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (237, 4, NULL, '20210122153126144', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '湖南省长沙市芙蓉区晚报大道226号附近', '113.017995', '28.208067', NULL, NULL, NULL, NULL, '2021-01-22 07:31:27', NULL, NULL, NULL, NULL, 781.68, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (238, 4, NULL, '20210122153221145', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '湖南省长沙市芙蓉区人民东路与望龙路交汇处西北角(红旗路口站附近)', '113.071314', '28.193133', NULL, NULL, NULL, NULL, '2021-01-22 07:32:22', NULL, NULL, NULL, NULL, 782.22, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (239, 4, NULL, '20210122154113146', '出租车', NULL, '18721439751', '广东省深圳市福田区蓝花道', '114.05607638888888', '22.50644232855903', '湖南省长沙市芙蓉区晚报大道226号附近', '113.017995', '28.208067', NULL, NULL, NULL, NULL, '2021-01-22 07:41:14', NULL, NULL, NULL, NULL, 781.66, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (240, 4, NULL, '20210122154948147', '出租车', NULL, '18721439751', '广东省深圳市福田区市花路', '114.05627658420138', '22.506405978732637', '广东省广州市荔湾区浣花西路82号附近', '113.225913', '23.084647', 5, '陈师傅', '18007710009', NULL, '2021-01-22 07:49:48', '2021-01-22 15:50:18', NULL, NULL, NULL, 137.55, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (241, 9, NULL, '20210122155820148', '出租车', NULL, '18620907791', '广东省深圳市福田区设计之都7号', '114.07496643066406', '22.54400062561035', '广东省深圳市福田区北环大道附近', '114.011241', '22.575703', 5, '陈师傅', '18007710009', NULL, '2021-01-22 07:58:20', '2021-01-22 08:14:29', NULL, '2021-01-22 08:14:23', '2021-01-22 08:14:35', 12.13, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (242, 9, NULL, '20210122164241149', '出租车', NULL, '18620907791', '广东省深圳市宝安区广深沿江高速公路', '113.86690521240234', '22.55347442626953', '广东省深圳市宝安区福凤路附近', '113.839902', '22.670802', 5, '陈师傅', '18007710009', NULL, '2021-01-22 08:42:41', '2021-01-22 08:49:13', NULL, '2021-01-22 08:48:35', '2021-01-22 08:49:48', 20.10, NULL, '2021-01-22 08:52:21', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.00, '4200000920202101229221011747', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (243, 8, NULL, '20210123110623150', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29803466796875', '22.7896785736084', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-23 03:06:23', '2021-01-23 03:07:04', NULL, '2021-01-23 03:06:59', '2021-01-23 03:07:06', 4.72, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (244, 2, NULL, '20210123232911151', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市良庆区飞云路6号', '108.382113', '22.76475', 5, '陈师傅', '18007710009', NULL, '2021-01-23 15:29:11', '2021-01-23 23:29:25', NULL, NULL, NULL, 16.41, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (245, 4, NULL, '20210124214547152', '出租车', NULL, '18721439751', '广东省深圳市福田区五号路', '114.03835259331598', '22.560857747395833', '广东省深圳市福田区上沙椰树村14巷2号附近', '114.029965', '22.525342', 13, '马师傅', '18721439751', NULL, '2021-01-24 13:45:48', '2021-01-24 21:46:13', NULL, NULL, NULL, 7.59, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (246, 4, NULL, '20210124222538153', '出租车', NULL, '18721439751', '广东省深圳市福田区五号路', '114.03835259331598', '22.560857747395833', '上海市浦东新区昌里路260号附近', '121.493863', '31.172572', 13, '马师傅', '18721439751', NULL, '2021-01-24 14:25:39', '2021-01-24 22:25:57', NULL, NULL, NULL, 1461.77, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (247, 4, NULL, '20210124222746154', '出租车', NULL, '18721439751', '广东省深圳市福田区五号路', '114.03835259331598', '22.560857747395833', '上海市浦东新区金谊路西海运调味品批发行附近', '121.52631', '31.125912', 13, '马师傅', '18721439751', NULL, '2021-01-24 14:27:47', '2021-01-24 22:27:50', NULL, NULL, NULL, 1459.24, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (248, 4, NULL, '20210124223136155', '出租车', NULL, '18721439751', '广东省深圳市福田区五号路', '114.03835259331598', '22.560857747395833', '上海市浦东新区新场大街345号附近', '121.645901', '31.025252', 13, '马师傅', '18721439751', NULL, '2021-01-24 14:31:36', '2021-01-24 14:34:22', NULL, '2021-01-24 14:33:34', '2021-01-24 14:37:46', 1477.77, NULL, '2021-01-24 14:44:50', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.00, '4200000921202101244567728312', '已完成', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (249, 4, NULL, '20210124224620156', '出租车', NULL, '18721439751', '广东省深圳市福田区五号路', '114.03835259331598', '22.560857747395833', '上海市浦东新区置业路243附近', '121.69213', '31.207282', 13, '马师傅', '18721439751', NULL, '2021-01-24 14:46:21', '2021-01-24 14:46:56', NULL, '2021-01-24 14:46:53', '2021-01-24 14:47:19', 1479.89, NULL, '2021-01-27 15:16:45', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.00, '4200000923202101274418407688', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (250, 3, NULL, '20210125002532157', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.33287048339844', '22.85135841369629', '轨道交通3号线,轨道交通4号线', '108.376302', '22.767602', NULL, NULL, NULL, NULL, '2021-01-24 16:25:32', NULL, NULL, NULL, NULL, 13.05, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (251, 3, NULL, '20210125003006158', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.33284759521484', '22.85135269165039', '轨道交通3号线,轨道交通4号线', '108.376302', '22.767602', NULL, NULL, NULL, NULL, '2021-01-24 16:30:07', NULL, NULL, NULL, NULL, 13.05, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (252, 3, NULL, '20210125003141159', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.3329086303711', '22.851409912109375', '广西壮族自治区南宁市青秀区青环路附近', '108.413182', '22.764634', NULL, NULL, NULL, NULL, '2021-01-24 16:31:42', NULL, NULL, NULL, NULL, 15.43, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (253, 3, NULL, '20210125003206160', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.33291625976562', '22.851394653320312', '广西壮族自治区南宁市西乡塘区邕隆路(石埠叉路批发部附近)', '108.189588', '22.82291', NULL, NULL, NULL, NULL, '2021-01-24 16:32:06', NULL, NULL, NULL, NULL, 19.83, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (254, 3, NULL, '20210125003308161', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.33290100097656', '22.85137367248535', '轨道交通3号线,轨道交通4号线', '108.376302', '22.767602', NULL, NULL, NULL, NULL, '2021-01-24 16:33:08', NULL, NULL, NULL, NULL, 13.05, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (255, 3, NULL, '20210125003334162', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.3329086303711', '22.851390838623047', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', NULL, NULL, NULL, NULL, '2021-01-24 16:33:34', NULL, NULL, NULL, NULL, 6.32, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (256, 2, NULL, '20210126193424163', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市良庆区飞云路6号', '108.382113', '22.76475', NULL, NULL, NULL, NULL, '2021-01-26 11:34:24', NULL, NULL, NULL, NULL, 16.41, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (257, 8, NULL, '20210126221522164', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29798014322917', '22.789575466579862', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-26 14:15:22', '2021-01-26 14:16:17', NULL, '2021-01-26 14:16:07', '2021-01-26 14:16:44', 4.71, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (258, 8, NULL, '20210127000001165', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29798014322917', '22.789575466579862', '广西壮族自治区南宁市武鸣区210国道附近', '108.297214', '23.039109', 12, '龚师傅', '18577833579', NULL, '2021-01-26 16:00:01', '2021-01-27 00:00:11', NULL, NULL, NULL, 31.84, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (259, 8, NULL, '20210127000234166', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29798014322917', '22.789575466579862', '广西壮族自治区南宁市西乡塘区高科路和创新路的交叉口附近', '108.27872', '22.859331', 12, '龚师傅', '18577833579', NULL, '2021-01-26 16:02:35', '2021-01-27 00:02:39', NULL, NULL, NULL, 12.25, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (260, 8, NULL, '20210127000455167', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29798014322917', '22.789575466579862', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-26 16:04:56', '2021-01-27 00:05:00', NULL, NULL, NULL, 4.71, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (261, 8, NULL, '20210127000611168', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29798014322917', '22.789575466579862', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-26 16:06:11', '2021-01-27 00:06:41', NULL, NULL, NULL, 4.71, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (262, 8, NULL, '20210127074843169', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29796006944444', '22.79017578125', '广西壮族自治区南宁市良庆区 ', '108.381121', '22.744206', 12, '龚师傅', '18577833579', NULL, '2021-01-26 23:48:43', '2021-01-26 23:49:46', NULL, '2021-01-26 23:49:39', '2021-01-27 00:20:06', 14.93, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (263, 3, NULL, '20210127083630170', '出租车', NULL, '18007710009', '广西壮族自治区南宁市青秀区金湖路57号', '108.36943054199219', '22.81468391418457', '广西壮族自治区南宁市青秀区朱槿路1-1号附近', '108.40643', '22.80713', NULL, NULL, NULL, NULL, '2021-01-27 00:36:30', NULL, NULL, NULL, NULL, 4.92, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (264, 2, NULL, '20210127104503171', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市良庆区飞云路6号', '108.382113', '22.76475', NULL, NULL, NULL, NULL, '2021-01-27 02:45:03', NULL, NULL, NULL, NULL, 16.41, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (265, 4, NULL, '20210127231754172', '出租车', NULL, '18721439751', '广东省深圳市福田区莲花路', '114.04521755642361', '22.553841959635417', '广东省深圳市福田区红树林片区福荣路以北(金域蓝湾公交站附近)', '114.025392', '22.523496', 13, '马师傅', '18721439751', NULL, '2021-01-27 15:17:55', '2021-01-29 00:27:42', NULL, NULL, NULL, 8.40, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (266, 2, NULL, '20210127233715173', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市良庆区飞云路6号', '108.382113', '22.76475', 5, '陈师傅', '18007710009', NULL, '2021-01-27 15:37:16', '2021-01-27 15:48:37', NULL, NULL, '2021-01-27 15:48:57', 16.40, '碳币支付', '2021-01-27 16:07:39', NULL, 0.01, NULL, 0.00, 0.01, 0.00, 0.00, 0.01, 'caa147a0-8ba7-4787-a573-1426c502b2dd', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (267, 8, NULL, '20210128101025174', '出租车', NULL, '18577833579', '广西壮族自治区南宁市良庆区秋月路', '108.3810693359375', '22.747008192274304', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-28 02:10:26', '2021-01-28 10:10:32', NULL, NULL, NULL, 11.94, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (268, 3, NULL, '20210128211026175', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区明秀东路', '108.33411407470703', '22.850990295410156', '广西壮族自治区南宁市西乡塘区高科路和创新路的交叉口附近', '108.27872', '22.859331', NULL, NULL, NULL, NULL, '2021-01-28 13:10:26', NULL, NULL, NULL, NULL, 9.80, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (269, 3, NULL, '20210128211511176', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区明秀东路', '108.33352661132812', '22.85076141357422', '广西壮族自治区南宁市邕宁区良堤路附近', '108.431676', '22.766741', NULL, NULL, NULL, NULL, '2021-01-28 13:15:11', NULL, NULL, NULL, NULL, 17.42, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (270, 8, NULL, '20210128214612177', '出租车', NULL, '18577833579', '广西壮族自治区南宁市良庆区秋月路', '108.3810693359375', '22.747008192274304', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-28 13:46:12', '2021-01-28 21:46:39', NULL, NULL, NULL, 11.94, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (271, 8, NULL, '20210128221823178', '出租车', NULL, '18577833579', '广西壮族自治区南宁市良庆区秋月路', '108.3810693359375', '22.747008192274304', '广西壮族自治区南宁市西乡塘区明秀东路157号(明秀小区附近150米)', '108.325328', '22.844436', 12, '龚师傅', '18577833579', NULL, '2021-01-28 14:18:23', '2021-01-28 22:19:44', NULL, '2021-01-28 14:19:47', NULL, 15.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (272, 8, NULL, '20210128222035179', '出租车', NULL, '18577833579', '广西壮族自治区南宁市良庆区秋月路', '108.3810693359375', '22.747008192274304', '广西壮族自治区南宁市青秀区青环路附近', '108.413182', '22.764634', 12, '龚师傅', '18577833579', NULL, '2021-01-28 14:20:36', '2021-01-28 14:21:29', NULL, '2021-01-28 14:21:26', '2021-01-28 14:21:31', 8.22, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (273, 4, NULL, '20210129002926180', '出租车', NULL, '18721439751', '广东省深圳市福田区回雁路18号', '114.04536187065972', '22.55673095703125', '广东省深圳市福田区北环大道附近', '114.011241', '22.575703', 13, '马师傅', '18721439751', NULL, '2021-01-28 16:29:26', '2021-01-28 16:34:12', NULL, '2021-01-28 16:33:53', '2021-01-28 16:34:31', 12.39, '碳币支付', '2021-01-28 16:35:43', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, '5a794a29-1164-4648-9372-4241fb86828b', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (274, 8, NULL, '20210129005021181', '出租车', NULL, '18577833579', '广西壮族自治区南宁市青秀区南湖隧道', '108.34969329833984', '22.808420181274414', '广西壮族自治区南宁市江南区 ', '108.299004', '22.789759', 12, '龚师傅', '18577833579', NULL, '2021-01-28 16:50:21', '2021-01-28 16:50:51', NULL, '2021-01-28 16:50:50', '2021-01-28 17:08:53', 7.24, '碳币支付', '2021-01-30 13:51:34', NULL, 0.01, NULL, 0.01, 0.02, 0.00, 0.00, 0.02, '55bbb2d9-c17c-4f68-bc9f-faca0dd6525d', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (275, 8, NULL, '20210130215140182', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29800211588541', '22.789483506944446', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-30 13:51:41', '2021-01-30 13:54:05', NULL, '2021-01-30 13:54:05', '2021-01-30 13:54:07', 5.49, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (276, 4, NULL, '20210131105633183', '出租车', NULL, '18721439751', '广东省深圳市福田区梅林路', '114.05745144314236', '22.568621148003473', NULL, '114.011241', '22.575703', 13, '马师傅', '18721439751', NULL, '2021-01-31 02:56:33', '2021-01-31 10:57:57', NULL, NULL, NULL, 10.92, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (277, 8, NULL, '20210131110313184', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.29800211588541', '22.789483506944446', '广西壮族自治区南宁市武鸣区210国道附近', '108.297214', '23.039109', 12, '龚师傅', '18577833579', NULL, '2021-01-31 03:03:13', '2021-01-31 11:03:29', NULL, NULL, NULL, 31.84, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (278, 4, NULL, '20210131121715185', '出租车', NULL, '18721439751', '广东省深圳市福田区八一路', '114.06023735894097', '22.571111924913193', NULL, '114.08828', '22.54957', NULL, NULL, NULL, NULL, '2021-01-31 04:17:15', NULL, NULL, NULL, NULL, 6.06, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (279, 4, NULL, '20210131122038186', '出租车', NULL, '18721439751', '广东省深圳市福田区八一路', '114.06023735894097', '22.571111924913193', '广东省深圳市福田区北环大道附近', '114.011241', '22.575703', NULL, NULL, NULL, NULL, '2021-01-31 04:20:39', NULL, NULL, NULL, NULL, 9.40, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (280, 4, NULL, '20210131122828187', '出租车', NULL, '18721439751', '广东省深圳市福田区八一路', '114.06023735894097', '22.571111924913193', '广东省深圳市福田区红树林片区福荣路以北(金域蓝湾公交站附近)', '114.025392', '22.523496', 13, '马师傅', '18721439751', NULL, '2021-01-31 04:28:28', '2021-01-31 12:35:31', NULL, NULL, NULL, 11.79, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (281, 2, NULL, '20210131141912188', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区嘉宾路', '108.3669', '22.81673', '明月湖欧洲风情小镇', '108.27872', '22.859331', NULL, NULL, NULL, NULL, '2021-01-31 06:19:12', NULL, NULL, NULL, NULL, 17.21, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (282, 2, NULL, '20210131142126189', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区嘉宾路', '108.3669', '22.81673', '明月湖欧洲风情小镇', '108.27872', '22.859331', 5, '陈师傅', '18007710009', NULL, '2021-01-31 06:21:27', '2021-01-31 14:21:54', NULL, NULL, NULL, 17.21, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (283, 4, NULL, '20210131151347190', '出租车', NULL, '18721439751', '广东省深圳市福田区红荔路', '114.05186469184028', '22.54823296440972', '广东省深圳市福田区圣廷苑酒店东座附近', '114.08828', '22.54957', 13, '马师傅', '18721439751', NULL, '2021-01-31 07:13:48', '2021-01-31 15:13:57', NULL, NULL, NULL, 4.73, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (284, 8, NULL, '20210131193659191', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.7895263671875', '广西壮族自治区南宁市西乡塘区高科路和创新路的交叉口附近', '108.27872', '22.859331', 12, '龚师傅', '18577833579', NULL, '2021-01-31 11:36:59', '2021-01-31 20:06:36', NULL, NULL, NULL, 12.26, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (285, 2, NULL, '20210131212128192', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区嘉宾路', '108.3669', '22.81673', '广西壮族自治区南宁市西乡塘区高科路和创新路的交叉口附近', '108.27872', '22.859331', NULL, NULL, NULL, NULL, '2021-01-31 13:21:28', NULL, NULL, NULL, NULL, 18.59, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (286, 4, NULL, '20210131214505193', '出租车', NULL, '18721439751', '万科金域蓝湾', '114.025392', '22.523496', '广东省深圳市福田区圣廷苑酒店东座附近', '114.08828', '22.54957', NULL, NULL, NULL, NULL, '2021-01-31 13:45:06', NULL, NULL, NULL, NULL, 10.61, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (287, 4, NULL, '20210131214518194', '出租车', NULL, '18721439751', '广东省深圳市福田区北环大道', '114.03864312065973', '22.56058865017361', '广东省深圳市福田区华发北路87号附近', '114.087917', '22.547333', 13, '马师傅', '18721439751', NULL, '2021-01-31 13:45:19', '2021-01-31 22:13:48', NULL, NULL, NULL, 7.85, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (288, 4, NULL, '20210131221815195', '出租车', NULL, '18721439751', '广东省深圳市福田区梅林路259号', '114.03863606770834', '22.560404188368057', '莲花山公园-风筝广场', '114.060253', '22.550065', 13, '马师傅', '18721439751', NULL, '2021-01-31 14:18:16', '2021-01-31 22:19:06', NULL, '2021-01-31 14:29:30', NULL, 5.37, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (289, 8, NULL, '20210131224013196', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.7895263671875', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-01-31 14:40:14', '2021-01-31 15:14:00', NULL, '2021-01-31 15:13:59', '2021-01-31 15:14:01', 4.72, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (290, 3, NULL, '20210201140940197', '出租车', NULL, '18007710009', '广西壮族自治区南宁市青秀区金湖路57号', '108.369384765625', '22.814586639404297', '石埠街市', '108.189588', '22.82291', NULL, NULL, NULL, NULL, '2021-02-01 06:09:41', NULL, NULL, NULL, NULL, 24.50, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (291, 8, NULL, '20210201215809198', '出租车', NULL, '18577833579', '广西壮族自治区南宁市良庆区秋月路', '108.3810693359375', '22.747008192274304', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-02-01 13:58:09', '2021-02-01 21:58:14', NULL, NULL, NULL, 11.94, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (292, 8, NULL, '20210201215923199', '出租车', NULL, '18577833579', '广西壮族自治区南宁市良庆区秋月路', '108.3810693359375', '22.747008192274304', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-02-01 13:59:24', '2021-02-02 13:13:54', NULL, '2021-02-02 13:13:52', '2021-02-02 13:14:04', 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (293, 8, NULL, '20210202213710200', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-02-02 13:37:10', '2021-02-02 21:37:22', NULL, NULL, NULL, 5.51, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (294, 8, NULL, '20210202213927201', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-02-02 13:39:28', '2021-02-02 21:39:37', NULL, NULL, NULL, 4.73, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (295, 8, NULL, '20210202214111202', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-02-02 13:41:11', '2021-02-02 21:41:17', NULL, NULL, NULL, 4.73, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (296, 8, NULL, '20210202214549203', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-02-02 13:45:50', '2021-02-02 21:45:58', NULL, NULL, NULL, 4.73, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (297, 8, NULL, '20210202214746204', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市青秀区青环路附近', '108.413182', '22.764634', 12, '龚师傅', '18577833579', NULL, '2021-02-02 13:47:46', '2021-02-02 21:47:50', NULL, NULL, NULL, 14.08, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (298, 8, NULL, '20210202214901205', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市青秀区青环路附近', '108.413182', '22.764634', 12, '龚师傅', '18577833579', NULL, '2021-02-02 13:49:01', '2021-02-02 21:49:07', NULL, NULL, NULL, 14.08, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (299, 19, NULL, '20210202224638206', '出租车', NULL, NULL, '上海市闵行区鹤庆路', '121.42011260986328', '31.010786056518555', '上海市闵行区东川路北侧(金平新村附近)', '121.413819', '31.012779', NULL, NULL, NULL, NULL, '2021-02-02 14:46:39', NULL, NULL, NULL, NULL, 0.93, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (300, 7, NULL, '20210202233014207', '出租车', NULL, '18607718485', '广西壮族自治区南宁市兴宁区037县道', '108.35555267333984', '22.930374145507812', '广西壮族自治区南宁市良庆区坛兴路27号', '108.392201', '22.719868', NULL, NULL, NULL, NULL, '2021-02-02 15:30:15', NULL, NULL, NULL, NULL, 34.19, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (301, 8, NULL, '20210202235359208', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市邕宁区良堤路附近', '108.431676', '22.766741', 12, '龚师傅', '18577833579', NULL, '2021-02-02 15:54:00', '2021-02-02 15:55:28', NULL, '2021-02-02 15:54:34', '2021-02-02 16:02:43', 0.00, '碳币支付', '2021-02-02 16:12:32', NULL, 0.01, NULL, 0.01, 0.02, 0.00, 0.00, 0.02, '2fb9b744-f44c-4078-9fcf-81b3787dbf55', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (302, 8, NULL, '20210203001329209', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市兴宁区邕武路快环建材市场二塘坡东里26号', '108.33576', '22.865641', 12, '龚师傅', '18577833579', NULL, '2021-02-02 16:13:29', '2021-02-02 16:15:26', NULL, '2021-02-02 16:14:57', '2021-02-02 16:19:17', 17.00, '碳币支付', '2021-02-02 16:20:13', NULL, 0.01, NULL, 0.00, 0.01, 0.00, 0.00, 0.01, '305eda64-528a-405f-8a9f-499a7e7d3c24', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (303, 8, NULL, '20210203003507210', '出租车', NULL, '18577833579', '广西壮族自治区南宁市江南区亭洪路', '108.2979470486111', '22.78958550347222', '广西壮族自治区南宁市武鸣区兴武大道248号附近', '108.275099', '23.158007', 12, '龚师傅', '18577833579', NULL, '2021-02-02 16:35:07', '2021-02-03 00:35:15', NULL, NULL, NULL, 52.76, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (304, 8, NULL, '20210203003604211', '出租车', NULL, '18577833579', '深圳市', '114.05956', '22.54286', '广州市', '113.26436', '23.12908', 12, '龚师傅', '18577833579', NULL, '2021-02-02 16:36:04', '2021-02-03 00:36:07', NULL, NULL, NULL, 136.21, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (305, 4, NULL, '20210203233028212', '出租车', NULL, '18721439751', '广东省深圳市福田区景田东路', '114.04495415581597', '22.554464518229167', '莲花山公园-风筝广场', '114.060253', '22.550065', 13, '马师傅', '18721439751', NULL, '2021-02-03 15:30:29', '2021-02-03 23:31:35', NULL, NULL, NULL, 4.36, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (306, 4, NULL, '20210203233544213', '出租车', NULL, '18721439751', '广东省深圳市福田区景田东路', '114.04495415581597', '22.554464518229167', '红梅苑', '114.048916', '22.565426', 13, '马师傅', '18721439751', NULL, '2021-02-03 15:35:44', '2021-02-03 23:36:06', NULL, '2021-02-03 15:43:51', NULL, 2.16, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (307, 4, NULL, '20210203235003214', '出租车', NULL, '18721439751', '广东省深圳市福田区景田东路', '114.04495415581597', '22.554464518229167', '上海市浦东新区迎宾大道6000号', '121.806263', '31.150388', 13, '马师傅', '18721439751', NULL, '2021-02-03 15:50:04', '2021-02-03 23:52:39', NULL, '2021-02-03 15:52:48', NULL, 1489.30, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (308, 4, NULL, '20210203235500215', '出租车', NULL, '18721439751', '广东省深圳市福田区景田东路', '114.04478488498263', '22.554313151041665', '上海市浦东新区迎宾大道6000号', '121.806263', '31.150388', 13, '马师傅', '18721439751', NULL, '2021-02-03 15:55:00', '2021-02-03 15:55:20', NULL, '2021-02-03 15:55:12', '2021-02-03 15:56:12', 14.00, '碳币支付', '2021-02-03 16:08:11', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, 'fc38c827-93f9-45e8-8bb7-af0c1c2b1310', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (309, 7, NULL, '20210204001620216', '出租车', NULL, '18607718485', '广西壮族自治区南宁市良庆区打铁岭路', '108.39160919189453', '22.719833374023438', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', NULL, NULL, NULL, NULL, '2021-02-03 16:16:20', NULL, NULL, NULL, NULL, 14.86, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (310, 22, NULL, '20210204184703217', '出租车', NULL, NULL, '广西壮族自治区南宁市青秀区金湖北路', '108.36994934082031', '22.81488800048828', '广西壮族自治区南宁市西乡塘区高科路和创新路的交叉口附近', '108.27872', '22.859331', NULL, NULL, NULL, NULL, '2021-02-04 10:47:04', NULL, NULL, NULL, NULL, 18.39, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (311, 24, NULL, '20210204234644218', '出租车', NULL, NULL, '广西壮族自治区南宁市良庆区打铁岭路', '108.3918228149414', '22.71962547302246', '北部湾大厦(中泰路)', '108.393117', '22.808498', NULL, NULL, NULL, NULL, '2021-02-04 15:46:45', NULL, NULL, NULL, NULL, 14.41, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (312, 23, NULL, '20210205093055219', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '柳州市', '109.41552', '24.32543', NULL, NULL, NULL, NULL, '2021-02-05 01:30:56', NULL, NULL, NULL, NULL, 231.43, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (313, 23, NULL, '20210205093533220', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', NULL, NULL, NULL, NULL, '2021-02-05 01:35:34', NULL, NULL, NULL, NULL, 25.41, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (314, 25, NULL, '20210205155033221', '出租车', NULL, NULL, '广西壮族自治区南宁市良庆区秋月路', '108.3810853407118', '22.746910264756945', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', 12, '龚师傅', '18577833579', NULL, '2021-02-05 07:50:33', '2021-02-05 07:51:25', NULL, '2021-02-05 07:51:20', '2021-02-05 07:51:42', 0.00, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (315, 21, NULL, '20210206014138222', '出租车', NULL, '18721439751', '广东省深圳市福田区梅华路321-7号', '114.04138780381945', '22.561510687934028', '广东省深圳市福田区回雁路景田社区', '114.046548', '22.556623', 13, '马师傅', '18721439751', NULL, '2021-02-05 17:41:39', '2021-02-05 17:42:06', NULL, '2021-02-05 17:42:04', '2021-02-05 17:47:58', 917.00, NULL, '2021-02-05 17:49:26', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.00, '4200000907202102064009906182', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (316, 21, NULL, '20210206022139223', '出租车', NULL, '18721439751', '广东省深圳市福田区红荔路', '114.05139105902778', '22.548203938802082', '广西壮族自治区南宁市江南区凤巢北路与明阳大道交汇处', '108.184083', '22.607926', 13, '马师傅', '18721439751', NULL, '2021-02-05 18:21:39', '2021-02-05 18:22:39', NULL, '2021-02-05 18:22:07', NULL, 748.54, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (317, 21, NULL, '20210206022318224', '出租车', NULL, '18721439751', '广东省深圳市福田区红荔路', '114.05139105902778', '22.548203938802082', '广东省深圳市福田区华发北路87号附近', '114.087917', '22.547333', 13, '马师傅', '18721439751', NULL, '2021-02-05 18:23:19', '2021-02-05 18:23:38', NULL, '2021-02-05 18:23:33', NULL, 4.46, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (318, 21, NULL, '20210206022423225', '出租车', NULL, '18721439751', '广东省深圳市福田区回雁路', '114.04583306206597', '22.557044813368055', '广东省深圳市福田区华发北路87号附近', '114.087917', '22.547333', 13, '马师傅', '18721439751', NULL, '2021-02-05 18:24:23', '2021-02-06 02:24:33', NULL, '2021-02-05 18:24:51', NULL, 6.40, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 1);
INSERT INTO `tab_order` VALUES (319, 21, NULL, '20210206022529226', '出租车', NULL, '18721439751', '广东省深圳市福田区红荔路', '114.05139105902778', '22.548203938802082', '广东省深圳市福田区深南大道6019号附近', '114.024854', '22.535606', NULL, NULL, NULL, NULL, '2021-02-05 18:25:30', NULL, NULL, NULL, NULL, 4.38, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (320, 23, NULL, '20210206095258227', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市西乡塘区邕隆路(石埠叉路批发部附近)', '108.189588', '22.82291', 5, '陈师傅', '18007710009', NULL, '2021-02-06 01:52:59', '2021-02-06 01:54:57', NULL, NULL, '2021-02-06 01:55:20', 40.75, NULL, '2021-02-06 01:59:47', NULL, 0.01, NULL, 0.00, 0.01, 0.00, 0.00, 0.00, '4200000926202102066250638312', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (321, 21, NULL, '20210206113430228', '出租车', NULL, '18721439751', '广东省深圳市罗湖区宝安南路', '114.11121527777777', '22.541923828125', '广东省深圳市罗湖区和平路1010附近', '114.117209', '22.53168', 13, '马师傅', '18721439751', NULL, '2021-02-06 03:34:31', '2021-02-06 03:35:27', NULL, '2021-02-06 03:35:11', '2021-02-06 04:07:15', 0.41, '碳币支付', '2021-02-06 04:07:57', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, 'a68aa999-45fd-45fb-8eda-7d80226b715c', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (322, 23, NULL, '20210206114408229', '出租车', NULL, '13481137304', '广西壮族自治区南宁市青秀区五合大道', '108.49693', '22.78658', '广西壮族自治区南宁市武鸣区兴武大道248号附近', '108.275099', '23.158007', 5, '陈师傅', '18007710009', NULL, '2021-02-06 03:44:08', '2021-02-06 03:44:43', NULL, NULL, NULL, 64.75, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '接送中', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (323, 26, NULL, '20210206154711230', '出租车', NULL, NULL, '广东省深圳市罗湖区笋岗东路2127-3号', '114.1095962524414', '22.555313110351562', '广东省深圳市罗湖区和平路1010附近', '114.117209', '22.53168', 13, '马师傅', '18721439751', NULL, '2021-02-06 07:47:11', '2021-02-06 07:48:28', NULL, '2021-02-06 07:48:22', '2021-02-06 07:48:48', 0.00, NULL, '2021-02-06 07:49:43', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.00, '4200000915202102065511157301', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (324, 26, NULL, '20210206155557231', '出租车', NULL, NULL, '广东省深圳市罗湖区宝安南路3070-7号', '114.10951232910156', '22.555330276489258', '广东省深圳市罗湖区和平路1010附近深圳站F1', '114.11768', '22.532271', 13, '马师傅', '18721439751', NULL, '2021-02-06 07:55:58', '2021-02-06 07:56:17', NULL, '2021-02-06 07:56:05', '2021-02-06 08:02:15', 0.37, NULL, '2021-02-06 08:03:35', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.00, '4200000910202102064814717948', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (325, 22, NULL, '20210206161749232', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.33411407470703', '22.851884841918945', '伊岭岩', '108.297214', '23.039109', NULL, NULL, NULL, NULL, '2021-02-06 08:17:50', NULL, NULL, NULL, NULL, 27.39, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (326, 22, NULL, '20210206161823233', '出租车', NULL, '18007710009', '伊岭岩', '108.297214', '23.039109', '广西壮族自治区南宁市青秀区中山路小吃街附近', '108.322666', '22.81164', NULL, NULL, NULL, NULL, '2021-02-06 08:18:24', NULL, NULL, NULL, NULL, 28.40, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (327, 21, NULL, '20210206174333234', '出租车', NULL, '18721439751', '广东省深圳市罗湖区宝安南路3070-1号', '114.10908447265625', '22.55447238498264', '广东省深圳市罗湖区北斗路18附近', '114.13697', '22.54488', 13, '马师傅', '18721439751', NULL, '2021-02-06 09:43:33', '2021-02-06 09:49:25', NULL, '2021-02-06 09:48:37', '2021-02-06 09:51:44', 0.01, '碳币支付', '2021-02-06 09:54:22', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, '3729f2e8-a4dc-4a48-a24f-04ce364dfc6b', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (328, 21, NULL, '20210206175550235', '出租车', NULL, '18721439751', '广东省深圳市罗湖区宝安南路3070-9号', '114.10919297960069', '22.555159776475694', '广东省深圳市罗湖区和平路1010附近', '114.117209', '22.53168', 13, '马师傅', '18721439751', NULL, '2021-02-06 09:55:50', '2021-02-06 09:56:05', NULL, '2021-02-06 09:56:02', '2021-02-06 09:59:29', 0.14, '碳币支付', '2021-02-06 10:00:08', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, '67eb8e8d-4c2f-42f0-9397-ce817edd08f0', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (329, 22, NULL, '20210206190412236', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区秀田路', '108.33397674560547', '22.851970672607422', '广西壮族自治区南宁市良庆区云英路与宋厢路交叉口西北100米', '108.379778', '22.765116', 11, '陆师傅', '13481137304', NULL, '2021-02-06 11:04:12', '2021-02-06 19:10:34', NULL, NULL, '2021-02-06 11:17:41', 18.91, NULL, '2021-02-06 11:18:20', NULL, 0.01, NULL, 0.00, 0.01, 0.00, 0.00, 0.00, '4200000925202102067114241333', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (330, 21, NULL, '20210208235448237', '出租车', NULL, '18721439751', '山东省青岛市城阳区民航路99号', '120.386228', '36.267067', '山东省青岛市市南区山东路10号', '120.377598', '36.06695', 13, '马师傅', '18721439751', NULL, '2021-02-08 15:54:48', '2021-02-08 15:57:41', NULL, '2021-02-08 15:57:34', '2021-02-08 15:57:46', 0.00, '碳币支付', '2021-02-08 16:31:35', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, '9483ecb7-f041-41df-8120-d85344c77b50', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (331, 21, NULL, '20210209003217238', '出租车', NULL, '18721439751', '广东省深圳市福田区红荔路', '114.05232231987847', '22.548353678385418', '广东省深圳市福田区北环大道附近', '114.011241', '22.575703', 13, '马师傅', '18721439751', NULL, '2021-02-08 16:32:18', '2021-02-08 16:32:44', NULL, '2021-02-08 16:32:41', '2021-02-08 16:32:50', 0.00, '碳币支付', '2021-02-08 16:33:07', NULL, 0.10, NULL, 0.00, 0.10, 0.00, 0.00, 0.10, 'fe99aa59-88b5-41fd-9ff7-f0887463f462', '已完成', NULL, NULL, 1);
INSERT INTO `tab_order` VALUES (332, 25, NULL, '20210211121557239', '出租车', NULL, '18577833579', '广西壮族自治区玉林市博白县兴隆东路40号', '109.9859068467882', '22.270985514322916', '广西壮族自治区南宁市青秀区桃源路6号', '108.327975', '22.802067', 12, '龚师傅', '18577833579', NULL, '2021-02-11 04:15:58', '2021-02-11 12:16:05', NULL, NULL, NULL, 310.96, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '司机取消', '默认理由', '默认备注', 1);
INSERT INTO `tab_order` VALUES (333, 21, NULL, '20210213222205240', '出租车', NULL, '18721439751', '广东省深圳市罗湖区宝安南路1881号', '114.11092', '22.539099', '广东省深圳市罗湖区宝安南路3070号西湖大厦4楼', '114.109457', '22.554855', NULL, NULL, NULL, NULL, '2021-02-13 14:22:06', NULL, NULL, NULL, NULL, 2.16, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (334, 22, NULL, '20210214092051241', '出租车', NULL, '18007710009', '伊岭岩', '108.297214', '23.039109', '明月湖欧洲风情小镇', '108.27872', '22.859331', NULL, NULL, NULL, NULL, '2021-02-14 01:20:51', NULL, NULL, NULL, NULL, 23.62, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (335, 22, NULL, '20210220121117242', '出租车', NULL, '18007710009', '广西壮族自治区南宁市青秀区金湖路57号', '108.36931610107422', '22.814573287963867', '伊岭岩', '108.297214', '23.039109', NULL, NULL, NULL, NULL, '2021-02-20 04:11:17', NULL, NULL, NULL, NULL, 38.29, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (336, 20, NULL, '20210301181800243', '出租车', NULL, '18607712940', '广西壮族自治区南宁市西乡塘区大化路6号', '108.26451958550348', '22.83626953125', '广西壮族自治区南宁市西乡塘区高科路和创新路的交叉口附近', '108.27872', '22.859331', NULL, NULL, NULL, NULL, '2021-03-01 10:18:00', NULL, NULL, NULL, NULL, 5.11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (337, 20, NULL, '20210301183841244', '出租车', NULL, '18607712940', '广西壮族自治区南宁市西乡塘区大化路6号', '108.26457763671876', '22.83626953125', '广西壮族自治区南宁市西乡塘区北湖北路15附近', '108.320745', '22.847514', NULL, NULL, NULL, NULL, '2021-03-01 10:38:41', NULL, NULL, NULL, NULL, 10.11, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (338, 22, NULL, '20210301194057245', '出租车', NULL, '18007710009', '广西壮族自治区南宁市兴宁区明秀东路', '108.334228515625', '22.850940704345703', '广西壮族自治区南宁市武鸣区210国道附近', '108.297214', '23.039109', NULL, NULL, NULL, NULL, '2021-03-01 11:40:57', NULL, NULL, NULL, NULL, 27.06, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);
INSERT INTO `tab_order` VALUES (339, 21, NULL, '20210301221140246', '出租车', NULL, '18721439751', '广东省深圳市福田区景田东路', '114.04505343967014', '22.55427978515625', '广东省深圳市福田区北环大道附近', '114.011241', '22.575703', NULL, NULL, NULL, NULL, '2021-03-01 14:11:40', NULL, NULL, NULL, NULL, 12.96, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, NULL, '客户取消', 'null', 'null', 0);

SET FOREIGN_KEY_CHECKS = 1;
