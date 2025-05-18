/*UPDATE courses
SET status = 'closed'
WHERE course_id = 2001;*/

CREATE OR REPLACE PROCEDURE open_semester AS
    cnt NUMBER;
BEGIN
  SELECT COUNT(*) INTO cnt
  FROM courses
  WHERE status NOT IN ('closed', 'closed registration');

  IF cnt = 0 THEN
    UPDATE courses
    SET status = 'opened'
    WHERE status = 'closed registration'
    AND course_id IN (
      SELECT course_id
      FROM course_registrations
      GROUP BY course_id
      HAVING COUNT(*) >= 10
    );
  ELSE
    RAISE_APPLICATION_ERROR(-20001, 'Semester can only be opened if all courses have status ''closed'' or ''closed registration''.');
  END IF;
END;
/

/*
BEGIN
  open_semester;
END;
/
*/



CREATE OR REPLACE PROCEDURE open_registration(p_course_id IN NUMBER) AS
  v_status courses.status%TYPE;
BEGIN
  SELECT status INTO v_status
  FROM courses
  WHERE course_id = p_course_id;

  IF v_status = 'closed' THEN
    UPDATE courses
    SET status = 'opened registration'
    WHERE course_id = p_course_id;
  ELSE
    DBMS_OUTPUT.PUT_LINE('Registration can only be opened for courses with status ''closed''.');
  END IF;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('Course not found.');
END;
/

/*
BEGIN
  open_registration(2101);
END;
/
*/


CREATE OR REPLACE PROCEDURE close_semester AS
    cnt NUMBER;
BEGIN
  SELECT COUNT(*) INTO cnt
  FROM courses
  WHERE status NOT IN ('closed', 'closed registration', 'null');

  IF cnt = 0 THEN
    UPDATE courses
    SET status = 'closed'
    WHERE status IN ('opened', 'closed_registration');
  ELSE
    RAISE_APPLICATION_ERROR(-20001, 'Semester can only be closed if all courses have status ''closed'' or ''closed registration''.');
  END IF;
END;
/


/*BEGIN
  close_semester;
END;
/ */

CREATE OR REPLACE TRIGGER update_avg_grade_after_closing
AFTER UPDATE OF status ON courses
FOR EACH ROW
WHEN (NEW.status = 'closed')
DECLARE
    CURSOR student_cursor IS
        SELECT DISTINCT g.student_id
        FROM grades g
        WHERE g.course_id = :NEW.course_id;
    avg_grade NUMBER;
BEGIN
    FOR student_rec IN student_cursor LOOP
        BEGIN
            SELECT AVG(grade)
            INTO avg_grade
            FROM grades
            WHERE student_id = student_rec.student_id
              AND type = 'FINAL';

            UPDATE grades
            SET grade = NVL(avg_grade, 0)
            WHERE student_id = student_rec.student_id
              AND type = 'AVG';
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                NULL;
        END;
    END LOOP;
END;
/

CREATE OR REPLACE TRIGGER add_avg_grade
AFTER INSERT ON users
FOR EACH ROW
DECLARE
    new_grade_id NUMBER;
BEGIN
    IF :NEW.role_name = 'STUDENT' THEN
        SELECT NVL(MAX(grade_id), 0) + 1 INTO new_grade_id FROM grades;
        INSERT INTO grades(grade_id, student_id, type, grade, received_date)
        VALUES (new_grade_id, :NEW.user_id, 'AVG', 0, SYSDATE);
    END IF;
END;
/

/*ALTER TABLE grades DROP CONSTRAINT grades_student_fk;

ALTER TABLE grades ADD CONSTRAINT grades_student_fk
FOREIGN KEY (student_id) REFERENCES users(user_id)
DEFERRABLE INITIALLY DEFERRED;*/

--INSERT INTO users VALUES (1201, 'b', 'b', 'b.b@pw.edu.pl', 'p', 1);


CREATE OR REPLACE PROCEDURE close_course(p_course_id IN NUMBER) AS
    v_status courses.status%TYPE;
BEGIN
    SELECT status INTO v_status
    FROM courses
    WHERE course_id = p_course_id;

    IF v_status != 'active' THEN
        RAISE_APPLICATION_ERROR(-20004, 'Course exists but is not open');
    ELSE
        UPDATE courses
        SET status = 'closed'
        WHERE course_id = p_course_id;

        DBMS_OUTPUT.PUT_LINE('Course was successfully closed.');
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20003, 'Course does not exist');
END;
/

/*BEGIN
  close_course(41);
END;
/ */

CREATE OR REPLACE TRIGGER count_grade
AFTER UPDATE OF status ON courses
FOR EACH ROW
WHEN (OLD.status = 'active' AND NEW.status = 'closed')
DECLARE
    CURSOR c_students IS
        SELECT student_id, SUM(grade) AS total_points
        FROM grades
        WHERE course_id = :NEW.course_id
          AND type != 'FINAL'
        GROUP BY student_id;

    v_min_score completion_requirements.min_score%TYPE;
    v_max_score NUMBER;
    v_step NUMBER;
    v_final_grade NUMBER;
