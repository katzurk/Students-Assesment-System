## Analiza zapytań
1. 
```
select g1_0.grade_id,g1_0.course_id,g1_0.grade,g1_0.lecturer_id,g1_0.received_date,g1_0.student_id,g1_0.type
from grades g1_0
left join courses c1_0 on c1_0.course_id=g1_0.course_id
left join (select * from users t where t.role_name='STUDENT') s1_0 on s1_0.user_id=g1_0.student_id
where c1_0.course_id=:1 
order by s1_0.last_name;
```
Plan wykonania:
```
Plan hash value: 2794187858
 
------------------------------------------------------------------------------
| Id  | Operation           | Name   | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------
|   0 | SELECT STATEMENT    |        |    15 |   705 |     7  (15)| 00:00:01 |
|   1 |  SORT ORDER BY      |        |    15 |   705 |     7  (15)| 00:00:01 |
|*  2 |   HASH JOIN OUTER   |        |    15 |   705 |     6   (0)| 00:00:01 |
|*  3 |    TABLE ACCESS FULL| GRADES |    15 |   420 |     3   (0)| 00:00:01 |
|*  4 |    TABLE ACCESS FULL| USERS  |   166 |  3154 |     3   (0)| 00:00:01 |
------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("T"."USER_ID"(+)="G1_0"."STUDENT_ID")
   3 - filter("G1_0"."COURSE_ID"=TO_NUMBER(:1))
   4 - filter("T"."ROLE_NAME"(+)='STUDENT')
 
Note
-----
   - this is an adaptive plan
   - Failed to use SQL plan baseline for this statement
```

2. 
```
SELECT
GRADE_STATUS, COUNT(*) AS STUDENTS_COUNT
FROM
( SELECT CASE WHEN G.GRADE = 2 THEN 'failed' ELSE 'passed' END AS GRADE_STATUS FROM GRADES G WHERE G.TYPE = 'FINAL' AND G.COURSE_ID = :B1 )
GROUP BY GRADE_STATUS
ORDER BY GRADE_STATUS;
```

Plan wykonania:
```
Plan hash value: 1979537127
 
------------------------------------------------------------------------------
| Id  | Operation           | Name   | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------
|   0 | SELECT STATEMENT    |        |     3 |    33 |     5  (40)| 00:00:01 |
|   1 |  SORT ORDER BY      |        |     3 |    33 |     5  (40)| 00:00:01 |
|   2 |   HASH GROUP BY     |        |     3 |    33 |     5  (40)| 00:00:01 |
|*  3 |    TABLE ACCESS FULL| GRADES |     4 |    44 |     3   (0)| 00:00:01 |
------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   3 - filter("G"."TYPE"='FINAL' AND "G"."COURSE_ID"=TO_NUMBER(:B1))
```

3. 
```
select
c1_0.course_id,c1_0.ects_points,c1_0.name,c1_0.status,cr1_0.status
from courses c1_0
left join 
(course_registrations cr1_0 left join users s1_0 on s1_0.user_id=cr1_0.student_id) on cr1_0.course_id=c1_0.course_id and s1_0.email=:1 
where c1_0.status='opened' 
order by c1_0.name;
```

Plan wykonania:
```
Plan hash value: 1800091024
 
----------------------------------------------------------------------------------------------
| Id  | Operation             | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT      |                      |     1 |   560 |    10  (10)| 00:00:01 |
|   1 |  SORT ORDER BY        |                      |     1 |   560 |    10  (10)| 00:00:01 |
|*  2 |   HASH JOIN OUTER     |                      |     1 |   560 |     9   (0)| 00:00:01 |
|*  3 |    TABLE ACCESS FULL  | COURSES              |     1 |    44 |     3   (0)| 00:00:01 |
|   4 |    VIEW               |                      |     1 |   516 |     6   (0)| 00:00:01 |
|*  5 |     HASH JOIN         |                      |     1 |    50 |     6   (0)| 00:00:01 |
|*  6 |      TABLE ACCESS FULL| USERS                |     1 |    22 |     3   (0)| 00:00:01 |
|   7 |      TABLE ACCESS FULL| COURSE_REGISTRATIONS |    83 |  2324 |     3   (0)| 00:00:01 |
----------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("CR1_0"."COURSE_ID"(+)="C1_0"."COURSE_ID")
   3 - filter("C1_0"."STATUS"='opened')
   5 - access("S1_0"."USER_ID"="CR1_0"."STUDENT_ID")
   6 - filter("S1_0"."EMAIL"=:1)
```
4. 
```
select c1_0.course_id,cr1_0.course_id,cr1_0.completion_req_id,cr1_0.min_score,cr1_0.type,c1_0.ects_points,er1_0.course_id,er1_0.reg_id,cc1_0.course_id,cr2_0.course_id,cr2_0.completion_req_id,cr2_0.min_score,cr2_0.type,cc1_0.ects_points,cc1_0.name,s1_0.course_id,s1_0.specialization,cc1_0.status,er1_0.min_ects,c1_0.name,s2_0.course_id,s2_0.specialization,c1_0.status
from courses c1_0
left join completion_requirements cr1_0 on c1_0.course_id=cr1_0.course_id
left join enroll_requirements er1_0 on c1_0.course_id=er1_0.course_id
left join courses cc1_0 on cc1_0.course_id=er1_0.complited_course_id
left join completion_requirements cr2_0 on cc1_0.course_id=cr2_0.course_id
left join courses_special s1_0 on cc1_0.course_id=s1_0.course_id
left join courses_special s2_0 on c1_0.course_id=s2_0.course_id
where c1_0.course_id=:1;
```

