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


-------------------------------------
-- TESTS TO TRIGGER add_avg_grade
-------------------------------------
-- TEST 1: User with role_id = 1 ➔ a row should be added to grades
BEGIN
  SAVEPOINT test1_start;

  INSERT INTO users (user_id, role_id, first_name)
  VALUES (99901, 1, 'test_student');

  DECLARE
    v_count NUMBER;
  BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM grades
    WHERE student_id = 99901
      AND type = 'AVG';

    IF v_count = 1 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: AVG grade added for student.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: AVG grade not added.');
    END IF;
  END;

  ROLLBACK TO test1_start;
END;
/


-- TEST 2: User with role_id ≠ 1 ➔ should not add an entry to grades
BEGIN
  SAVEPOINT test2_start;

  INSERT INTO users (user_id, role_id, first_name)
  VALUES (99902, 2, 'test_teacher');

  DECLARE
    v_count NUMBER;
  BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM grades
    WHERE student_id = 99902
      AND type = 'AVG';

    IF v_count = 0 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: No AVG grade created for non-student.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Unexpected AVG grade created.');
    END IF;
  END;

  ROLLBACK TO test2_start;
END;
/


-- TEST 3: Several users in a row ➔ generation of new grade_id should be handled correctly
BEGIN
  SAVEPOINT test3_start;

  INSERT INTO users (user_id, role_id, first_name)
  VALUES (99903, 1, 'student_one');

  INSERT INTO users (user_id, role_id, first_name)
  VALUES (99904, 1, 'student_two');

  DECLARE
    v_count NUMBER;
  BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM grades
    WHERE student_id IN (99903, 99904)
      AND type = 'AVG';

    IF v_count = 2 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: AVG grades correctly created for both students.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Expected 2 AVG grades, found ' || v_count || '.');
    END IF;
  END;

  ROLLBACK TO test3_start;
END;
/


-- TEST 4: Checking the correctness of field values ​​in grades (grade = 0, date set)
BEGIN
  SAVEPOINT test4_start;

  INSERT INTO users (user_id, role_id, first_name)
  VALUES (99905, 1, 'student_test');

  DECLARE
    v_grade NUMBER;
    v_date  DATE;
  BEGIN
    SELECT grade, received_date
    INTO v_grade, v_date
    FROM grades
    WHERE student_id = 99905
      AND type = 'AVG';

    IF v_grade = 0 AND v_date IS NOT NULL THEN
      DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED: AVG grade initialized correctly.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Grade = ' || v_grade || ', Date = ' || TO_CHAR(v_date, 'YYYY-MM-DD'));
    END IF;
  END;

  ROLLBACK TO test4_start;
END;
/


-----------------------------------------------
-- TESTS TO PROCEDURE close_course
-----------------------------------------------
-- TEST 1: Course exists, status 'active' ➔ must be closed successfully
BEGIN
  SAVEPOINT test1_start;

  INSERT INTO courses (course_id, status)
  VALUES (88801, 'active');

  BEGIN
    close_course(88801);
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: Unexpected error: ' || SQLERRM);
  END;

  DECLARE
    v_status courses.status%TYPE;
  BEGIN
    SELECT status INTO v_status
    FROM courses
    WHERE course_id = 88801;

    IF v_status = 'closed' THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: Course successfully closed.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: Course status = ' || v_status);
    END IF;
  END;

  ROLLBACK TO test1_start;
END;
/


-- TEST 2: The course exists, but the status is 'closed' ➔ there should be an error -20004
BEGIN
  SAVEPOINT test2_start;

  INSERT INTO courses (course_id, status)
  VALUES (88802, 'closed');

  BEGIN
    close_course(88802);
    DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Expected exception, but none was raised.');
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE = -20004 THEN
        DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: Correct exception - Course exists but not open.');
      ELSE
        DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Unexpected exception: ' || SQLERRM);
      END IF;
  END;

  ROLLBACK TO test2_start;
END;
/


-- TEST 3: The course does NOT exist ➔ there should be an error -20003
BEGIN
  SAVEPOINT test3_start;

  BEGIN
    close_course(99999);
    DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Expected exception, but none was raised.');
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE = -20003 THEN
        DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: Correct exception - Course does not exist.');
      ELSE
        DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Unexpected exception: ' || SQLERRM);
      END IF;
  END;

  ROLLBACK TO test3_start;
END;
/


