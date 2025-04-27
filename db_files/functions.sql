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
        SELECT DISTINCT student_id FROM grades;
    avg_grade NUMBER;
BEGIN
    FOR student_rec IN student_cursor LOOP
        BEGIN
            SELECT AVG(grade) INTO avg_grade
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
    IF :NEW.role_id = 1 THEN
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


commit;