Plan wykonania:
```
Plan hash value: 488604041
 
-------------------------------------------------------------------------------------------------------------
| Id  | Operation                         | Name                    | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                  |                         |     2 |   342 |    13   (0)| 00:00:01 |
|*  1 |  HASH JOIN OUTER                  |                         |     2 |   342 |    13   (0)| 00:00:01 |
|   2 |   NESTED LOOPS OUTER              |                         |     1 |   156 |    10   (0)| 00:00:01 |
|   3 |    NESTED LOOPS OUTER             |                         |     1 |   134 |     9   (0)| 00:00:01 |
|   4 |     MERGE JOIN OUTER              |                         |     1 |    90 |     8   (0)| 00:00:01 |
|*  5 |      HASH JOIN OUTER              |                         |     1 |    68 |     7   (0)| 00:00:01 |
|   6 |       NESTED LOOPS OUTER          |                         |     1 |    59 |     4   (0)| 00:00:01 |
|   7 |        TABLE ACCESS BY INDEX ROWID| COURSES                 |     1 |    44 |     1   (0)| 00:00:01 |
|*  8 |         INDEX UNIQUE SCAN         | SYS_C0055624            |     1 |       |     0   (0)| 00:00:01 |
|*  9 |        TABLE ACCESS FULL          | COMPLETION_REQUIREMENTS |     1 |    15 |     3   (0)| 00:00:01 |
|* 10 |       TABLE ACCESS FULL           | ENROLL_REQUIREMENTS     |     1 |     9 |     3   (0)| 00:00:01 |
|  11 |      BUFFER SORT                  |                         |     1 |    22 |     5   (0)| 00:00:01 |
|* 12 |       INDEX RANGE SCAN            | SYS_C0056657            |     1 |    22 |     1   (0)| 00:00:01 |
|  13 |     TABLE ACCESS BY INDEX ROWID   | COURSES                 |     1 |    44 |     1   (0)| 00:00:01 |
|* 14 |      INDEX UNIQUE SCAN            | SYS_C0055624            |     1 |       |     0   (0)| 00:00:01 |
|* 15 |    INDEX RANGE SCAN               | SYS_C0056657            |     1 |    22 |     1   (0)| 00:00:01 |
|  16 |   TABLE ACCESS FULL               | COMPLETION_REQUIREMENTS |    16 |   240 |     3   (0)| 00:00:01 |
-------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - access("CC1_0"."COURSE_ID"="CR2_0"."COURSE_ID"(+))
   5 - access("C1_0"."COURSE_ID"="ER1_0"."COURSE_ID"(+))
   8 - access("C1_0"."COURSE_ID"=TO_NUMBER(:1))
   9 - filter("CR1_0"."COURSE_ID"(+)=TO_NUMBER(:1))
  10 - filter("ER1_0"."COURSE_ID"(+)=TO_NUMBER(:1))
  12 - access("S2_0"."COURSE_ID"(+)=TO_NUMBER(:1))
  14 - access("CC1_0"."COURSE_ID"(+)="ER1_0"."COMPLITED_COURSE_ID")
  15 - access("CC1_0"."COURSE_ID"="S1_0"."COURSE_ID"(+))
 
Note
-----
   - this is an adaptive plan
```
5. 
```
SELECT 
G.GRADE, COUNT(*) AS STUDENTS_COUNT
FROM GRADES G 
WHERE G.TYPE = :B2 AND G.COURSE_ID = :B1 
GROUP BY G.GRADE 
ORDER BY G.GRADE DESC;
```

Plan wykonania:
```
Plan hash value: 880012138
 
-----------------------------------------------------------------------------
| Id  | Operation          | Name   | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |        |     2 |    22 |     4  (25)| 00:00:01 |
|   1 |  SORT GROUP BY     |        |     2 |    22 |     4  (25)| 00:00:01 |
|*  2 |   TABLE ACCESS FULL| GRADES |     2 |    22 |     3   (0)| 00:00:01 |
-----------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - filter("G"."TYPE"=:B2 AND "G"."COURSE_ID"=TO_NUMBER(:B1))
```
6. 
```
SELECT
* FROM course_registrations
WHERE student_id = :1 AND status LIKE :2;
```

Plan wykonania:
```
Plan hash value: 1997091296
 
------------------------------------------------------------------------------------------
| Id  | Operation         | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT  |                      |     1 |    31 |     3   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS FULL| COURSE_REGISTRATIONS |     1 |    31 |     3   (0)| 00:00:01 |
------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("STATUS"='opened' AND "STUDENT_ID"=1001)
```
7. 
```
select s1_0.user_id,s1_0.email,s1_0.first_name,s1_0.last_name,s1_0.password,s1_0.library_card_number,s1_0.specialization,s1_0.status,s1_0.student_number from users s1_0 where s1_0.role_name='STUDENT' and s1_0.user_id=:1;
```

Plan wykonania:
```
Plan hash value: 2865859890
 
--------------------------------------------------------------------------------------------
| Id  | Operation                   | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT            |              |     1 |    72 |     1   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS BY INDEX ROWID| USERS        |     1 |    72 |     1   (0)| 00:00:01 |
|*  2 |   INDEX UNIQUE SCAN         | SYS_C0055617 |     1 |       |     0   (0)| 00:00:01 |
--------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("S1_0"."ROLE_NAME"='STUDENT')
   2 - access("S1_0"."USER_ID"=TO_NUMBER(:1))
```
8. 
```
select s1_0.user_id,s1_0.email,s1_0.first_name,s1_0.last_name,s1_0.password,s1_0.library_card_number,s1_0.specialization,s1_0.status,s1_0.student_number
from users s1_0
where s1_0.email=:1 and s1_0.role_name='STUDENT';
```

Plan wykonania:
```
Plan hash value: 3461732445
 
---------------------------------------------------------------------------
| Id  | Operation         | Name  | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------
|   0 | SELECT STATEMENT  |       |     1 |    72 |     3   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS FULL| USERS |     1 |    72 |     3   (0)| 00:00:01 |
---------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("S1_0"."EMAIL"=:1 AND "S1_0"."ROLE_NAME"='STUDENT')
```

9. 
```
select u1_0.user_id,u1_0.role_name,u1_0.email,u1_0.first_name,u1_0.last_name,u1_0.passwordu1_0.academic_title,u1_0.library_card_number,u1_0.specialization,u1_0.status,u1_0.student_number
from users u1_0
where u1_0.email=:1;
```