--TEST 4: The course exists, status 'opened registration' ➔ there should be an error -20004
BEGIN
  SAVEPOINT test4_start;

  INSERT INTO courses (course_id, status)
  VALUES (88803, 'opened registration');

  BEGIN
    close_course(88803);
    DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Expected exception, but none was raised.');
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE = -20004 THEN
        DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED: Correct exception - Course exists but not open.');
      ELSE
        DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Unexpected exception: ' || SQLERRM);
      END IF;
  END;

  ROLLBACK TO test4_start;
END;
/

-------------------------------------
-- TESTS TO TRIGGER count_grade
-------------------------------------

-- TEST 1: Student scored less than min_score ➔ final grade = 2
BEGIN
  SAVEPOINT test1_start;

  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9001, 'Test Course 1', 6, 'active');

  INSERT INTO completion_requirements (completion_req_id, min_score, type)
  VALUES (6001, 30, 'exam');

  INSERT INTO course_requirement (course_id, completion_req_id)
  VALUES (9001, 6001);

  INSERT INTO grades (grade_id, student_id, course_id, grade, type)
  VALUES (grades_seq.NEXTVAL, 1001, 9001, 10, 'EXAM');

  UPDATE courses
  SET status = 'closed'
  WHERE course_id = 9001;

  DECLARE
    v_grade NUMBER;
  BEGIN
    SELECT grade INTO v_grade
    FROM grades
    WHERE student_id = 1001 AND course_id = 9001 AND type = 'FINAL';

    IF v_grade = 2 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: Student got grade 2.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: Unexpected final grade = ' || v_grade);
    END IF;
  END;

  ROLLBACK TO test1_start;
END;
/


-- TEST 2: The student scored exactly min_score ➔ score 3
BEGIN
  SAVEPOINT test2_start;

  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9002, 'Test Course 2', 6, 'active');

  INSERT INTO completion_requirements (completion_req_id, min_score, type)
  VALUES (6002, 25, 'exam');

  INSERT INTO course_requirement (course_id, completion_req_id)
  VALUES (9002, 6002);

  INSERT INTO grades (grade_id, student_id, course_id, grade, type)
  VALUES (grades_seq.NEXTVAL, 1002, 9002, 25, 'PROJECT');

  UPDATE courses
  SET status = 'closed'
  WHERE course_id = 9002;

  DECLARE
    v_grade NUMBER;
  BEGIN
    SELECT grade INTO v_grade
    FROM grades
    WHERE student_id = 1002 AND course_id = 9002 AND type = 'FINAL';

    IF v_grade = 3 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: Student got grade 3.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Unexpected final grade = ' || v_grade);
    END IF;
  END;

  ROLLBACK TO test2_start;
END;
/


-- TEST 3: The student scored the maximum ➔ final grade 5
BEGIN
  SAVEPOINT test3_start;

  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9003, 'Test Course 3', 6, 'active');

  INSERT INTO completion_requirements (completion_req_id, min_score, type)
  VALUES (6003, 20, 'exam');

  INSERT INTO course_requirement (course_id, completion_req_id)
  VALUES (9003, 6003);

  INSERT INTO grades (grade_id, student_id, course_id, grade, type)
  VALUES (grades_seq.NEXTVAL, 1003, 9003, 38, 'PROJECT');

  UPDATE courses
  SET status = 'closed'
  WHERE course_id = 9003;

  DECLARE
    v_grade NUMBER;
  BEGIN
    SELECT grade INTO v_grade
    FROM grades
    WHERE student_id = 1003 AND course_id = 9003 AND type = 'FINAL';

    IF v_grade = 5 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: Student got grade 5.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Unexpected final grade = ' || v_grade);
    END IF;
  END;

  ROLLBACK TO test3_start;
END;
/


-- TEST 4: No requirement ➔ ORA-20010 error
BEGIN
  SAVEPOINT test4_start;

  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9004, 'Test Course 4', 6, 'active');

  INSERT INTO grades (grade_id, student_id, course_id, grade, type)
  VALUES (grades_seq.NEXTVAL, 1004, 9004, 20, 'PROJECT');

  BEGIN
    UPDATE courses
    SET status = 'closed'
    WHERE course_id = 9004;
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE = -20010 THEN
        DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED: Correctly raised no requirement error.');
      ELSE
        DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Unexpected error code: ' || SQLCODE);
      END IF;
  END;

  ROLLBACK TO test4_start;
