'use client';

import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';
import styles from '../../../courses/CoursesTable.module.css';
import React from 'react';
import { Table, TableBody, TableCell, TableHead, TableRow, Typography, Button } from '@mui/material';

interface Grade {
  id: number;
  grade: number;
  recorded: Date;
  student: Student | null;
  type: string;
  courseId: number;
  course: Course;
}

interface Student {
  studentNumber: string;
  firstName: string;
  lastName: string;
}

interface CompletionRequirement {
  type: string;
  minScore: number;
}

interface Course {
  id: number,
  name: string;
  ects: number;
  completionRequirements: Array<CompletionRequirement>;
}

function GradesForCourseTable({ courseId }: { courseId: number }) {
  const [grades, setGrades] = useState<Grade[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(`http://localhost:8080/course/${courseId}/grades`)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        setGrades(data || []);
        setLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  }, [courseId]);

  if (loading) return <Typography>Loading data...</Typography>;
  if (error) return <Typography>--Error: {error}</Typography>;

  const addGrade = async (courseId: number, studentNumber: string) => {
    window.location.href = `/course/${courseId}/addgrades/${studentNumber}`;
  }

  const goToGradeReport = async (courseId: number) => {
    window.location.href = `/course/${courseId}/gradereport`;
  }

  const removeGrade = async (id: number, courseId: number) => {
    fetch("http://localhost:8080/course/" + courseId + "/grades/" + id, {
      method: "DELETE"
    })
    .then ((response)  => {
      if (!response.ok) {
        throw new Error('Error when deleting grade');
      }
      fetch("http://localhost:8080/course/" + courseId + "/grades")
        .then((resp) => {
          if (!resp.ok) {
            throw new Error('Network response was not ok');
          }
          console.info("-----fetch after del", resp);
          return resp.json();
        })
        .then((data) => {
          setGrades(data || []);
          setLoading(false);
        })
        .catch((error) => {
          setError(error.message);
          setLoading(false);
        });
    })
    .catch((error) => {
      alert(error.message);
    });
  }

  // Grupowanie ocen per studentNumber
  const grouped = grades.reduce((acc: Record<string, Grade[]>, grade) => {
    const key = grade.student?.studentNumber || 'unknown';
    if (!acc[key]) acc[key] = [];
    acc[key].push(grade);
    return acc;
  }, {});

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Grades list for course: {grades[0]?.course.name}</h1>
      <Button
        className={styles.details_button}
        onClick={() => goToGradeReport(courseId)}
      >
        Show report
      </Button>
      <Table className={styles.table}>
        <TableHead>
          <TableRow className={styles.headerRow}>
            <TableCell className={styles.headerCell}>Student ID</TableCell>
            <TableCell className={styles.headerCell}>Surname</TableCell>
            <TableCell className={styles.headerCell}>Name</TableCell>
            <TableCell className={styles.headerCell}>Grades</TableCell>
            <TableCell className={styles.headerCell}>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {Object.entries(grouped).map(([studentNumber, studentGrades]) => {
            const student = studentGrades[0].student!;
            return (
              <TableRow key={studentNumber} className={styles.row}>
                <TableCell className={styles.cell}>{student.studentNumber}</TableCell>
                <TableCell className={styles.cell}>{student.lastName}</TableCell>
                <TableCell className={styles.cell}>{student.firstName}</TableCell>
                <TableCell className={styles.cell}>
                  {studentGrades.map((g) => `${g.type}: ${g.grade}`).join(', ')}
                </TableCell>
                <TableCell className={styles.cell}>
                  {studentGrades.map((g) => (
                    <div key={g.id} style={{ marginBottom: '4px' }}>
                      <Button
                        className={styles.delete_button}
                        onClick={() => removeGrade(g.id, g.course.id)}
                      >
                        Delete {g.type}
                      </Button>
                    </div>
                  ))}
                  <Button
                    className={styles.details_button}
                    onClick={() => addGrade(studentGrades[0].course.id, studentGrades[0].student!.studentNumber)}
                  >
                    Add
                  </Button>
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </div>
  );
}

export default function Page() {
  const params = useParams();
  const courseId = Number(params.courseId);

  if (isNaN(courseId)) return <Typography>Invalid course ID</Typography>;

  return <GradesForCourseTable courseId={courseId} />;
}
