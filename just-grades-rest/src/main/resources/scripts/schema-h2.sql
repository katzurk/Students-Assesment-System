CREATE SEQUENCE USERS_SEQ START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE COURSES_SEQ START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE REGISTRATION_SEQ START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE REQUIREMENTS_SEQ START WITH 1 INCREMENT BY 50;


CREATE TABLE classes (
    class_id            INTEGER NOT NULL,
    time                DATE NOT NULL,
    day_of_week         INTEGER NOT NULL,
    group_nr            INTEGER NOT NULL,
    user_id         INTEGER,
    courses_id          INTEGER,
    classroom           INTEGER NOT NULL,
    course_type         VARCHAR2(50) NOT NULL
);

ALTER TABLE classes ADD CONSTRAINT classes_pk PRIMARY KEY ( class_id );

CREATE TABLE completion_req (
    req_id         INTEGER NOT NULL,
    min_score      INTEGER NOT NULL,
    is_mandatory   NUMBER,
    type           VARCHAR2(50) NOT NULL
);

ALTER TABLE completion_req ADD CONSTRAINT completion_req_pk PRIMARY KEY ( req_id );

CREATE TABLE course_req (
    courses_id  INTEGER NOT NULL,
    req_id      INTEGER NOT NULL
);

ALTER TABLE course_req ADD CONSTRAINT course_req_pk PRIMARY KEY ( courses_id, req_id );

CREATE TABLE course_reg (
    reg_id     INTEGER NOT NULL,
    min_ects NUMBER,
    complited_course_id INTEGER,
    course_id INTEGER NOT NULL
);

ALTER TABLE course_reg ADD CONSTRAINT course_reg_pk PRIMARY KEY ( reg_id );

CREATE TABLE courses (
    course_id   INTEGER NOT NULL,
    name        VARCHAR2(50) NOT NULL,
    ects        INTEGER NOT NULL
);

ALTER TABLE courses ADD CONSTRAINT courses_pk PRIMARY KEY ( course_id );

CREATE TABLE courses_lectures (
    courses_id    INTEGER NOT NULL,
    user_id  INTEGER NOT NULL
);

ALTER TABLE courses_lectures ADD CONSTRAINT courses_lectures_pk PRIMARY KEY ( courses_id, user_id );

CREATE TABLE courses_spec (
    courses_id  INTEGER NOT NULL,
    spec_id     INTEGER NOT NULL
);

ALTER TABLE courses_spec ADD CONSTRAINT courses_spec_pk PRIMARY KEY ( courses_id, spec_id );

CREATE TABLE grades (
    grade_id    INTEGER NOT NULL,
    grade       INTEGER NOT NULL,
    recorded    DATE,
    student_id INTEGER,
    courses_id   INTEGER,
    lecturer_id  INTEGER,
    type        VARCHAR2(50) NOT NULL
);

ALTER TABLE grades ADD CONSTRAINT grades_pk PRIMARY KEY ( grade_id );

CREATE TABLE lecturers (
    user_id        INTEGER NOT NULL,
    lecture_id     INTEGER NOT NULL,
    academic_title VARCHAR2(80)
);

ALTER TABLE lecturers ADD CONSTRAINT lecturers_pk PRIMARY KEY ( user_id );
ALTER TABLE lecturers ADD CONSTRAINT lecturers_pkv1 UNIQUE ( lecture_id );

CREATE TABLE roles (
    role_id INTEGER NOT NULL,
    role_name    VARCHAR2(50) NOT NULL
);

ALTER TABLE roles ADD CONSTRAINT roles_pk PRIMARY KEY ( role_id );

CREATE TABLE semester_ects (
    sem_ects_id INTEGER NOT NULL,
    semester    INTEGER NOT NULL,
    ects_number INTEGER,
    spec_id     INTEGER NOT NULL
);

ALTER TABLE semester_ects ADD CONSTRAINT semester_ects_pk PRIMARY KEY ( sem_ects_id );

CREATE TABLE specializations (
    spec_id INTEGER NOT NULL,
    name    VARCHAR2(50) NOT NULL
);

ALTER TABLE specializations ADD CONSTRAINT spec_pk PRIMARY KEY ( spec_id );

CREATE TABLE student_spec (
    spec_id    INTEGER NOT NULL,
    user_id INTEGER NOT NULL
);

ALTER TABLE student_spec ADD CONSTRAINT student_spec_pk PRIMARY KEY ( spec_id, user_id );

CREATE TABLE students (
    user_id             INTEGER NOT NULL,
    student_number      VARCHAR2(50) NOT NULL,
    status              NUMBER NOT NULL,
    library_card_number VARCHAR2(50)
);

ALTER TABLE students ADD CONSTRAINT students_pk PRIMARY KEY ( user_id );

CREATE TABLE users (
    user_id      INTEGER NOT NULL,
    first_name   VARCHAR2(50) NOT NULL,
    last_name    VARCHAR2(50) NOT NULL,
    email        VARCHAR2(50) NOT NULL,
    password     VARCHAR2(50) NOT NULL,
    role_id      INTEGER
);

ALTER TABLE users ADD CONSTRAINT users_pk PRIMARY KEY ( user_id );

ALTER TABLE classes ADD CONSTRAINT classes_courses_fk FOREIGN KEY ( courses_id ) REFERENCES courses ( course_id );

ALTER TABLE course_req ADD CONSTRAINT course_req_req_fk FOREIGN KEY ( req_id ) REFERENCES completion_req ( req_id );
ALTER TABLE course_req ADD CONSTRAINT course_req_courses_fk FOREIGN KEY ( courses_id ) REFERENCES courses ( course_id );

ALTER TABLE course_reg ADD CONSTRAINT course_reg_courses_fk FOREIGN KEY ( course_id ) REFERENCES courses ( course_id );

ALTER TABLE courses_lectures ADD CONSTRAINT courses_lectures_courses_fk FOREIGN KEY ( courses_id ) REFERENCES courses ( course_id );
ALTER TABLE courses_lectures ADD CONSTRAINT courses_lectures_lecturers_fk FOREIGN KEY ( user_id ) REFERENCES lecturers ( user_id );

ALTER TABLE courses_spec ADD CONSTRAINT courses_spec_courses_fk FOREIGN KEY ( courses_id ) REFERENCES courses ( course_id );
ALTER TABLE courses_spec ADD CONSTRAINT courses_spec_spec_fk FOREIGN KEY ( spec_id ) REFERENCES specializations ( spec_id );

ALTER TABLE grades ADD CONSTRAINT grades_courses_fk FOREIGN KEY ( courses_id ) REFERENCES courses ( course_id );
ALTER TABLE grades ADD CONSTRAINT grades_lecturers_fk FOREIGN KEY ( lecturer_id ) REFERENCES lecturers ( user_id );
ALTER TABLE grades ADD CONSTRAINT grades_students_fk FOREIGN KEY ( student_id ) REFERENCES students ( user_id );

ALTER TABLE semester_ects ADD CONSTRAINT semester_ects_spec_fk FOREIGN KEY ( spec_id ) REFERENCES specializations ( spec_id );

ALTER TABLE student_spec ADD CONSTRAINT student_spec_spec_fk FOREIGN KEY ( spec_id ) REFERENCES specializations ( spec_id );
ALTER TABLE student_spec ADD CONSTRAINT student_spec_students_fk FOREIGN KEY ( user_id ) REFERENCES students ( user_id );

ALTER TABLE users ADD CONSTRAINT users_roles_fk FOREIGN KEY ( role_id ) REFERENCES roles ( role_id );
