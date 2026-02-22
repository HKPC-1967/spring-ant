
DROP TABLE IF EXISTS demo_rule;
DROP TABLE IF EXISTS db_config;
DROP TABLE IF EXISTS account_role_relation;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS user_account;
DROP TABLE IF EXISTS test_mybatis_generator;




CREATE TABLE user_account (
                              id SERIAL PRIMARY KEY,
                              disabled BOOLEAN NOT NULL DEFAULT false,
                              perm_ver INTEGER NOT NULL DEFAULT 1,
                              username VARCHAR(50) UNIQUE NOT NULL,
                              password VARCHAR(255) NOT NULL,
                              email VARCHAR(100) UNIQUE NOT NULL,
                              nickname VARCHAR(50) NOT NULL,

                              avatar VARCHAR(300),
                              title VARCHAR(100),
                              additional_info JSONB,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE user_account IS 'the login account and the basic information of the user';
COMMENT ON COLUMN user_account.id IS 'Auto-incremented unique ID';
COMMENT ON COLUMN user_account.perm_ver IS 'permission_version, if the permission changes, the JWT token will be invalid';
COMMENT ON COLUMN user_account.username IS 'Username for login, must be unique';
COMMENT ON COLUMN user_account.password IS 'Password with encryption';
COMMENT ON COLUMN user_account.nickname IS 'The nickname for display';

INSERT INTO user_account
(id, perm_ver, username, "password", email, nickname, avatar, title, additional_info, created_at, updated_at)
VALUES(1,1, 'admin', '$2a$10$PAJGE4d1upApRodWX9V6GeQSNQ5/KwzdxtLE1xO5ULfHjnB2k14lG', 'admin@133.com', 'Admin', NULL, NULL, NULL, '2024-11-06 17:05:59.076', '2024-11-06 17:05:59.076');
INSERT INTO user_account
(id, perm_ver, username, "password", email, nickname, avatar, title, additional_info, created_at, updated_at)
VALUES(2,1, 'user', '$2a$10$PAJGE4d1upApRodWX9V6GeQSNQ5/KwzdxtLE1xO5ULfHjnB2k14lG', 'user@133.com', 'User', NULL, NULL, NULL, '2024-11-06 17:05:59.076', '2024-11-06 17:05:59.076');



CREATE TABLE user_role (
                           id SERIAL PRIMARY KEY,
                           disabled BOOLEAN NOT NULL DEFAULT false,
                           role_name VARCHAR(50) UNIQUE NOT NULL,
                           description VARCHAR(200),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE user_role IS 'the role of the user for permission control';
COMMENT ON COLUMN user_role.id IS 'Auto-incremented primary key';
COMMENT ON COLUMN user_role.role_name IS 'Role name for display';
COMMENT ON COLUMN user_role.description IS 'Description of the role';

INSERT INTO user_role
(id, disabled, role_name, description, created_at, updated_at)
VALUES(1, false, 'Admin', NULL, '2024-11-13 16:41:34.695', '2024-11-13 16:41:34.695');
INSERT INTO user_role
(id, disabled, role_name, description, created_at, updated_at)
VALUES(2, false, 'User', NULL, '2024-11-13 16:41:50.954', '2024-11-13 16:41:50.954');



CREATE TABLE account_role_relation (
                                       user_account_id INTEGER NOT NULL,
                                       user_role_id INTEGER NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       PRIMARY KEY (user_account_id, user_role_id),
                                       FOREIGN KEY (user_account_id) REFERENCES user_account(id),
                                       FOREIGN KEY (user_role_id) REFERENCES user_role(id)
);

COMMENT ON TABLE account_role_relation IS 'Relation table between user_account and user_role';
COMMENT ON COLUMN account_role_relation.user_account_id IS 'Foreign key referencing user_account';
COMMENT ON COLUMN account_role_relation.user_role_id IS 'Foreign key referencing user_role';

INSERT INTO account_role_relation
(user_account_id, user_role_id, created_at)
VALUES(1, 1, '2024-11-20 10:55:38.361');
INSERT INTO account_role_relation
(user_account_id, user_role_id, created_at)
VALUES(2, 2, '2024-11-20 10:55:45.895');





CREATE TABLE db_config (
                           id SERIAL PRIMARY KEY,
                           disabled BOOLEAN NOT NULL DEFAULT false,
                           key VARCHAR(255) UNIQUE NOT NULL,
                           value VARCHAR(2000) NOT NULL,
                           description VARCHAR(255),
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table comment
COMMENT ON TABLE db_config IS 'Stores initialization settings for the application';

-- Column comments
COMMENT ON COLUMN db_config.key IS 'The unique key for the setting';
COMMENT ON COLUMN db_config.value IS 'The value associated with the key';
COMMENT ON COLUMN db_config.description IS 'Description of the setting';

INSERT INTO db_config
(id, disabled, "key", value, description, created_at, updated_at)
VALUES(1, false, 'jwt_key', 'sdVM7oi8OIih+kKv/5iQai7c29HeysAGczhpOatyeJ6l5GGcNLXHh1St+8FZCWAQ1Zou5Q5kAxrxB1Ebdv0sFTpnZ32q1WolpIla8r5+gzOZ2J2JxjFEvAAdFmNk7ZdC', 'the encrypted jwt key', '2024-11-07 11:38:13.439', '2024-11-07 11:38:13.439');







CREATE TABLE demo_rule (
                           key SERIAL PRIMARY KEY,
                           disabled BOOLEAN NOT NULL DEFAULT false,
                           href VARCHAR(255) ,
                           avatar VARCHAR(255) ,
                           name VARCHAR(255) NOT NULL,
                           owner VARCHAR(255) ,
                           "desc" TEXT NOT NULL,
                           call_no INTEGER ,
                           status VARCHAR(10) NOT NULL,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           progress INTEGER NOT NULL
);

COMMENT ON TABLE demo_rule IS 'Table to store demo records of rule';
COMMENT ON COLUMN demo_rule.key IS 'Use "id" for new table ("key" is just aligned with Ant Design Pro); auto-incremented primary key';
COMMENT ON COLUMN demo_rule.disabled IS 'Indicates if the record is disabled (true/false)';
COMMENT ON COLUMN demo_rule.href IS 'URL link associated with the record';
COMMENT ON COLUMN demo_rule.avatar IS 'URL of the avatar image';
COMMENT ON COLUMN demo_rule.name IS 'Name of the item or entity';
COMMENT ON COLUMN demo_rule.owner IS 'Owner of the item or entity';
COMMENT ON COLUMN demo_rule."desc" IS 'Description of the rule';
COMMENT ON COLUMN demo_rule.call_no IS 'Call number or a related numeric identifier';
COMMENT ON COLUMN demo_rule.status IS 'Status of the item, typically a short string';
COMMENT ON COLUMN demo_rule.updated_at IS 'Timestamp of the last update to the record';
COMMENT ON COLUMN demo_rule.created_at IS 'Timestamp of when the record was created';
COMMENT ON COLUMN demo_rule.progress IS 'Progress value, typically a percentage or score';

INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (81, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 81', '曲丽丽', '这是一段描述', 406, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 61);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (80, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 80', '曲丽丽', '这是一段描述', 112, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 20);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (79, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 79', '曲丽丽', '这是一段描述', 405, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 60);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (78, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 78', '曲丽丽', '这是一段描述', 111, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 19);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (77, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 77', '曲丽丽', '这是一段描述', 404, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 59);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (76, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 76', '曲丽丽', '这是一段描述', 110, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 18);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (75, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 75', '曲丽丽', '这是一段描述', 403, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 58);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (74, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 74', '曲丽丽', '这是一段描述', 109, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 17);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (73, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 73', '曲丽丽', '这是一段描述', 402, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 57);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (72, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 72', '曲丽丽', '这是一段描述', 108, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 16);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (71, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 71', '曲丽丽', '这是一段描述', 401, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 56);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (70, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 70', '曲丽丽', '这是一段描述', 107, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 15);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (69, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 69', '曲丽丽', '这是一段描述', 400, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 55);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (68, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 68', '曲丽丽', '这是一段描述', 106, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 14);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (67, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 67', '曲丽丽', '这是一段描述', 399, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 54);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (66, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 66', '曲丽丽', '这是一段描述', 105, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 13);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (65, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 65', '曲丽丽', '这是一段描述', 398, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 53);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (64, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 64', '曲丽丽', '这是一段描述', 104, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 12);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (63, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png', 'TradeCode 63', '曲丽丽', '这是一段描述', 397, '3', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 52);
INSERT INTO demo_rule (key, disabled, href, avatar, name, owner, "desc", call_no, status, updated_at, created_at, progress) VALUES (62, false, 'https://ant.design', 'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png', 'TradeCode 62', '曲丽丽', '这是一段描述', 103, '2', '2022-12-06T05:00:57.040Z', '2022-12-06T05:00:57.040Z', 11);



-- Create table for MyBatis Generator testing
CREATE TABLE test_mybatis_generator (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
                                        email VARCHAR(255),
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add table comment in English
COMMENT ON TABLE test_mybatis_generator IS 'Table used for MyBatis Generator testing';

-- Add column comments in English
COMMENT ON COLUMN test_mybatis_generator.id IS 'Primary key, auto-incrementing unique identifier';
COMMENT ON COLUMN test_mybatis_generator.name IS 'User full name, up to 100 characters, not nullable';
COMMENT ON COLUMN test_mybatis_generator.email IS 'User email address, up to 255 characters, nullable';
COMMENT ON COLUMN test_mybatis_generator.created_at IS 'Timestamp when the record was created, defaults to current time';
