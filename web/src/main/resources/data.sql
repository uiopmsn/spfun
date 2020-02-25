-- 角色
INSERT INTO sys_role(role_code, role_name)
SELECT 'admin', '管理员' FROM DUAL WHERE NOT EXISTS
(SELECT role_code FROM sys_role WHERE role_code = 'admin');

INSERT INTO sys_role(role_code, role_name)
SELECT 'user', '普通用户' FROM DUAL WHERE NOT EXISTS
(SELECT role_code FROM sys_role WHERE role_code = 'user');

-- 菜单权限
INSERT INTO sys_perm (perm_code, perm_name, icon, pid, type, order_by)
select '/welcome', '欢迎', '', -1, 'menu', 1 FROM DUAL WHERE NOT EXISTS
(SELECT perm_code FROM sys_perm WHERE perm_code = '/welcome');

INSERT INTO sys_perm (perm_code, perm_name, icon, pid, type, order_by)
select '/sysMange', '系统管理', '', -1, 'menu', 900 FROM DUAL WHERE NOT EXISTS
(SELECT perm_code FROM sys_perm WHERE perm_code = '/sysMange');

select @permId := id from sys_perm where perm_code='/sysMange';

INSERT INTO sys_perm (perm_code, perm_name, icon, pid, type, order_by)
select '/sysMange/roleManage', '角色管理', '', @permId, 'menu', 909 FROM DUAL WHERE NOT EXISTS
(SELECT perm_code FROM sys_perm WHERE perm_code = '/sysMange/roleManage');

INSERT INTO sys_perm (perm_code, perm_name, icon, pid, type, order_by)
select '/sysMange/permManage', '菜单权限', '', @permId, 'menu', 910 FROM DUAL WHERE NOT EXISTS
(SELECT perm_code FROM sys_perm WHERE perm_code = '/sysMange/permManage');

INSERT INTO sys_perm (perm_code, perm_name, icon, pid, type, order_by)
select '/sysMange/userManage', '用户管理', '', @permId, 'menu', 920 FROM DUAL WHERE NOT EXISTS
(SELECT perm_code FROM sys_perm WHERE perm_code = '/sysMange/userManage');

-- 角色-权限
select @roleId := id from sys_role where role_code = 'admin';

select @permId := id from sys_perm where perm_code='/welcome';
INSERT INTO sys_role_perm (role_id, perm_id)
select @roleId, @permId FROM DUAL WHERE NOT EXISTS
(SELECT perm_id FROM sys_role_perm WHERE perm_id = @permId and role_id = @roleId);

select @permId := id from sys_perm where perm_code='/sysMange';
INSERT INTO sys_role_perm (role_id, perm_id)
select @roleId, @permId FROM DUAL WHERE NOT EXISTS
(SELECT perm_id FROM sys_role_perm WHERE perm_id = @permId and role_id = @roleId);

select @permId := id from sys_perm where perm_code='/sysMange/roleManage';
INSERT INTO sys_role_perm (role_id, perm_id)
select @roleId, @permId FROM DUAL WHERE NOT EXISTS
(SELECT perm_id FROM sys_role_perm WHERE perm_id = @permId and role_id = @roleId);

select @permId := id from sys_perm where perm_code='/sysMange/permManage';
INSERT INTO sys_role_perm (role_id, perm_id)
select @roleId, @permId FROM DUAL WHERE NOT EXISTS
(SELECT perm_id FROM sys_role_perm WHERE perm_id = @permId and role_id = @roleId);

select @permId := id from sys_perm where perm_code='/sysMange/userManage';
INSERT INTO sys_role_perm (role_id, perm_id)
select @roleId, @permId FROM DUAL WHERE NOT EXISTS
(SELECT perm_id FROM sys_role_perm WHERE perm_id = @permId and role_id = @roleId);


select @roleId := id from sys_role where role_code = 'user';

select @permId := id from sys_perm where perm_code='/welcome';
INSERT INTO sys_role_perm (role_id, perm_id)
select @roleId, @permId FROM DUAL WHERE NOT EXISTS
(SELECT perm_id FROM sys_role_perm WHERE perm_id = @permId and role_id = @roleId);

-- 用户
INSERT INTO sys_user(user_name, pass_word)
SELECT 'system', '202cb962ac59075b964b07152d234b70' FROM DUAL WHERE NOT EXISTS
(SELECT user_name FROM sys_user WHERE user_name = 'system');

INSERT INTO sys_user(user_name, pass_word)
SELECT 'guest', '202cb962ac59075b964b07152d234b70' FROM DUAL WHERE NOT EXISTS
(SELECT user_name FROM sys_user WHERE user_name = 'guest');

-- 用户-角色
select @roleId := id from sys_role where role_code='admin';
INSERT INTO sys_user_role(role_id, user_name)
SELECT @roleId, 'system' FROM DUAL WHERE NOT EXISTS
(SELECT user_name FROM sys_user_role WHERE role_id = @roleId and user_name = 'system');

select @roleId := id from sys_role where role_code='user';
INSERT INTO sys_user_role(role_id, user_name)
SELECT @roleId, 'guest' FROM DUAL WHERE NOT EXISTS
(SELECT user_name FROM sys_user_role WHERE role_id = @roleId and user_name = 'guest');

-- 用户-数据源
INSERT INTO sys_user_datasource(data_source_id, user_name)
SELECT 'master', 'system' FROM DUAL WHERE NOT EXISTS
(SELECT data_source_id FROM sys_user_datasource WHERE data_source_id = "master" and user_name = 'system');

INSERT INTO sys_user_datasource(data_source_id, user_name)
SELECT 'master', 'guest' FROM DUAL WHERE NOT EXISTS
(SELECT data_source_id FROM sys_user_datasource WHERE data_source_id = "master" and user_name = 'guest');
