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
  WHERE status NOT IN ('closed', 'closed registration');

  IF cnt = 0 THEN
    UPDATE courses
    SET status = 'closed'
    WHERE status IN ('opened', 'closed_registration');

    FOR rec IN (SELECT DISTINCT student_id FROM grades) LOOP
        DECLARE
            avg_result NUMBER;
        BEGIN
            avg_result := count_avg_grade(rec.student_id);
            DBMS_OUTPUT.PUT_LINE('Student ID: ' || rec.student_id || ' | Final Average: ' || NVL(TO_CHAR(avg_result), 'No final grades'));
        END;
    END LOOP;

  ELSE
    RAISE_APPLICATION_ERROR(-20001, 'Semester can only be closed if all courses have status ''closed'' or ''closed registration''.');
  END IF;
END;
/

/*
BEGIN
  close_semester;
END;
/
*/

CREATE OR REPLACE FUNCTION count_avg_grade(p_student_id IN NUMBER)
RETURN NUMBER
IS
    avg_grade NUMBER;
BEGIN
    SELECT AVG(grade)
    INTO avg_grade
    FROM grades
    WHERE student_id = p_student_id
      AND type = 'FINAL';

    RETURN avg_grade;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL; 
END;
/

--SELECT count_avg_grade(1040) FROM dual;