Plan wykonania:
```
Plan hash value: 3461732445
 
---------------------------------------------------------------------------
| Id  | Operation         | Name  | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------
|   0 | SELECT STATEMENT  |       |     1 |    72 |     3   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS FULL| USERS |     1 |    72 |     3   (0)| 00:00:01 |
---------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("U1_0"."EMAIL"=:1)
```
10. 
```
select g1_0.grade_id,g1_0.course_id,g1_0.grade,g1_0.lecturer_id,g1_0.received_date,g1_0.student_id,g1_0.type 
from grades g1_0 
left join (select * from users t where t.role_name='STUDENT') s1_0 on s1_0.user_id=g1_0.student_id 
left join courses c1_0 on c1_0.course_id=g1_0.course_id 
where s1_0.email=:1 and c1_0.course_id=:2;
```

Plan wykonania:
```
Plan hash value: 1077204302
 
-----------------------------------------------------------------------------
| Id  | Operation          | Name   | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |        |     1 |    58 |     6   (0)| 00:00:01 |
|*  1 |  HASH JOIN         |        |     1 |    58 |     6   (0)| 00:00:01 |
|*  2 |   TABLE ACCESS FULL| USERS  |     1 |    30 |     3   (0)| 00:00:01 |
|*  3 |   TABLE ACCESS FULL| GRADES |    15 |   420 |     3   (0)| 00:00:01 |
-----------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - access("T"."USER_ID"="G1_0"."STUDENT_ID")
   2 - filter("T"."EMAIL"=:1 AND "T"."ROLE_NAME"='STUDENT')
   3 - filter("G1_0"."COURSE_ID"=TO_NUMBER(:2))
```
11. 
```
select er1_0.course_id,er1_0.reg_id,cc1_0.course_id,cc1_0.ects_points,cc1_0.name,cc1_0.status,er1_0.min_ects from enroll_requirements er1_0 
left join courses cc1_0 on cc1_0.course_id=er1_0.complited_course_id 
where er1_0.course_id=:1
```

Plan wykonania: EDIT
```
Plan hash value: 1664145947
 
----------------------------------------------------------------------------------------------------
| Id  | Operation                    | Name                | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT             |                     |     1 |    53 |     4   (0)| 00:00:01 |
|   1 |  NESTED LOOPS OUTER          |                     |     1 |    53 |     4   (0)| 00:00:01 |
|*  2 |   TABLE ACCESS FULL          | ENROLL_REQUIREMENTS |     1 |     9 |     3   (0)| 00:00:01 |
|   3 |   TABLE ACCESS BY INDEX ROWID| COURSES             |     1 |    44 |     1   (0)| 00:00:01 |
|*  4 |    INDEX UNIQUE SCAN         | SYS_C0055624        |     1 |       |     0   (0)| 00:00:01 |
----------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - filter("ER1_0"."COURSE_ID"=TO_NUMBER(:1))
   4 - access("CC1_0"."COURSE_ID"(+)="ER1_0"."COMPLITED_COURSE_ID")
```
12. 
```
select l1_0.user_id,l1_0.email,l1_0.first_name,l1_0.last_name,l1_0.password,l1_0.academic_title
from users l1_0 
where l1_0.role_name='LECTURER' and l1_0.user_id=:1;
```

Plan wykonania:
```
Plan hash value: 2865859890
 
--------------------------------------------------------------------------------------------
| Id  | Operation                   | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT            |              |     1 |    55 |     1   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS BY INDEX ROWID| USERS        |     1 |    55 |     1   (0)| 00:00:01 |
|*  2 |   INDEX UNIQUE SCAN         | SYS_C0055617 |     1 |       |     0   (0)| 00:00:01 |
--------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("L1_0"."ROLE_NAME"='LECTURER')
   2 - access("L1_0"."USER_ID"=TO_NUMBER(:1))
```
13. 
```
select 
c1_0.course_id,c1_0.ects_points,c1_0.name,c1_0.status 
from course_registrations cr1_0 
join courses c1_0 on c1_0.course_id=cr1_0.course_id 
join users s1_0 on s1_0.user_id=cr1_0.student_id 
where s1_0.email=:1 and cr1_0.status=:2;
```

Plan wykonania:
```
Plan hash value: 2519083869
 
-----------------------------------------------------------------------------------------------------
| Id  | Operation                    | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT             |                      |     1 |    94 |     7   (0)| 00:00:01 |
|   1 |  NESTED LOOPS                |                      |     1 |    94 |     7   (0)| 00:00:01 |
|   2 |   NESTED LOOPS               |                      |     1 |    94 |     7   (0)| 00:00:01 |
|*  3 |    HASH JOIN                 |                      |     1 |    50 |     6   (0)| 00:00:01 |
|*  4 |     TABLE ACCESS FULL        | USERS                |     1 |    22 |     3   (0)| 00:00:01 |
|*  5 |     TABLE ACCESS FULL        | COURSE_REGISTRATIONS |    37 |  1036 |     3   (0)| 00:00:01 |
|*  6 |    INDEX UNIQUE SCAN         | SYS_C0055624         |     1 |       |     0   (0)| 00:00:01 |
|   7 |   TABLE ACCESS BY INDEX ROWID| COURSES              |     1 |    44 |     1   (0)| 00:00:01 |
-----------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   3 - access("S1_0"."USER_ID"="CR1_0"."STUDENT_ID")
   4 - filter("S1_0"."EMAIL"=:1)
   5 - filter("CR1_0"."STATUS"=:2)
   6 - access("C1_0"."COURSE_ID"="CR1_0"."COURSE_ID")
 
Note
-----
   - this is an adaptive plan
```
14. 
```
select 
cr1_0.course_id,cr1_0.completion_req_id,cr1_0.min_score,cr1_0.type 
from completion_requirements cr1_0 
where cr1_0.course_id=:1; 
```

Plan wykonania:
```
Plan hash value: 3471475423
 
---------------------------------------------------------------------------------------------
| Id  | Operation         | Name                    | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT  |                         |     1 |    15 |     3   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS FULL| COMPLETION_REQUIREMENTS |     1 |    15 |     3   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("CR1_0"."COURSE_ID"=TO_NUMBER(:1))
```
15. 
```
SELECT 
MAX(GRADE) FROM GRADES 
WHERE STUDENT_ID = :B2 AND COURSE_ID = :B1 AND TYPE LIKE 'FINAL';
```

