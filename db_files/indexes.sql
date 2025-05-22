CREATE INDEX idx_users_last_name ON users(last_name);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role_name ON users(role_name);
CREATE INDEX idx_u_email_role ON users(email, role_name);
CREATE INDEX idx_u_uid_role ON users(user_id, role_name);

CREATE INDEX idx_grades_type ON grades(type);
CREATE INDEX idx_grades_course_id ON grades(course_id);
CREATE INDEX idx_grades_student_id ON grades(student_id);
CREATE INDEX idx_grades_cid_type ON grades(course_id, type);
CREATE INDEX idx_grades_sid_cid ON grades(student_id, course_id);

CREATE INDEX idx_c_reg_student_id ON course_registrations(student_id);
CREATE INDEX idx_c_reg_course_id ON course_registrations(course_id);
CREATE INDEX idx_c_reg_status ON course_registrations(status);
CREATE INDEX idx_c_reg_sid_status ON course_registrations(student_id, status);

CREATE INDEX idx_courses_status ON courses(status);
CREATE INDEX idx_courses_cid_status ON courses(course_id, status);

CREATE INDEX idx_c_req_course_id ON completion_requirements(course_id);

CREATE INDEX idx_e_req_course_id ON enroll_requirements(course_id);
