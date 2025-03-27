-- SELECT NEXT VALUE FOR USERS_SEQ;

INSERT INTO USERS
    (USER_ID, FIRST_NAME, LAST_NAME, PASSWORD_HASH, email)
VALUES
    (1, 'w', 'm', '1', 'w.m@gmail.com'),
    (2, 'Marie', 'Johnson', '123', 'marie@g.com'),
    (3, 'Alicja', 'Kot', '321', 'all.k@g.com'),
    (4, 'Jan', 'Kowalski', 'qwerty', 'king.jan@g.com');

INSERT INTO courses (course_id, name, ects)
VALUES
    (1, 'philosophy', 4),
    (2, 'work ethic', 3),
    (3, 'learning methods', 4),
    (4, 'modern physics', 6),
    (5, 'basics of physics', 5);

INSERT INTO completion_req (req_id, min_score, type)
VALUES
    (1, 51, 'physic'),
    (2, 31, 'social courses');

INSERT INTO course_req (courses_id, req_id)
VALUES
    (1, 2),
    (2, 2),
    (3, 2),
    (4, 1),
    (5, 1);

INSERT INTO course_reg (reg_id, regulation_text, courses_id)
VALUES
    (1, '> 30 ects', 1),
    (4, '> 61 ects', 4),
    (5, 'complited basics of physics', 4);