Plan wykonania:
```
Plan hash value: 3552239597
 
-----------------------------------------------------------------------------
| Id  | Operation          | Name   | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |        |     1 |    16 |     3   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE    |        |     1 |    16 |            |          |
|*  2 |   TABLE ACCESS FULL| GRADES |     1 |    16 |     3   (0)| 00:00:01 |
-----------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - filter("TYPE"='FINAL' AND "STUDENT_ID"=TO_NUMBER(:B2) AND 
              "COURSE_ID"=TO_NUMBER(:B1))
 
Note
-----
   - SQL plan baseline "SQL_PLAN_4xg0q0mpkn91kf17b0747" used for this statement
```
16. 
```
select 
s1_0.course_id,s1_0.specialization 
from courses_special s1_0 
where s1_0.course_id=:1;
```

Plan wykonania:
```
Plan hash value: 3760445940
 
---------------------------------------------------------------------------------
| Id  | Operation        | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------
|   0 | SELECT STATEMENT |              |     1 |    22 |     1   (0)| 00:00:01 |
|*  1 |  INDEX RANGE SCAN| SYS_C0056657 |     1 |    22 |     1   (0)| 00:00:01 |
---------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - access("S1_0"."COURSE_ID"=TO_NUMBER(:1))
```
17. 
```
select c1_0.course_id,cr1_0.course_id,cr1_0.completion_req_id,cr1_0.min_score,cr1_0.type,c1_0.ects_points,er1_0.course_id,er1_0.reg_id,er1_0.complited_course_id,er1_0.min_ects,c1_0.name,s1_0.course_id,s1_0.specialization,c1_0.status 
from courses c1_0 
left join completion_requirements cr1_0 on c1_0.course_id=cr1_0.course_id 
left join enroll_requirements er1_0 on c1_0.course_id=er1_0.course_id 
left join courses_special s1_0 on c1_0.course_id=s1_0.course_id 
order by c1_0.name
```

Plan wykonania:
```
Plan hash value: 1132265228
 
---------------------------------------------------------------------------------------------------
| Id  | Operation               | Name                    | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT        |                         |   149 | 13410 |    11  (10)| 00:00:01 |
|   1 |  SORT ORDER BY          |                         |   149 | 13410 |    11  (10)| 00:00:01 |
|*  2 |   HASH JOIN RIGHT OUTER |                         |   149 | 13410 |    10   (0)| 00:00:01 |
|*  3 |    TABLE ACCESS FULL    | COMPLETION_REQUIREMENTS |    15 |   225 |     3   (0)| 00:00:01 |
|*  4 |    HASH JOIN RIGHT OUTER|                         |   146 | 10950 |     7   (0)| 00:00:01 |
|   5 |     TABLE ACCESS FULL   | ENROLL_REQUIREMENTS     |     1 |     9 |     3   (0)| 00:00:01 |
|*  6 |     HASH JOIN OUTER     |                         |   146 |  9636 |     4   (0)| 00:00:01 |
|   7 |      TABLE ACCESS FULL  | COURSES                 |   137 |  6028 |     3   (0)| 00:00:01 |
|   8 |      INDEX FULL SCAN    | SYS_C0056657            |   108 |  2376 |     1   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("C1_0"."COURSE_ID"="CR1_0"."COURSE_ID"(+))
   3 - filter("CR1_0"."COURSE_ID"(+) IS NOT NULL)
   4 - access("C1_0"."COURSE_ID"="ER1_0"."COURSE_ID"(+))
   6 - access("C1_0"."COURSE_ID"="S1_0"."COURSE_ID"(+))
 
Note
-----
   - this is an adaptive plan
```
18. 
```
select g1_0.grade_id,c1_0.course_id,c1_0.ects_points,c1_0.name,c1_0.status,g1_0.grade,l1_0.user_id,l1_0.email,l1_0.first_name,l1_0.last_name,l1_0.password,l1_0.academic_title,g1_0.received_date,s1_0.user_id,s1_0.email,s1_0.first_name,s1_0.last_name,s1_0.password,s1_0.library_card_number,s1_0.specialization,s1_0.status,s1_0.student_number,g1_0.type 
from grades g1_0 
left join courses c1_0 on c1_0.course_id=g1_0.course_id 
left join (select * from users t where t.role_name='LECTURER') l1_0 on l1_0.user_id=g1_0.lecturer_id 
left join (select * from users t where t.role_name='STUDENT') s1_0 on s1_0.user_id=g1_0.student_id 
where g1_0.grade_id=:1 
```

Plan wykonania:
```
Plan hash value: 612061894
 
-----------------------------------------------------------------------------------------------
| Id  | Operation                      | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT               |              |     1 |   199 |     4   (0)| 00:00:01 |
|   1 |  NESTED LOOPS OUTER            |              |     1 |   199 |     4   (0)| 00:00:01 |
|   2 |   NESTED LOOPS OUTER           |              |     1 |   155 |     3   (0)| 00:00:01 |
|   3 |    NESTED LOOPS OUTER          |              |     1 |   100 |     2   (0)| 00:00:01 |
|   4 |     TABLE ACCESS BY INDEX ROWID| GRADES       |     1 |    28 |     1   (0)| 00:00:01 |
|*  5 |      INDEX UNIQUE SCAN         | SYS_C0055628 |     1 |       |     0   (0)| 00:00:01 |
|*  6 |     TABLE ACCESS BY INDEX ROWID| USERS        |     1 |    72 |     1   (0)| 00:00:01 |
|*  7 |      INDEX UNIQUE SCAN         | SYS_C0055617 |     1 |       |     0   (0)| 00:00:01 |
|*  8 |    TABLE ACCESS BY INDEX ROWID | USERS        |     1 |    55 |     1   (0)| 00:00:01 |
|*  9 |     INDEX UNIQUE SCAN          | SYS_C0055617 |     1 |       |     0   (0)| 00:00:01 |
|  10 |   TABLE ACCESS BY INDEX ROWID  | COURSES      |     1 |    44 |     1   (0)| 00:00:01 |
|* 11 |    INDEX UNIQUE SCAN           | SYS_C0055624 |     1 |       |     0   (0)| 00:00:01 |
-----------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   5 - access("G1_0"."GRADE_ID"=TO_NUMBER(:1))
   6 - filter("T"."ROLE_NAME"(+)='STUDENT')
   7 - access("T"."USER_ID"(+)="G1_0"."STUDENT_ID")
   8 - filter("T"."ROLE_NAME"(+)='LECTURER')
   9 - access("T"."USER_ID"(+)="G1_0"."LECTURER_ID")
  11 - access("C1_0"."COURSE_ID"(+)="G1_0"."COURSE_ID")
```
## Dodanie indeksów
- Indeksy na `users`: 
```
CREATE INDEX idx_users_last_name ON users(last_name);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role_name ON users(role_name);
CREATE INDEX idx_u_email_role ON users(email, role_name);
CREATE INDEX idx_u_uid_role ON users(user_id, role_name);
```

