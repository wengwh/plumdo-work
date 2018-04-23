USE plumdo_identity$$
SET FOREIGN_KEY_CHECKS=0$$

DROP PROCEDURE IF EXISTS `init_data`$$
CREATE PROCEDURE init_data() 
BEGIN 
	
	DECLARE user_id_ INT(10);
	DECLARE group_id_ INT(10);
	DECLARE role_id_ INT(10);
	DECLARE menu_id_ INT(10);
  	DECLARE _done int default 0;  
  	DECLARE menu_cur CURSOR FOR select id_ from `pw_id_menu` where type_=1;
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET _done = 1;
	
	IF NOT EXISTS(SELECT 1 FROM `pw_id_user` WHERE `account_`='admin') 
	THEN 
		INSERT INTO `pw_id_user` (`name_`,`phone_`,`account_`,`pwd_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('管理员', '13559120571', 'admin', '123456',0,now(),now()); 
		SELECT LAST_INSERT_ID() into user_id_;

		INSERT INTO `pw_id_role` (`name_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('管理员角色', 0,now(),now()); 
		SELECT LAST_INSERT_ID() into role_id_;
		
		INSERT INTO `pw_id_user_role` (`user_id_`,`role_id_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES(user_id_, role_id_,0,now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('人员权限管理', 'fa-slideshare', '0', '0', 1 , 'main.idm', 0, now(),now()); 
		SELECT LAST_INSERT_ID() into menu_id_;
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('人员管理', 'fa-user-plus', '1', menu_id_ , 1 , 'main.idm.user', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('群组管理', 'fa-group', '1', menu_id_ , 2 , 'main.idm.group', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('菜单管理', 'fa-windows', '1', menu_id_ , 3 , 'main.idm.menu', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('角色管理', 'fa-address-card', '1', menu_id_ , 4 , 'main.idm.role', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('设计器管理', 'fa-modx', '0', '0', 2 , 'main.modeler', 0, now(),now()); 
		SELECT LAST_INSERT_ID() into menu_id_;
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('表单设计器', 'fa-wpforms', '1', menu_id_ , 1 , 'main.modeler.form', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('流程设计器', 'fa-sitemap', '1', menu_id_ , 2 , 'main.modeler.flow', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('流程后台管理', 'fa-tachometer', '0', '0', 3 , 'main.flow', 0, now(),now()); 
		SELECT LAST_INSERT_ID() into menu_id_;
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('定义管理', 'fa-database', '1', menu_id_ , 1 , 'main.flow.definition', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('实例管理', 'fa-yelp', '1', menu_id_ , 2 , 'main.flow.instance', 0, now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('任务管理', 'fa-tasks', '1', menu_id_ , 3 , 'main.flow.task', 0, now(),now()); 
		
		
		OPEN menu_cur;  
		read_loop: LOOP
		FETCH menu_cur INTO menu_id_;  
		  	IF _done THEN LEAVE read_loop;
		    END IF;
		    INSERT INTO `pw_id_role_menu` (`menu_id_`,`role_id_`,`rev_`,`create_time_`,`last_update_time_`) 
			VALUES(menu_id_, role_id_,0,now(),now()); 
		END LOOP;
		CLOSE menu_cur;

		INSERT INTO `pw_id_group` (`name_`,`parent_id_`,`type_`,`order_`,`status_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('测试组', '0', '0', 1 , 0, 0, now(),now()); 
		
		INSERT INTO `pw_id_group` (`name_`,`parent_id_`,`type_`,`order_`,`status_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('开发组', '0', '0', 1 , 0, 0, now(),now()); 
		SELECT LAST_INSERT_ID() into group_id_;
		
		INSERT INTO `pw_id_group` (`name_`,`parent_id_`,`type_`,`order_`,`status_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('开发1组', group_id_, '1', 1 , 0, 0, now(),now()); 
		
		INSERT INTO `pw_id_group` (`name_`,`parent_id_`,`type_`,`order_`,`status_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('开发2组', group_id_, '1', 2 , 0, 0, now(),now()); 
		
	END IF; 
END $$

CALL init_data()$$
DROP PROCEDURE IF EXISTS `init_data`$$