END;
/


-------------------------------------------
-- TESTS TO PROCEDURE close_registration
-------------------------------------------
-- TEST 1: Less than 10 applications ➔ course is closed (status = 'closed')
BEGIN
  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9001, 'Test Course 1', 5, 'active');

  INSERT INTO completion_requirements (completion_req_id, min_score, type)
  VALUES (10001, 20, 'exam');

  INSERT INTO course_requirement (course_id, completion_req_id)
  VALUES (9001, 10001);

  FOR i IN 1..5 LOOP
    INSERT INTO course_registrations (course_reg_id, student_id, course_id, status)
    VALUES (9000 + i, 1000 + i, 9001, 'application submitted');
  END LOOP;

  close_registration(9001);

  DECLARE
    v_status VARCHAR2(25);
  BEGIN
    SELECT status INTO v_status FROM courses WHERE course_id = 9001;

    IF v_status = 'closed' THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED: Course correctly closed.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: Course status is ' || v_status);
    END IF;
  END;
  
  delete from course_requirement where course_id = 9001;
  delete from course_registrations where course_id = 9001;
  delete from courses where course_id = 9001;
  delete from completion_requirements where completion_req_id = 10001;
  
  ROLLBACK;
END;
/


-- TEST 2: Exactly 10 applications ➔ course active (status = 'active')
BEGIN
  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9002, 'Test Course 2', 5, 'closed');

  INSERT INTO completion_requirements (completion_req_id, min_score, type)
  VALUES (10002, 20, 'exam');

  INSERT INTO course_requirement (course_id, completion_req_id)
  VALUES (9002, 10002);

  FOR i IN 1..10 LOOP
    INSERT INTO users (user_id)
    VALUES (2000 + i);
  END LOOP;

  FOR i IN 1..10 LOOP
    INSERT INTO students (user_id)
    VALUES (2000 + i);
  END LOOP;

  FOR i IN 1..10 LOOP
    INSERT INTO course_registrations (course_reg_id, student_id, course_id, status)
    VALUES (9200 + i, 2000 + i, 9002, 'application submitted');
  END LOOP;

  close_registration(9002);

  DECLARE
    v_status VARCHAR2(25);
  BEGIN
    SELECT status INTO v_status FROM courses WHERE course_id = 9002;

    IF v_status = 'active' THEN
      DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED: Course correctly activated.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: Course status is ' || v_status);
    END IF;
  END;
  delete from course_requirement where course_id = 9002;
  delete from course_registrations where course_id = 9002;
  delete from courses where course_id = 9002;
  delete from completion_requirements where completion_req_id = 10002;
  delete from students where user_id between 2001 and 2010;
  delete from users where user_id between 2001 and 2010;
END;
/


-- TEST 3: More than 10 applications ➔ course is active (status = 'active')
BEGIN
  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9003, 'Test Course 2', 5, 'closed');

  INSERT INTO completion_requirements (completion_req_id, min_score, type)
  VALUES (10003, 20, 'exam');

  INSERT INTO course_requirement (course_id, completion_req_id)
  VALUES (9003, 10003);
  
  FOR i IN 1..15 LOOP
    INSERT INTO users (user_id)
    VALUES (3000 + i);
  END LOOP;

  FOR i IN 1..15 LOOP
    INSERT INTO students (user_id)
    VALUES (3000 + i);
  END LOOP;

  FOR i IN 1..15 LOOP
    INSERT INTO course_registrations (course_reg_id, student_id, course_id, status)
    VALUES (9000 + i, 3000 + i, 9003, 'application submitted');
  END LOOP;

  EXECUTE IMMEDIATE 'BEGIN close_registration(9003); END;';

  DECLARE
    v_status VARCHAR2(25);
  BEGIN
    SELECT status INTO v_status FROM courses WHERE course_id = 9003;

    IF v_status = 'active' THEN
      DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED: Course correctly activated with >10 applications.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: Course status is ' || v_status);
    END IF;
  END;

  delete from course_requirement where course_id = 9003;
  delete from course_registrations where course_id = 9003;
  delete from courses where course_id = 9003;
  delete from completion_requirements where completion_req_id = 10003;
  delete from students where user_id between 3001 and 3015;
  delete from users where user_id between 3001 and 3015;
END;
/