- Indeksy na `grades`: 
```
CREATE INDEX idx_grades_type ON grades(type);
CREATE INDEX idx_grades_course_id ON grades(course_id);
CREATE INDEX idx_grades_student_id ON grades(student_id);
CREATE INDEX idx_grades_cid_type ON grades(course_id, type);
CREATE INDEX idx_grades_sid_cid ON grades(student_id, course_id);
```

- Indeksy na `course_registrations`: 
```
CREATE INDEX idx_c_reg_student_id ON course_registrations(student_id);
CREATE INDEX idx_c_reg_course_id ON course_registrations(course_id);
CREATE INDEX idx_c_reg_status ON course_registrations(status);
CREATE INDEX idx_c_reg_sid_status ON course_registrations(student_id, status);
```

- Indeksy na `courses`: 
```
CREATE INDEX idx_courses_status ON courses(status);
CREATE INDEX idx_courses_cid_status ON courses(course_id, status);
```

- Indeksy na `completion_requirements`: 
```
CREATE INDEX idx_c_req_course_id ON completion_requirements(course_id);
```

- Indeksy na `enroll_requirements`: 
```
CREATE INDEX idx_e_req_course_id ON enroll_requirements(course_id);
```


## Po dodaniu indeksów
1. 
```
Plan hash value: 4077257545
 
-------------------------------------------------------------------------------------------------------------
| Id  | Operation                             | Name                | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                      |                     |    15 |   705 |     7  (15)| 00:00:01 |
|   1 |  SORT ORDER BY                        |                     |    15 |   705 |     7  (15)| 00:00:01 |
|*  2 |   HASH JOIN OUTER                     |                     |    15 |   705 |     6   (0)| 00:00:01 |
|   3 |    TABLE ACCESS BY INDEX ROWID BATCHED| GRADES              |    15 |   420 |     3   (0)| 00:00:01 |
|*  4 |     INDEX RANGE SCAN                  | IDX_GRADES_CID_TYPE |    15 |       |     1   (0)| 00:00:01 |
|*  5 |    TABLE ACCESS FULL                  | USERS               |   166 |  3154 |     3   (0)| 00:00:01 |
-------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("T"."USER_ID"(+)="G1_0"."STUDENT_ID")
   4 - access("G1_0"."COURSE_ID"=TO_NUMBER(:1))
   5 - filter("T"."ROLE_NAME"(+)='STUDENT')
 
Note
-----
   - this is an adaptive plan
   - Failed to use SQL plan baseline for this statement
```

2. 
```
Plan hash value: 3722564094
 
-------------------------------------------------------------------------------------------------------------
| Id  | Operation                             | Name                | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                      |                     |     3 |    33 |     4  (50)| 00:00:01 |
|   1 |  SORT ORDER BY                        |                     |     3 |    33 |     4  (50)| 00:00:01 |
|   2 |   HASH GROUP BY                       |                     |     3 |    33 |     4  (50)| 00:00:01 |
|   3 |    TABLE ACCESS BY INDEX ROWID BATCHED| GRADES              |     4 |    44 |     2   (0)| 00:00:01 |
|*  4 |     INDEX RANGE SCAN                  | IDX_GRADES_CID_TYPE |     4 |       |     1   (0)| 00:00:01 |
-------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   4 - access("G"."COURSE_ID"=TO_NUMBER(:B1) AND "G"."TYPE"='FINAL')
```

3. 
```
Plan hash value: 941601836
 
-----------------------------------------------------------------------------------------------------------------
| Id  | Operation                                | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                         |                      |     1 |   560 |     6  (17)| 00:00:01 |
|   1 |  SORT ORDER BY                           |                      |     1 |   560 |     6  (17)| 00:00:01 |
|*  2 |   HASH JOIN OUTER                        |                      |     1 |   560 |     5   (0)| 00:00:01 |
|   3 |    TABLE ACCESS BY INDEX ROWID BATCHED   | COURSES              |     1 |    44 |     2   (0)| 00:00:01 |
|*  4 |     INDEX RANGE SCAN                     | IDX_COURSES_STATUS   |     1 |       |     1   (0)| 00:00:01 |
|   5 |    VIEW                                  |                      |     1 |   516 |     3   (0)| 00:00:01 |
|   6 |     NESTED LOOPS                         |                      |     1 |    50 |     3   (0)| 00:00:01 |
|   7 |      NESTED LOOPS                        |                      |     1 |    50 |     3   (0)| 00:00:01 |
|   8 |       TABLE ACCESS BY INDEX ROWID BATCHED| USERS                |     1 |    22 |     2   (0)| 00:00:01 |
|*  9 |        INDEX RANGE SCAN                  | IDX_USERS_EMAIL      |     1 |       |     1   (0)| 00:00:01 |
|* 10 |       INDEX RANGE SCAN                   | IDX_C_REG_STUDENT_ID |     1 |       |     0   (0)| 00:00:01 |
|  11 |      TABLE ACCESS BY INDEX ROWID         | COURSE_REGISTRATIONS |     1 |    28 |     1   (0)| 00:00:01 |
-----------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("CR1_0"."COURSE_ID"(+)="C1_0"."COURSE_ID")
   4 - access("C1_0"."STATUS"='opened')
   9 - access("S1_0"."EMAIL"=:1)
  10 - access("S1_0"."USER_ID"="CR1_0"."STUDENT_ID")
 
Note
-----
   - this is an adaptive plan
```

