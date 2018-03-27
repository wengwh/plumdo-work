CREATE DATABASE IF NOT EXISTS plumdo_identity DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE plumdo_identity;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- 租户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_tenant`(
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name_` varchar(64) NOT NULL COMMENT '菜单名称',
  `remark_` varchar(500) DEFAULT '' COMMENT '菜单说明',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户表';


-- ----------------------------
-- 系统菜单表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_menu`(
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code_` varchar(64) NOT NULL COMMENT '菜单编号',
  `name_` varchar(64) NOT NULL COMMENT '菜单名称',
  `icon_` varchar(255) NOT NULL DEFAULT '' COMMENT '菜单图标',
  `type_` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '菜单类型 0 业务分类 1 菜单分类 2 菜单模块',
  `parent_id_` int(10) unsigned NOT NULL COMMENT '父级菜单ID,根群组id为1',
  `order_` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '序号',
  `url_` varchar(255) DEFAULT '' COMMENT '菜单对应的URL地址',
  `remark_` varchar(500) DEFAULT '' COMMENT '菜单说明',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

-- ----------------------------
-- 群组表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_group`  (
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id_` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '群组主管ID',
  `parent_id_` int(10) unsigned NOT NULL COMMENT '父亲群组id。根群组id为1',
  `order_` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '序号',
  `name_` varchar(255) NOT NULL COMMENT '群组名称',
  `status_` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '状态值0:正常 1:停用 2:删除',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群组表';

-- ----------------------------
-- 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_user`   (
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name_` varchar(64) NOT NULL COMMENT '用户姓名',
  `avatar_` varchar(255) DEFAULT '' COMMENT '头像url',
  `sex_` tinyint(4) unsigned NOT NULL DEFAULT '1' COMMENT '性别 0表示女性 1表示男性',
  `phone_` varchar(64) DEFAULT '' COMMENT '联系电话',
  `email_` varchar(64) DEFAULT '' COMMENT 'email',
  `account_` varchar(64) NOT NULL COMMENT '登录帐号',
  `pwd_` varchar(64) NOT NULL COMMENT '登录密码(MD5加密后保存的数据)',
  `remark_` varchar(500) DEFAULT '' COMMENT '备注',
  `status_` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '状态值0:正常 1:停用 2:删除',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';


-- ----------------------------
-- 用户与群组对应表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_user_group`  (
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id_` int(10) unsigned NOT NULL COMMENT '用户ID',
  `group_id_` int(10) unsigned NOT NULL COMMENT '群组ID',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与群组对应表';

-- ----------------------------
-- 角色表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_role`    (
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name_` varchar(64) NOT NULL COMMENT '角色名称',
  `remark_` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- 角色与菜单对应表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_role_menu`  (
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id_` int(10) unsigned NOT NULL COMMENT '角色ID',
  `menu_id_` int(10) unsigned NOT NULL COMMENT '菜单ID',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应表';

-- ----------------------------
-- 用户与角色对应表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `pw_id_user_role`  (
  `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id_` int(10) unsigned NOT NULL COMMENT '用户ID',
  `role_id_` int(10) unsigned NOT NULL COMMENT '角色ID',
  `create_time_` timestamp(3) NULL COMMENT '创建时间',
  `last_update_time_` timestamp(3) NULL COMMENT '更新时间',
  `rev_` int(10) unsigned NOT NULL COMMENT '乐观锁版本号',
  `tenant_id_` varchar(255) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色对应表';



