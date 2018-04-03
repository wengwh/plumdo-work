USE plumdo_identity$$
SET FOREIGN_KEY_CHECKS=0$$


DROP PROCEDURE IF EXISTS `init_data`$$
CREATE PROCEDURE init_data() 
BEGIN 
	IF NOT EXISTS(SELECT 1 FROM `pw_id_user` WHERE `account_`='admin') 
	THEN 
		INSERT INTO `pw_id_user` (`name_`,`phone_`,`account_`,`pwd_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('管理员', '13559120571', 'admin', '123456',0,now(),now()); 
		
		INSERT INTO `pw_id_menu` (`name_`,`icon_`,`type_`,`parent_id_`,`order_`,`route_`,`rev_`,`create_time_`,`last_update_time_`) 
		VALUES('人员权限管理', 'fa-user', '0', '0',1 , 'main.idm', 0, now(),now()); 
		
	END IF; 
END $$

CALL init_data()$$