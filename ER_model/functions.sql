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