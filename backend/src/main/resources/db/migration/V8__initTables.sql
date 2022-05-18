ALTER TABLE app_user
ADD COLUMN reactivation_email varchar(255);

ALTER TABLE app_user
ADD COLUMN is_blocked boolean DEFAULT false;

--could be independent table
ALTER TABLE app_user
ADD COLUMN login_time timestamp;

ALTER TABLE app_user
ADD COLUMN initial_login timestamp;
--

insert into role_assignment(user_id, role_id) values (4, 2);
insert into role_assignment(user_id, role_id) values (4, 1);

--should make first time login automatic after registration
--to differentiate initial login (so far just comparing login day, month, year)
UPDATE app_user
SET login_time = '2022-01-01 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'sarazaimovic@gmail.com';

UPDATE app_user
SET login_time = '2022-01-01 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'mehdizaimovic@gmail.com';

UPDATE app_user
SET login_time = '2022-01-01 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'sarazaimovic1@gmail.com';

UPDATE app_user
SET login_time = '2022-01-01 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'mehdizaimovic1@gmail.com';

UPDATE app_user
SET login_time = '2022-04-29 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'sarazaimovic2@gmail.com';

UPDATE app_user
SET login_time = '2022-01-01 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'mehdizaimovic2@gmail.com';

UPDATE app_user
SET login_time = '2022-02-01 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'sarazaimovic3@gmail.com';

UPDATE app_user
SET login_time = '2022-01-01 11:00:00',
    initial_login = '2022-01-01 11:00:00'
WHERE email = 'mehdizaimovic3@gmail.com';



