"use client"
import { useEffect, useState } from 'react';
import styles from './CoursesTable.module.css';
import Image from "next/image";

interface CompletionRequirement {
  type: string;
  minScore: number;
}

interface Course {
  name: string;
  ects: number;
  completionRequirements: Array<CompletionRequirement>;
}


export default function CoursesTable() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch('http://localhost:8080/courses')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        setCourses(data || []);
        setLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading data...</p>;
  if (error) return <p>--Error: {error}</p>;

  return (
    <div className={styles.container}>
      <a href="/">
        <Image
          className={styles.logo}
          src="/justgrades_logo.png"
          alt="JustGrades logo"
          width= {131}
          height= {96}
          priority
        />
      </a>
      <hr className={styles.logo_line}></hr>

      <h1 className={styles.title}>My courses list</h1>
      <a
        className={styles.add_button}
        href="/addCourse"
        rel="noopener noreferrer"
      >
        add course
      </a>
      <table className={styles.table}>
        <thead>
          <tr className={styles.headerRow}>
            <th className={styles.headerCell}>Name</th>
            <th className={styles.headerCell}>Ects points</th>
            <th className={styles.headerCell}>Type</th>
            <th className={styles.headerCell}>Min. score to pass</th>
            <th className={styles.headerCell}>Requirements to enroll?</th>
          </tr>
        </thead>
        <tbody>
          {courses.map((course, index) => (
            <tr key={index} className={styles.row}>
              <td className={styles.cell}>{course.name}</td>
              <td className={styles.cell}>{course.ects}</td>
              <td className={styles.cell}>{course.completionRequirements.map((requirement, index2) => (requirement.type))}</td>
              <td className={styles.cell}>{course.completionRequirements.map((requirement, index2) => (requirement.minScore))}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