-- TEST 4: There are no applications ➔ the course is closing (status = 'closed')
BEGIN

  INSERT INTO courses (course_id, name, ects_points, status)
  VALUES (9004, 'Test Course 4', 5, 'active');

  INSERT INTO completion_requirements (completion_req_id, min_score, type)
  VALUES (10004, 20, 'exam');

  INSERT INTO course_requirement (course_id, completion_req_id)
  VALUES (9004, 10004);

  EXECUTE IMMEDIATE 'BEGIN close_registration(9004); END;';

  DECLARE
    v_status VARCHAR2(25);
  BEGIN
    SELECT status INTO v_status FROM courses WHERE course_id = 9004;

    IF v_status = 'closed' THEN
      DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED: Course correctly closed with no applications.');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: Course status is ' || v_status);
    END IF;
  END;
  delete from course_requirement where course_id = 9004;
  delete from courses where course_id = 9004;
  delete from completion_requirements where completion_req_id = 10004;
END;
/


----------------------------------------
-- TESTS TO TRIGGER register_students
----------------------------------------
-- TEST 1: The base case is 10 students in the required specialty, all pass.
BEGIN
  SAVEPOINT test1_start;

  INSERT INTO courses (course_id, name, ects_points, status) VALUES (99101, 'Test Course 1', 5, 'closed');
  INSERT INTO specializations (specialization_id, name) VALUES (99501, 'TestSpec1');
  INSERT INTO courses_special (course_id, specialization_id) VALUES (99101, 99501);

  INSERT INTO completion_requirements (completion_req_id, min_score, type) VALUES (910001, 20, 'exam');
  INSERT INTO course_requirement (course_id, completion_req_id) VALUES (99101, 910001);

  FOR i IN 1..10 LOOP
    INSERT INTO users (user_id, role_id) VALUES (103000 + i, 1);
    INSERT INTO students (user_id) VALUES (103000 + i);
    INSERT INTO student_specializations (student_id, specialization_id) VALUES (103000 + i, 99501);
    INSERT INTO grades (grade_id, student_id, type, grade)
      VALUES (30+i, 103000 + i, 'AVG', 3 + MOD(i, 3));
    INSERT INTO course_registrations (course_reg_id, student_id, course_id, status)
      VALUES (109300 + i, 103000 + i, 99101, 'application submitted');
  END LOOP;

  UPDATE courses SET status = 'active' WHERE course_id = 99101;

  DECLARE
    v_cnt NUMBER;
  BEGIN
    SELECT COUNT(*) INTO v_cnt FROM course_registrations
    WHERE course_id = 99101 AND status = 'ACTIVE';

    IF v_cnt = 10 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 1 PASSED');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 1 FAILED: registered = ' || v_cnt);
    END IF;
  END;

  ROLLBACK TO test1_start;
END;
/


-- TEST 2: Students outside their specialty are excluded
BEGIN
  SAVEPOINT test2_start;

  INSERT INTO courses (course_id, name, ects_points, status) VALUES (99102, 'Test Course 2', 5, 'closed');
  INSERT INTO specializations (specialization_id, name) VALUES (99502, 'Spec2');
  INSERT INTO courses_special (course_id, specialization_id) VALUES (99102, 99502);

  FOR i IN 1..10 LOOP
    INSERT INTO users (user_id, role_id) VALUES (104000 + i, 1);
    INSERT INTO students (user_id) VALUES (104000 + i);
    INSERT INTO grades (grade_id, student_id, type, grade)
      VALUES (300 + i, 104000 + i, 'AVG', 3 + MOD(i, 2));
    INSERT INTO course_registrations (course_reg_id, student_id, course_id, status)
      VALUES (109400 + i, 104000 + i, 99102, 'application submitted');
  END LOOP;

  UPDATE courses SET status = 'active' WHERE course_id = 99102;

  DECLARE
    v_cnt NUMBER;
  BEGIN
    SELECT COUNT(*) INTO v_cnt FROM course_registrations
    WHERE course_id = 99102 AND status = 'ACTIVE';

    IF v_cnt = 10 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 2 PASSED (all are not registered in their specialty)');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 2 FAILED: were registered = ' || v_cnt);
    END IF;
  END;

  ROLLBACK TO test2_start;
END;
/


