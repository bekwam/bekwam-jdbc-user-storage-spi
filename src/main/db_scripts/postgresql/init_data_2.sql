-- script to test a second instance of the component in the realm

INSERT INTO us_user_2 (username, password) VALUES ('theuser', encode(digest('ghi789', 'sha256'), 'base64'));

INSERT INTO us_role_2 (name) VALUES ('user');

INSERT INTO us_user_role_2 (user_id, role_id)
SELECT us_user_2.id, us_role_2.id
FROM us_user_2 CROSS JOIN us_role_2 WHERE username = 'theuser' AND us_role_2.name = 'user'

-- query to verify joins
SELECT username, us_role_2.name
FROM us_user_2
         JOIN us_user_role_2 ON (us_user_role_2.user_id = us_user_2.id)
         JOIN us_role_2 ON (us_user_role_2.role_id = us_role_2.id)
