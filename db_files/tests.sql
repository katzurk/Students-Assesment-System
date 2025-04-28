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


---------------------------------------------
-- TESTS TO PROCEDURE close_semester
---------------------------------------------

-- TEST 1: All courses are closed ('closed' or 'closed registration')
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'closed';

    BEGIN
      close_semester;
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: Semester closed successfully when all courses were closed.');
    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: Unexpected error: ' || SQLERRM);
    END;
  END;
  ROLLBACK TO test_start;
END;
/


-- TEST 2: One course 'opened' - there must be an error
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'closed';

    UPDATE courses
    SET status = 'opened'
    WHERE course_id = (SELECT MIN(course_id) FROM courses);

    BEGIN
      close_semester;
      DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Procedure should have raised an error due to opened course.');
    EXCEPTION
      WHEN OTHERS THEN
        IF SQLCODE = -20001 THEN
          DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: Correct error raised for opened course.');
        ELSE
          DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Unexpected error: ' || SQLERRM);
        END IF;
    END;
  END;
  ROLLBACK TO test_start;
END;
/


-- TEST 3: One course 'active' - there must be an error
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'closed';

    UPDATE courses
    SET status = 'active'
    WHERE course_id = (SELECT MIN(course_id) FROM courses);

    BEGIN
      close_semester;
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Procedure should have raised an error due to active course.');
    EXCEPTION
      WHEN OTHERS THEN
        IF SQLCODE = -20001 THEN
          DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: Correct error raised for active course.');
        ELSE
          DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Unexpected error: ' || SQLERRM);
        END IF;
    END;
  END;
  ROLLBACK TO test_start;
END;
/


-- TEST 4: One NULL course - the procedure must complete successfully
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'closed';

    UPDATE courses
    SET status = NULL
    WHERE course_id = (SELECT MIN(course_id) FROM courses);

    BEGIN
      close_semester;
      DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED: Semester closed successfully even with NULL status course.');
    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Unexpected error: ' || SQLERRM);
    END;
  END;
  ROLLBACK TO test_start;
END;
/


-- TEST 5: Mixed statuses: part 'closed', part 'closed registration', part 'opened'
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'closed';

    UPDATE courses
    SET status = 'closed registration'
    WHERE course_id = (SELECT MIN(course_id) FROM courses);

    UPDATE courses
    SET status = 'opened'
    WHERE course_id = (SELECT MAX(course_id) FROM courses);

    BEGIN
      close_semester;
      DBMS_OUTPUT.PUT_LINE('TEST 5 FAILED: Procedure should have raised an error due to opened course.');
    EXCEPTION
      WHEN OTHERS THEN
        IF SQLCODE = -20001 THEN
          DBMS_OUTPUT.PUT_LINE('TEST 5 PASSED: Correct error raised for mixed statuses.');
        ELSE
          DBMS_OUTPUT.PUT_LINE('TEST 5 FAILED: Unexpected error: ' || SQLERRM);
        END IF;
    END;
  END;
  ROLLBACK TO test_start;
END;
/


-- TEST 6: Mixed statuses: only 'closed' and 'closed registration'
BEGIN
  SAVEPOINT test_start;
  BEGIN
    UPDATE courses
    SET status = 'closed';

    UPDATE courses
    SET status = 'closed registration'
    WHERE MOD(course_id, 2) = 0; 

    BEGIN
      close_semester;
      DBMS_OUTPUT.PUT_LINE('TEST 6 PASSED: Semester closed successfully with closed and closed registration statuses.');
    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('TEST 6 FAILED: Unexpected error: ' || SQLERRM);
    END;
  END;

  ROLLBACK TO test_start;
END;
/


-----------------------------------------------------
-- TESTS TO TRIGGER update_avg_grade_after_closing  
-----------------------------------------------------

--TEST 1: Checking the recalculation of the average grade when there are several FINAL grades
BEGIN
  SAVEPOINT test1_start;

  INSERT INTO courses (course_id, status) VALUES (9991, 'closed registration');
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9991, 101, 9991, 80, 'FINAL');
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9992, 101, 9991, 90, 'FINAL');
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9993, 101, 9991, 0, 'AVG'); -- стартовое значение

  UPDATE courses
  SET status = 'closed'
  WHERE course_id = 9991;

  DECLARE
    v_avg_grade NUMBER;
  BEGIN
    SELECT grade INTO v_avg_grade
    FROM grades
    WHERE student_id = 101 AND type = 'AVG';
  
    IF v_avg_grade = 85 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: AVG grade correctly recalculated.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: AVG grade = ' || v_avg_grade);
    END IF;
  END;

  ROLLBACK TO test1_start;
END;
/

-- TEST 2: Student without FINAL grades - AVG should be 0
BEGIN
  SAVEPOINT test2_start;

  INSERT INTO courses (course_id, status) VALUES (9992, 'closed registration');
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9999, 102, 9992, 0, 'AVG');

  UPDATE courses
  SET status = 'closed'
  WHERE course_id = 9992;

  DECLARE
    v_avg_grade NUMBER;
  BEGIN
    SELECT grade INTO v_avg_grade
    FROM grades
    WHERE student_id = 102 AND type = 'AVG';

    IF v_avg_grade = 0 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: AVG grade correctly set to 0.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: AVG grade = ' || v_avg_grade);
    END IF;
  END;

  ROLLBACK TO test2_start;
END;
/


-- TEST 3: Student with one FINAL grade
BEGIN
  SAVEPOINT test3_start;

  INSERT INTO courses (course_id, status) VALUES (9993, 'closed registration');
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9999, 103, 9993, 75, 'FINAL');
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9998, 103, 9993, 0, 'AVG');

  UPDATE courses
  SET status = 'closed'
  WHERE course_id = 9993;

  DECLARE
    v_avg_grade NUMBER;
  BEGIN
    SELECT grade INTO v_avg_grade
    FROM grades
    WHERE student_id = 103 AND type = 'AVG';

    IF v_avg_grade = 75 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: AVG grade correctly set to FINAL grade.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: AVG grade = ' || v_avg_grade);
    END IF;
  END;

  ROLLBACK TO test3_start;
END;
/


-- TEST 4: Course closes but student is not associated with course - AVG should not change
BEGIN
  SAVEPOINT test4_start;

  INSERT INTO courses (course_id, status) VALUES (9994, 'closed registration');
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9999, 104, 2001, 70, 'FINAL'); -- другой курс
  INSERT INTO grades (grade_id, student_id, course_id, grade, type) VALUES (9998, 104, null, 0, 'AVG');

  DECLARE
    v_old_avg NUMBER;
  BEGIN
    SELECT grade INTO v_old_avg
    FROM grades
    WHERE student_id = 104 AND type = 'AVG';

    UPDATE courses
    SET status = 'closed'
    WHERE course_id = 9994;

    DECLARE
      v_new_avg NUMBER;
    BEGIN
      SELECT grade INTO v_new_avg
      FROM grades
      WHERE student_id = 104 AND type = 'AVG';

      IF v_old_avg = v_new_avg THEN
        DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED: Unrelated student AVG grade unchanged.');
      ELSE
        DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: AVG grade changed unexpectedly.');
      END IF;
    END;
  END;

  ROLLBACK TO test4_start;
END;
/