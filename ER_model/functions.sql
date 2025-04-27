create or replace function get_final_grade (
   p_student_id number,
   p_course_id  number
) return number is
   v_final_grade number := null;
begin
   select max(grade)
     into v_final_grade
     from grades
    where student_id = p_student_id
      and course_id = p_course_id
      and type like 'FINAL';

   return v_final_grade;
exception
   when no_data_found then
      return null;
end;

CREATE or REPLACE PROCEDURE get_grades_cross_section(
   p_course_id  number,
   p_grade_type  VARCHAR2,
   p_cursor      OUT SYS_REFCURSOR
) AS
BEGIN
   OPEN p_cursor FOR
   SELECT
      g.grade,
      COUNT(*) AS students_count
   FROM
      grades g
   WHERE
      g.type = p_grade_type
      and g.course_id = p_course_id
   GROUP BY
      g.grade
   ORDER BY
      g.grade DESC;
END;


CREATE or REPLACE PROCEDURE get_final_grades_distribution(
   p_course_id  number,
   p_cursor      OUT SYS_REFCURSOR
) AS
BEGIN
   OPEN p_cursor FOR
   SELECT
      CASE
         WHEN g.grade = 2 THEN 'failed'
         ELSE 'passed'
      END AS grade_status,
      COUNT(*) AS students_count
   FROM
      grades g
   WHERE
      g.type = 'FINAL'
      and g.course_id = p_course_id
   GROUP BY
      grade_status
   ORDER BY
      grade_status;
END;