4. 
```
Plan hash value: 486599840
 
---------------------------------------------------------------------------------------------------------------------
| Id  | Operation                                 | Name                    | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                          |                         |     2 |   342 |     7   (0)| 00:00:01 |
|   1 |  NESTED LOOPS OUTER                       |                         |     2 |   342 |     7   (0)| 00:00:01 |
|   2 |   MERGE JOIN OUTER                        |                         |     1 |   156 |     6   (0)| 00:00:01 |
|   3 |    NESTED LOOPS OUTER                     |                         |     1 |   141 |     5   (0)| 00:00:01 |
|   4 |     NESTED LOOPS OUTER                    |                         |     1 |   119 |     4   (0)| 00:00:01 |
|   5 |      MERGE JOIN OUTER                     |                         |     1 |    75 |     3   (0)| 00:00:01 |
|   6 |       NESTED LOOPS OUTER                  |                         |     1 |    66 |     2   (0)| 00:00:01 |
|   7 |        TABLE ACCESS BY INDEX ROWID        | COURSES                 |     1 |    44 |     1   (0)| 00:00:01 |
|*  8 |         INDEX UNIQUE SCAN                 | SYS_C0055624            |     1 |       |     0   (0)| 00:00:01 |
|*  9 |        INDEX RANGE SCAN                   | SYS_C0056657            |     1 |    22 |     1   (0)| 00:00:01 |
|  10 |       BUFFER SORT                         |                         |     1 |     9 |     2   (0)| 00:00:01 |
|  11 |        TABLE ACCESS BY INDEX ROWID BATCHED| ENROLL_REQUIREMENTS     |     1 |     9 |     1   (0)| 00:00:01 |
|* 12 |         INDEX RANGE SCAN                  | IDX_E_REQ_COURSE_ID     |     1 |       |     0   (0)| 00:00:01 |
|  13 |      TABLE ACCESS BY INDEX ROWID          | COURSES                 |     1 |    44 |     1   (0)| 00:00:01 |
|* 14 |       INDEX UNIQUE SCAN                   | SYS_C0055624            |     1 |       |     0   (0)| 00:00:01 |
|* 15 |     INDEX RANGE SCAN                      | SYS_C0056657            |     1 |    22 |     1   (0)| 00:00:01 |
|  16 |    BUFFER SORT                            |                         |     1 |    15 |     5   (0)| 00:00:01 |
|  17 |     TABLE ACCESS BY INDEX ROWID BATCHED   | COMPLETION_REQUIREMENTS |     1 |    15 |     1   (0)| 00:00:01 |
|* 18 |      INDEX RANGE SCAN                     | IDX_C_REQ_COURSE_ID     |     1 |       |     0   (0)| 00:00:01 |
|  19 |   TABLE ACCESS BY INDEX ROWID BATCHED     | COMPLETION_REQUIREMENTS |     1 |    15 |     1   (0)| 00:00:01 |
|* 20 |    INDEX RANGE SCAN                       | IDX_C_REQ_COURSE_ID     |     1 |       |     0   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   8 - access("C1_0"."COURSE_ID"=TO_NUMBER(:1))
   9 - access("S2_0"."COURSE_ID"(+)=TO_NUMBER(:1))
  12 - access("ER1_0"."COURSE_ID"(+)=TO_NUMBER(:1))
  14 - access("CC1_0"."COURSE_ID"(+)="ER1_0"."COMPLITED_COURSE_ID")
  15 - access("CC1_0"."COURSE_ID"="S1_0"."COURSE_ID"(+))
  18 - access("CR1_0"."COURSE_ID"(+)=TO_NUMBER(:1))
  20 - access("CC1_0"."COURSE_ID"="CR2_0"."COURSE_ID"(+))
 
Note
-----
   - this is an adaptive plan
```

5. 
```
Plan hash value: 3159786908
 
------------------------------------------------------------------------------------------------------------
| Id  | Operation                            | Name                | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                     |                     |     2 |    22 |     3  (34)| 00:00:01 |
|   1 |  SORT GROUP BY                       |                     |     2 |    22 |     3  (34)| 00:00:01 |
|   2 |   TABLE ACCESS BY INDEX ROWID BATCHED| GRADES              |     2 |    22 |     2   (0)| 00:00:01 |
|*  3 |    INDEX RANGE SCAN                  | IDX_GRADES_CID_TYPE |     2 |       |     1   (0)| 00:00:01 |
------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   3 - access("G"."COURSE_ID"=TO_NUMBER(:B1) AND "G"."TYPE"=:B2)
```

6. 
```
Plan hash value: 1833910691
 
------------------------------------------------------------------------------------------------------------
| Id  | Operation                           | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                    |                      |     1 |    31 |     2   (0)| 00:00:01 |
|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| COURSE_REGISTRATIONS |     1 |    31 |     2   (0)| 00:00:01 |
|*  2 |   INDEX RANGE SCAN                  | IDX_C_REG_SID_STATUS |     1 |       |     1   (0)| 00:00:01 |
------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("STUDENT_ID"=TO_NUMBER(:1) AND "STATUS" LIKE :2)
       filter("STATUS" LIKE :2)
```

7. 
```
Plan hash value: 2865859890
 
--------------------------------------------------------------------------------------------
| Id  | Operation                   | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT            |              |     1 |    72 |     1   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS BY INDEX ROWID| USERS        |     1 |    72 |     1   (0)| 00:00:01 |
|*  2 |   INDEX UNIQUE SCAN         | SYS_C0055617 |     1 |       |     0   (0)| 00:00:01 |
--------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("S1_0"."ROLE_NAME"='STUDENT')
   2 - access("S1_0"."USER_ID"=TO_NUMBER(:1))
```

