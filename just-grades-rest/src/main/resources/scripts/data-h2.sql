-- SELECT NEXT VALUE FOR USERS_SEQ;
INSERT INTO roles (role_id, role_name) VALUES
    (1, 'ROLE_STUDENT'),
    (2, 'ROLE_LECTURER'),
    (3, 'ROLE_ADMIN');

SELECT NEXT VALUE FOR USERS_SEQ;
INSERT INTO USERS
    (USER_ID, FIRST_NAME, LAST_NAME, PASSWORD, email)
VALUES
    (1, 'w', 'm', '1', 'w.m@gmail.com'),
    (2, 'Marie', 'Johnson', '123', 'marie@g.com'),
    (3, 'Alicja', 'Kot', '321', 'all.k@g.com'),
    (4, 'Jan', 'Kowalski', 'qwerty', 'king.jan@g.com');

UPDATE users SET role_id = 1 WHERE user_id IN (1, 2);
UPDATE users SET role_id = 2 WHERE user_id IN (3, 4);

SELECT NEXT VALUE FOR COURSES_SEQ;
INSERT INTO courses (course_id, name, ects)
VALUES
    (1, 'philosophy', 4),
    (2, 'work ethic', 3),
    (3, 'learning methods', 4),
    (4, 'modern physics', 6),
    (5, 'basics of physics', 5);

SELECT NEXT VALUE FOR REQUIREMENTS_SEQ;
INSERT INTO completion_req (req_id, min_score, type)
VALUES
    (1, 51, 'exam'),
    (2, 21, 'laboratories'),
    (3, 21, 'tests');

INSERT INTO course_req (courses_id, req_id)
VALUES
    (1, 2),
    (1, 3),
    (2, 3),
    (3, 3),
    (4, 1),
    (4, 2),
    (4, 3),
    (5, 1);

SELECT NEXT VALUE FOR REGISTRATION_SEQ;
INSERT INTO course_reg (reg_id, min_ects, course_id)
VALUES
    (1, 30, 1),
    (4, 60, 4);

INSERT INTO course_reg (reg_id, complited_course_id, course_id)
VALUES
    (5, 5, 4);

INSERT INTO students (user_id, student_number, status, library_card_number)
VALUES
    (1, 'S001', 1, 'LC001'),
    (2, 'S002', 1, 'LC002');

INSERT INTO lecturers (user_id, lecture_id, academic_title)
VALUES
    (3, 1, 'Professor'),
    (4, 2, 'Dr.');

INSERT INTO grades (grade_id, grade, recorded, student_id, courses_id, lecturer_id, type)
VALUES
    (1, 85, DATE '2024-11-01', 1, 1, 3, 'exam'),
    (2, 75, DATE '2024-11-01', 2, 2, 3, 'laboratories'),
    (3, 90, DATE '2024-11-01', 2, 3, 4, 'tests'),
    (4, 88, DATE '2024-11-01', 1, 4, 4, 'exam'),
    (5, 72, DATE '2024-11-01', 1, 5, 3, 'laboratories');
