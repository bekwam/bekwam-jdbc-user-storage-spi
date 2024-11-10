-- must have pg_crypto available postgres-wide and enabled for this DB
CREATE EXTENSION pgcrypto;

INSERT INTO us_user (username, password) VALUES ('myuser', encode(digest('abc123', 'sha256'), 'base64'));
INSERT INTO us_user (username, password) VALUES ('myadmin', encode(digest('def456', 'sha256'), 'base64'));

INSERT INTO us_role (name) VALUES ('user');
INSERT INTO us_role (name) VALUES ('admin');

INSERT INTO us_user_role (user_id, role_id)
SELECT us_user.id, us_role.id
FROM us_user CROSS JOIN us_role WHERE username = 'myuser' AND name = 'user'

INSERT INTO us_user_role (user_id, role_id)
SELECT us_user.id, us_role.id
FROM us_user CROSS JOIN us_role WHERE username = 'myadmin' AND name = 'admin'

-- query to verify joins
SELECT username, name
FROM us_user
         JOIN us_user_role ON (us_user_role.user_id = us_user.id)
         JOIN us_role ON (us_user_role.role_id = us_role.id)
