USE plumdo_identity;
SET FOREIGN_KEY_CHECKS=0;

INSERT INTO `pw_id_user` (`name_`,`phone_`,`account_`,`pwd_`,`rev_`) SELECT '管理员', '13559120571', 'admin', '123456',0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `pw_id_user` WHERE `account_`='admin');

