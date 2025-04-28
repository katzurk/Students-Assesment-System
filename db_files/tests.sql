SET SERVEROUTPUT ON;

------------------------------------------------------------
-- TESTS TO PROCEDURE open_semester
------------------------------------------------------------

-- TEST 1: open courses sucsessfull
BEGIN
  SAVEPOINT test1_start;

  UPDATE courses
  SET status = 'closed';

  UPDATE courses
  SET status = 'closed registration'
  WHERE course_id = (SELECT MIN(course_id) FROM courses);

  DELETE FROM course_registrations 
  WHERE course_id = (SELECT MIN(course_id) FROM courses);

  DECLARE
    CURSOR cur_students IS SELECT user_id FROM students WHERE ROWNUM <= 10;
    v_student_id students.user_id%TYPE;
    v_course_id courses.course_id%TYPE;
  BEGIN
    SELECT MIN(course_id) INTO v_course_id FROM courses;

    OPEN cur_students;
    LOOP
      FETCH cur_students INTO v_student_id;
      EXIT WHEN cur_students%NOTFOUND;

      INSERT INTO course_registrations (course_id, student_id)
      VALUES (v_course_id, v_student_id);
    END LOOP;
    CLOSE cur_students;
  END;

  open_semester;

  DECLARE
    v_status courses.status%TYPE;
    v_course_id courses.course_id%TYPE;
  BEGIN
    SELECT MIN(course_id) INTO v_course_id FROM courses;

    SELECT status INTO v_status
    FROM courses
    WHERE course_id = v_course_id;

    IF v_status = 'opened' THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: Course successfully opened.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: Course status = ' || v_status);
    END IF;
  END;

  ROLLBACK TO test1_start;
END;
/


-- TEST 2: error, there are not closed courses
BEGIN
  SAVEPOINT test2_start;

  UPDATE courses
  SET status = 'active'
  WHERE ROWNUM = 1;

  DECLARE
    v_bad_count NUMBER;
  BEGIN
    SELECT COUNT(*) INTO v_bad_count
    FROM courses
    WHERE status NOT IN ('closed', 'closed registration');
    DBMS_OUTPUT.PUT_LINE('DEBUG: Bad courses count = ' || v_bad_count);
  END;

  BEGIN
    open_semester;
    DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: No error raised.');
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE = -20001 THEN
        DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: Correct error raised.');
      ELSE
        DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Unexpected error: ' || SQLERRM);
      END IF;
  END;

  ROLLBACK TO test2_start;
END;
/


-- TEST 3: course is closed, not enough registrations (<10)
BEGIN
  SAVEPOINT test3_start;

  UPDATE courses
  SET status = 'closed';

  UPDATE courses
  SET status = 'closed registration'
  WHERE course_id = (SELECT MIN(course_id) FROM courses);

  DELETE FROM course_registrations WHERE course_id = (SELECT MIN(course_id) FROM courses);
  FOR i IN 1001..1005 LOOP
    INSERT INTO course_registrations (course_id, student_id)
    VALUES ((SELECT MIN(course_id) FROM courses), i);
  END LOOP;
  
  open_semester;

  DECLARE
    v_status VARCHAR2(50);
  BEGIN
    SELECT status INTO v_status
    FROM courses
    WHERE course_id = (SELECT MIN(course_id) FROM courses);

    IF v_status = 'closed registration' THEN
      DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: Course remained in ''closed registration''.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Course status = ' || v_status);
    END IF;
  END;

  ROLLBACK TO test3_start;
END;
/


----------------------------------------------
-- TESTS TO PROCEDURE open_registration
----------------------------------------------

-- TEST 1: Opening registration for a course with status 'closed'
BEGIN
  SAVEPOINT test_start;

  BEGIN
    UPDATE courses
    SET status = 'closed'
    WHERE course_id = 2001;

    open_registration(2001);

    DECLARE
      v_status courses.status%TYPE;
    BEGIN
      SELECT status INTO v_status
      FROM courses
      WHERE course_id = 2001;

      IF v_status = 'opened registration' THEN
        DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: Registration successfully opened for closed course.');
      ELSE
        DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: Course status is ' || v_status);
      END IF;
    END;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: ' || SQLERRM);
  END;
  ROLLBACK TO test_start;
END;
/


-- TEST 2: Attempt to open registration for a course with status 'opened registration' (should be rejected)
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'opened registration'
    WHERE course_id = 2002;

    BEGIN
      open_registration(2002);
      DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Registration should not be opened for already opened course.');
    EXCEPTION
      WHEN OTHERS THEN
        IF SQLCODE = -20002 THEN
          DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: Correct error raised for course already in opened registration.');
        ELSE
          DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Unexpected error: ' || SQLERRM);
        END IF;
    END;
  END;
  ROLLBACK TO test_start;
END;
/


-- TEST 3: Attempt to open registration for a course with status 'active' (should be rejected)
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'active'
    WHERE course_id = 2001;

    BEGIN
      open_registration(2001);
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Registration should not be opened for course with active status.');
    EXCEPTION
      WHEN OTHERS THEN
        IF SQLCODE = -20002 THEN
          DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: Correct error raised for active course.');
        ELSE
          DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Unexpected error: ' || SQLERRM);
        END IF;
    END;
  END;
  ROLLBACK TO test_start;
END;
/

-- TEST 4: Attempt to open registration for a non-existent course (should be rejected)
BEGIN
  SAVEPOINT test_start;
  BEGIN
    BEGIN
      open_registration(9999);
      DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Registration should not be opened for non-existing course.');
    EXCEPTION
      WHEN OTHERS THEN
        IF SQLCODE = -20003 THEN
          DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED: Correct error raised for non-existing course.');
        ELSE
          DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Unexpected error: ' || SQLERRM);
        END IF;
    END;
  END;

  ROLLBACK TO test_start;
END;
/