BEGIN
    BEGIN
        SELECT cr.min_score
        INTO v_min_score
        FROM completion_requirements cr
        WHERE cr.course_id = :NEW.course_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20010, 'No completion requirement defined for this course.');
    END;

    v_max_score := (v_min_score - 1) * 2;
    v_step := v_max_score / 10;

    FOR r IN c_students LOOP
        DELETE FROM grades
        WHERE student_id = r.student_id
          AND course_id = :NEW.course_id
          AND type = 'FINAL';

        IF r.total_points < v_min_score THEN
            v_final_grade := 2;
        ELSE
            v_final_grade := 3 + FLOOR((r.total_points - v_min_score) / v_step) * 0.5;

            IF v_final_grade > 5 THEN
                v_final_grade := 5;
            END IF;
        END IF;

        INSERT INTO grades (grade_id, student_id, course_id, grade, type)
        VALUES (grades_seq.NEXTVAL, r.student_id, :NEW.course_id, v_final_grade, 'FINAL');
    END LOOP;
END;
/

/*
drop sequence grades_seq;

CREATE SEQUENCE grades_seq
    START WITH 700
    INCREMENT BY 1;

UPDATE courses
SET status = 'active'
WHERE course_id = 2082;

delete from grades where course_id = 2082 and type = 'FINAL';

INSERT INTO completion_requirements (course_id, completion_req_id, min_score, type)
VALUES (2082, 2082, 10, 'exam');

*/

CREATE OR REPLACE PROCEDURE close_registration(p_course_id IN courses.course_id%TYPE)
IS
    v_application_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_application_count
    FROM course_registrations
    WHERE course_id = p_course_id
      AND status = 'application submitted';

    IF v_application_count < 10 THEN
        UPDATE courses
        SET status = 'closed'
        WHERE course_id = p_course_id;
    ELSE
        UPDATE courses
        SET status = 'active'
        WHERE course_id = p_course_id;
    END IF;

    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20020, 'Course not found.');
END;
/


CREATE OR REPLACE TRIGGER register_students
AFTER UPDATE OF status ON courses
FOR EACH ROW
WHEN (OLD.status != 'opened registration' AND NEW.status = 'active')
DECLARE
    c_max_students CONSTANT NUMBER := 15;

    v_course_id courses.course_id%TYPE := :NEW.course_id;
    v_registered_ids SYS.OdciNumberList := SYS.OdciNumberList();
    v_total_registered NUMBER := 0;

    v_program_count INTEGER;
BEGIN
    SELECT COUNT(DISTINCT specialization)
    INTO v_program_count
    FROM courses_special
    WHERE course_id = v_course_id;

    DECLARE
        CURSOR c_spec IS
            SELECT cr.course_reg_id,
                   NVL((SELECT MAX(grade) FROM grades WHERE student_id = cr.student_id AND type = 'AVG'), 0) AS avg_grade
            FROM course_registrations cr
            WHERE cr.course_id = v_course_id
              AND cr.status = 'application submitted'
              AND EXISTS (
                  SELECT 1
                  FROM users u
                  JOIN courses_special cs ON u.specialization = cs.specialization
                  WHERE u.user_id = cr.student_id AND cs.course_id = cr.course_id
              )
            ORDER BY avg_grade DESC;

        i NUMBER := 0;
    BEGIN
        FOR rec IN c_spec LOOP
            EXIT WHEN v_total_registered = c_max_students;

            IF v_program_count > 1 OR v_total_registered < c_max_students THEN
                v_registered_ids.EXTEND;
                v_registered_ids(v_registered_ids.COUNT) := rec.course_reg_id;
                v_total_registered := v_total_registered + 1;
            END IF;
        END LOOP;
    END;

    DECLARE
        remaining NUMBER := c_max_students - v_total_registered;
    BEGIN
        IF remaining > 0 THEN
            FOR rec IN (
                SELECT cr.course_reg_id,
                       NVL((SELECT MAX(grade) FROM grades WHERE student_id = cr.student_id AND type = 'AVG'), 0) AS avg_grade
                FROM course_registrations cr
                WHERE cr.course_id = v_course_id
                  AND cr.status = 'application submitted'
                  AND NOT EXISTS (
                      SELECT 1
                      FROM users u
                      JOIN courses_special cs ON u.specialization = cs.specialization
                      WHERE u.user_id = cr.student_id AND cs.course_id = cr.course_id
                  )
                ORDER BY avg_grade DESC
            ) LOOP
                EXIT WHEN remaining = 0;
                v_registered_ids.EXTEND;
                v_registered_ids(v_registered_ids.COUNT) := rec.course_reg_id;
                remaining := remaining - 1;
            END LOOP;
        END IF;
    END;

    FOR i IN 1 .. v_registered_ids.COUNT LOOP
        UPDATE course_registrations
        SET status = 'ACTIVE'
        WHERE course_reg_id = v_registered_ids(i);
    END LOOP;

    UPDATE course_registrations
    SET status = 'application rejected'
    WHERE course_id = v_course_id
      AND status = 'application submitted'
      AND course_reg_id NOT IN (SELECT COLUMN_VALUE FROM TABLE(v_registered_ids));
END;
/


commit;