8. 
```
Plan hash value: 2355334677
 
-------------------------------------------------------------------------------------------------------
| Id  | Operation                           | Name            | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                    |                 |     1 |    72 |     2   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS BY INDEX ROWID BATCHED| USERS           |     1 |    72 |     2   (0)| 00:00:01 |
|*  2 |   INDEX RANGE SCAN                  | IDX_USERS_EMAIL |     1 |       |     1   (0)| 00:00:01 |
-------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("S1_0"."ROLE_NAME"='STUDENT')
   2 - access("S1_0"."EMAIL"=:1)
```

9. 
```
Plan hash value: 2355334677
 
-------------------------------------------------------------------------------------------------------
| Id  | Operation                           | Name            | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                    |                 |     1 |    72 |     2   (0)| 00:00:01 |
|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| USERS           |     1 |    72 |     2   (0)| 00:00:01 |
|*  2 |   INDEX RANGE SCAN                  | IDX_USERS_EMAIL |     1 |       |     1   (0)| 00:00:01 |
-------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("U1_0"."EMAIL"=:1)
```

10. 
```
Plan hash value: 4074375144
 
------------------------------------------------------------------------------------------------------------
| Id  | Operation                             | Name               | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                      |                    |     1 |    58 |     3   (0)| 00:00:01 |
|   1 |  NESTED LOOPS                         |                    |     1 |    58 |     3   (0)| 00:00:01 |
|   2 |   NESTED LOOPS                        |                    |     1 |    58 |     3   (0)| 00:00:01 |
|*  3 |    TABLE ACCESS BY INDEX ROWID BATCHED| USERS              |     1 |    30 |     2   (0)| 00:00:01 |
|*  4 |     INDEX RANGE SCAN                  | IDX_USERS_EMAIL    |     1 |       |     1   (0)| 00:00:01 |
|*  5 |    INDEX RANGE SCAN                   | IDX_GRADES_SID_CID |     1 |       |     0   (0)| 00:00:01 |
|   6 |   TABLE ACCESS BY INDEX ROWID         | GRADES             |     1 |    28 |     1   (0)| 00:00:01 |
------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   3 - filter("T"."ROLE_NAME"='STUDENT')
   4 - access("T"."EMAIL"=:1)
   5 - access("T"."USER_ID"="G1_0"."STUDENT_ID" AND "G1_0"."COURSE_ID"=TO_NUMBER(:2))
 
Note
-----
   - this is an adaptive plan
```

11. 
```
Plan hash value: 88436321
 
------------------------------------------------------------------------------------------------------------
| Id  | Operation                            | Name                | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                     |                     |     1 |    53 |     3   (0)| 00:00:01 |
|   1 |  NESTED LOOPS OUTER                  |                     |     1 |    53 |     3   (0)| 00:00:01 |
|   2 |   TABLE ACCESS BY INDEX ROWID BATCHED| ENROLL_REQUIREMENTS |     1 |     9 |     2   (0)| 00:00:01 |
|*  3 |    INDEX RANGE SCAN                  | IDX_E_REQ_COURSE_ID |     1 |       |     1   (0)| 00:00:01 |
|   4 |   TABLE ACCESS BY INDEX ROWID        | COURSES             |     1 |    44 |     1   (0)| 00:00:01 |
|*  5 |    INDEX UNIQUE SCAN                 | SYS_C0055624        |     1 |       |     0   (0)| 00:00:01 |
------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   3 - access("ER1_0"."COURSE_ID"=TO_NUMBER(:1))
   5 - access("CC1_0"."COURSE_ID"(+)="ER1_0"."COMPLITED_COURSE_ID")
```

12. 
```
Plan hash value: 2865859890
 
--------------------------------------------------------------------------------------------
| Id  | Operation                   | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
--------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT            |              |     1 |    55 |     1   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS BY INDEX ROWID| USERS        |     1 |    55 |     1   (0)| 00:00:01 |
|*  2 |   INDEX UNIQUE SCAN         | SYS_C0055617 |     1 |       |     0   (0)| 00:00:01 |
--------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - filter("L1_0"."ROLE_NAME"='LECTURER')
   2 - access("L1_0"."USER_ID"=TO_NUMBER(:1))
```

13. 
```
Plan hash value: 687107713
 
---------------------------------------------------------------------------------------------------------------
| Id  | Operation                              | Name                 | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                       |                      |     1 |    94 |     4   (0)| 00:00:01 |
|   1 |  NESTED LOOPS                          |                      |     1 |    94 |     4   (0)| 00:00:01 |
|   2 |   NESTED LOOPS                         |                      |     1 |    94 |     4   (0)| 00:00:01 |
|   3 |    NESTED LOOPS                        |                      |     1 |    50 |     3   (0)| 00:00:01 |
|   4 |     TABLE ACCESS BY INDEX ROWID BATCHED| USERS                |     1 |    22 |     2   (0)| 00:00:01 |
|*  5 |      INDEX RANGE SCAN                  | IDX_USERS_EMAIL      |     1 |       |     1   (0)| 00:00:01 |
|   6 |     TABLE ACCESS BY INDEX ROWID BATCHED| COURSE_REGISTRATIONS |     1 |    28 |     1   (0)| 00:00:01 |
|*  7 |      INDEX RANGE SCAN                  | IDX_C_REG_SID_STATUS |     1 |       |     0   (0)| 00:00:01 |
|*  8 |    INDEX UNIQUE SCAN                   | SYS_C0055624         |     1 |       |     0   (0)| 00:00:01 |
|   9 |   TABLE ACCESS BY INDEX ROWID          | COURSES              |     1 |    44 |     1   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   5 - access("S1_0"."EMAIL"=:1)
   7 - access("S1_0"."USER_ID"="CR1_0"."STUDENT_ID" AND "CR1_0"."STATUS"=:2)
   8 - access("C1_0"."COURSE_ID"="CR1_0"."COURSE_ID")
 
Note
-----
   - this is an adaptive plan
```