-- TEST 3: There are more students than the limit (20 people), the best are selected
BEGIN
  SAVEPOINT test3_start;

  INSERT INTO courses (course_id, name, ects_points, status) VALUES (99103, 'Test Course 3', 5, 'closed');
  INSERT INTO specializations (specialization_id, name) VALUES (99503, 'Spec3');
  INSERT INTO courses_special (course_id, specialization_id) VALUES (99103, 99503);

  FOR i IN 1..20 LOOP
    INSERT INTO users (user_id, role_id) VALUES (105000 + i, 1);
    INSERT INTO students (user_id) VALUES (105000 + i);
    INSERT INTO student_specializations (student_id, specialization_id) VALUES (105000 + i, 99503);
    INSERT INTO grades (grade_id, student_id, type, grade)
      VALUES (400 + i, 105000 + i, 'AVG', 2 + MOD(i, 4)); -- Разные оценки
    INSERT INTO course_registrations (course_reg_id, student_id, course_id, status)
      VALUES (109500 + i, 105000 + i, 99103, 'application submitted');
  END LOOP;

  UPDATE courses SET status = 'active' WHERE course_id = 99103;

  DECLARE
    v_cnt NUMBER;
  BEGIN
    SELECT COUNT(*) INTO v_cnt FROM course_registrations
    WHERE course_id = 99103 AND status = 'ACTIVE';

    IF v_cnt = 15 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 3 PASSED (the best 15 are selected)');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 3 FAILED: were registered = ' || v_cnt);
    END IF;
  END;

  ROLLBACK TO test3_start;
END;
/


-- TEST 4: The course falls into two specializations
BEGIN
  SAVEPOINT test4_start;

  INSERT INTO courses (course_id, name, ects_points, status) VALUES (99104, 'Test Course 4', 5, 'closed');
  INSERT INTO specializations (specialization_id, name) VALUES (99504, 'Spec4a');
  INSERT INTO specializations (specialization_id, name) VALUES (99505, 'Spec4b');
  INSERT INTO courses_special (course_id, specialization_id) VALUES (99104, 99504);
  INSERT INTO courses_special (course_id, specialization_id) VALUES (99104, 99505);

  FOR i IN 1..10 LOOP
    INSERT INTO users (user_id, role_id) VALUES (106000 + i, 1);
    INSERT INTO students (user_id) VALUES (106000 + i);
    IF MOD(i, 2) = 0 THEN
      INSERT INTO student_specializations (student_id, specialization_id) VALUES (106000 + i, 99504);
    ELSE
      INSERT INTO student_specializations (student_id, specialization_id) VALUES (106000 + i, 99505);
    END IF;
    INSERT INTO grades (grade_id, student_id, type, grade)
      VALUES (500 + i, 106000 + i, 'AVG', 3 + MOD(i, 2));
    INSERT INTO course_registrations (course_reg_id, student_id, course_id, status)
      VALUES (109600 + i, 106000 + i, 99104, 'application submitted');
  END LOOP;

  UPDATE courses SET status = 'active' WHERE course_id = 99104;

  DECLARE
    v_cnt NUMBER;
  BEGIN
    SELECT COUNT(*) INTO v_cnt FROM course_registrations
    WHERE course_id = 99104 AND status = 'ACTIVE';

    IF v_cnt = 10 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 4 PASSED (two specializations, all admitted)');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 4 FAILED: were registered = ' || v_cnt);
    END IF;
  END;

  ROLLBACK TO test4_start;
END;
/

-- TEST 5: There are no candidates (no one has applied)
BEGIN
  SAVEPOINT test5_start;

  INSERT INTO courses (course_id, name, ects_points, status) VALUES (99105, 'Test Course 5', 5, 'closed');
  INSERT INTO specializations (specialization_id, name) VALUES (99506, 'Spec5');
  INSERT INTO courses_special (course_id, specialization_id) VALUES (99105, 99506);

  UPDATE courses SET status = 'active' WHERE course_id = 99105;

  DECLARE
    v_cnt NUMBER;
  BEGIN
    SELECT COUNT(*) INTO v_cnt FROM course_registrations
    WHERE course_id = 99105 AND status = 'ACTIVE';

    IF v_cnt = 0 THEN
      DBMS_OUTPUT.PUT_LINE('TEST 5 PASSED (no one has applied)');
    ELSE
      DBMS_OUTPUT.PUT_LINE('TEST 5 FAILED: were registered = ' || v_cnt);
    END IF;
  END;

  ROLLBACK TO test5_start;
END;
/


commit;