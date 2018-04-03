USE plumdo_identity;
SET FOREIGN_KEY_CHECKS=0;


DROP PROCEDURE IF EXISTS `init_data`; 

CREATE PROCEDURE init_data() 
BEGIN 
	IF NOT EXISTS(SELECT 1 FROM `pw_id_user` WHERE `account_`='admin')
	THEN
	     INSERT INTO `pw_id_user` (`name_`,`phone_`,`account_`,`pwd_`,`rev_`) VALUES('管理员', '13559120571', 'admin', '123456',0);
	END IF;
END;

CALL init_data();

DROP PROCEDURE IF EXISTS init_data; 