14. 
```
Plan hash value: 412680905
 
---------------------------------------------------------------------------------------------------------------
| Id  | Operation                           | Name                    | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                    |                         |     1 |    15 |     2   (0)| 00:00:01 |
|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| COMPLETION_REQUIREMENTS |     1 |    15 |     2   (0)| 00:00:01 |
|*  2 |   INDEX RANGE SCAN                  | IDX_C_REQ_COURSE_ID     |     1 |       |     1   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("CR1_0"."COURSE_ID"=TO_NUMBER(:1))
```

15. 
```
Plan hash value: 3250736256
 
-----------------------------------------------------------------------------------------------------------
| Id  | Operation                            | Name               | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                     |                    |     1 |    16 |     2   (0)| 00:00:01 |
|   1 |  SORT AGGREGATE                      |                    |     1 |    16 |            |          |
|*  2 |   TABLE ACCESS BY INDEX ROWID BATCHED| GRADES             |     1 |    16 |     2   (0)| 00:00:01 |
|*  3 |    INDEX RANGE SCAN                  | IDX_GRADES_SID_CID |     1 |       |     1   (0)| 00:00:01 |
-----------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - filter("TYPE"='FINAL')
   3 - access("STUDENT_ID"=TO_NUMBER(:B2) AND "COURSE_ID"=TO_NUMBER(:B1))
 
Note
-----
   - This is SQL Plan Management Test Plan
```

16. 
```
Plan hash value: 3760445940
 
---------------------------------------------------------------------------------
| Id  | Operation        | Name         | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------
|   0 | SELECT STATEMENT |              |     1 |    22 |     1   (0)| 00:00:01 |
|*  1 |  INDEX RANGE SCAN| SYS_C0056657 |     1 |    22 |     1   (0)| 00:00:01 |
---------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   1 - access("S1_0"."COURSE_ID"=TO_NUMBER(:1))
```

17. 
```
Plan hash value: 11302030
 
-------------------------------------------------------------------------------------------------------------------
| Id  | Operation                               | Name                    | Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                        |                         |   149 | 13410 |     9  (12)| 00:00:01 |
|   1 |  SORT ORDER BY                          |                         |   149 | 13410 |     9  (12)| 00:00:01 |
|*  2 |   HASH JOIN RIGHT OUTER                 |                         |   149 | 13410 |     8   (0)| 00:00:01 |
|   3 |    INDEX FULL SCAN                      | SYS_C0056657            |   108 |  2376 |     1   (0)| 00:00:01 |
|*  4 |    HASH JOIN RIGHT OUTER                |                         |   140 |  9520 |     7   (0)| 00:00:01 |
|*  5 |     TABLE ACCESS FULL                   | COMPLETION_REQUIREMENTS |    15 |   225 |     3   (0)| 00:00:01 |
|   6 |     NESTED LOOPS OUTER                  |                         |   137 |  7261 |     4   (0)| 00:00:01 |
|   7 |      TABLE ACCESS FULL                  | COURSES                 |   137 |  6028 |     3   (0)| 00:00:01 |
|   8 |      TABLE ACCESS BY INDEX ROWID BATCHED| ENROLL_REQUIREMENTS     |     1 |     9 |     1   (0)| 00:00:01 |
|*  9 |       INDEX RANGE SCAN                  | IDX_E_REQ_COURSE_ID     |     1 |       |     0   (0)| 00:00:01 |
-------------------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   2 - access("C1_0"."COURSE_ID"="S1_0"."COURSE_ID"(+))
   4 - access("C1_0"."COURSE_ID"="CR1_0"."COURSE_ID"(+))
   5 - filter("CR1_0"."COURSE_ID"(+) IS NOT NULL)
   9 - access("C1_0"."COURSE_ID"="ER1_0"."COURSE_ID"(+))
 
Note
-----
   - this is an adaptive plan
```

18. 
```
Plan hash value: 779539311
 
---------------------------------------------------------------------------------------------------------
| Id  | Operation                              | Name           | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT                       |                |     1 |   199 |     4   (0)| 00:00:01 |
|   1 |  NESTED LOOPS OUTER                    |                |     1 |   199 |     4   (0)| 00:00:01 |
|   2 |   NESTED LOOPS OUTER                   |                |     1 |   155 |     3   (0)| 00:00:01 |
|   3 |    NESTED LOOPS OUTER                  |                |     1 |   100 |     2   (0)| 00:00:01 |
|   4 |     TABLE ACCESS BY INDEX ROWID        | GRADES         |     1 |    28 |     1   (0)| 00:00:01 |
|*  5 |      INDEX UNIQUE SCAN                 | SYS_C0055628   |     1 |       |     0   (0)| 00:00:01 |
|   6 |     TABLE ACCESS BY INDEX ROWID BATCHED| USERS          |     1 |    72 |     1   (0)| 00:00:01 |
|*  7 |      INDEX RANGE SCAN                  | IDX_U_UID_ROLE |     1 |       |     0   (0)| 00:00:01 |
|   8 |    TABLE ACCESS BY INDEX ROWID BATCHED | USERS          |     1 |    55 |     1   (0)| 00:00:01 |
|*  9 |     INDEX RANGE SCAN                   | IDX_U_UID_ROLE |     1 |       |     0   (0)| 00:00:01 |
|  10 |   TABLE ACCESS BY INDEX ROWID          | COURSES        |     1 |    44 |     1   (0)| 00:00:01 |
|* 11 |    INDEX UNIQUE SCAN                   | SYS_C0055624   |     1 |       |     0   (0)| 00:00:01 |
---------------------------------------------------------------------------------------------------------
 
Predicate Information (identified by operation id):
---------------------------------------------------
 
   5 - access("G1_0"."GRADE_ID"=TO_NUMBER(:1))
   7 - access("T"."USER_ID"(+)="G1_0"."STUDENT_ID" AND "T"."ROLE_NAME"(+)='STUDENT')
   9 - access("T"."USER_ID"(+)="G1_0"."LECTURER_ID" AND "T"."ROLE_NAME"(+)='LECTURER')
  11 - access("C1_0"."COURSE_ID"(+)="G1_0"."COURSE_ID")
```