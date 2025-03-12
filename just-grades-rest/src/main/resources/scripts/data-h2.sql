SELECT NEXT VALUE FOR USERS_SEQ;

INSERT INTO USERS
    (USER_ID, FIRST_NAME, LAST_NAME, PASSWORD_HASH)
VALUES
    (1, 'w', 'm', '1'),
    (2, 'Marie', 'Johnson', '123'),
    (3, 'Alicja', 'Kot', '321'),
    (4, 'Jan', 'Kowalski', 'qwerty');