CREATE TABLE users (
    user_id INTEGER PRIMARY KEY,
    first_name VARCHAR2(50),
    last_name VARCHAR2(50),
    email VARCHAR2(100),
    password VARCHAR2(255),
    role_name VARCHAR2(255),
    academic_title VARCHAR2(50),
    student_number VARCHAR2(20) UNIQUE,
    status VARCHAR2(50),
    library_card_number VARCHAR2(20)
);

CREATE TABLE courses (
    course_id INTEGER PRIMARY KEY,
    name VARCHAR2(100) UNIQUE,
    ects_points INTEGER,
    status VARCHAR2(25)
);

CREATE TABLE courses_special (
    course_id INTEGER,
    specialization_id INTEGER,
    PRIMARY KEY (course_id, specialization_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (specialization_id) REFERENCES specializations(specialization_id) ON DELETE CASCADE
);

CREATE TABLE course_lecturers (
    course_id INTEGER,
    lecturer_id INTEGER,
    PRIMARY KEY (course_id, lecturer_id),
    CONSTRAINT course_lecturers_course_fk FOREIGN KEY (course_id) REFERENCES courses(course_id),
    CONSTRAINT course_lecturers_lecturer_fk FOREIGN KEY (lecturer_id) REFERENCES lecturers(user_id)
);

CREATE TABLE grades (
    grade_id INTEGER PRIMARY KEY,
    grade NUMBER(4,2),
    received_date DATE,
    lecturer_id INTEGER,
    course_id INTEGER,
    student_id INTEGER,
    type VARCHAR2(50),
    CONSTRAINT grades_lecturer_fk FOREIGN KEY (lecturer_id) REFERENCES lecturers(user_id),
    CONSTRAINT grades_course_fk FOREIGN KEY (course_id) REFERENCES courses(course_id),
    CONSTRAINT grades_student_fk FOREIGN KEY (student_id) REFERENCES students(user_id)
);

CREATE TABLE course_registrations (
    course_reg_id INTEGER PRIMARY KEY,
    status NUMBER,
    course_id INTEGER,
    student_id INTEGER,
    CONSTRAINT course_registrations_course_fk FOREIGN KEY (course_id) REFERENCES courses(course_id),
    CONSTRAINT course_registrations_student_fk FOREIGN KEY (student_id) REFERENCES students(user_id)
);

CREATE TABLE classes (
    class_id INTEGER PRIMARY KEY,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    day_of_week INTEGER,
    group_nr INTEGER,
    course_id INTEGER,
    classroom INTEGER,
    class_type VARCHAR2(50),
    CONSTRAINT classes_course_fk FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

CREATE TABLE specializations (
    specialization_id INTEGER PRIMARY KEY,
    name VARCHAR2(100)
);

CREATE TABLE student_specializations (
    specialization_id INTEGER,
    student_id INTEGER,
    PRIMARY KEY (specialization_id, student_id),
    CONSTRAINT student_specializations_spec_fk FOREIGN KEY (specialization_id) REFERENCES specializations(specialization_id),
    CONSTRAINT student_specializations_student_fk FOREIGN KEY (student_id) REFERENCES students(user_id)
);

CREATE TABLE completion_requirements (
    completion_req_id INTEGER PRIMARY KEY,
    min_score NUMBER,
    is_mandatory VARCHAR2(20)
);

CREATE TABLE course_requirement (
    course_id INTEGER,
    completion_req_id INTEGER,
    PRIMARY KEY (course_id, completion_req_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (completion_req_id) REFERENCES completion_requirements(completion_req_id) ON DELETE CASCADE
);

CREATE TABLE semester_ects (
    semester_ects_id INTEGER PRIMARY KEY,
    semester INTEGER,
    ects_amount INTEGER,
    specialization_id INTEGER,
    CONSTRAINT semester_ects_specialization_fk FOREIGN KEY (specialization_id) REFERENCES specializations(specialization_id)
);


