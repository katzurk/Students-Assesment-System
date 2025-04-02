"use client"
import { SetStateAction, useEffect, useState } from 'react';
import styles from './CoursesTable.module.css';
import Image from "next/image";
import React from 'react';

interface CompletionRequirement {
  type: string;
  minScore: number;
}

interface EnrollRequirement {
  complitedCourse: Course | null;
  minEcts: number | null;
}

interface Course {
  name: string;
  ects: number;
  completionRequirements: Array<CompletionRequirement>;
  enrollRequirements: Array<EnrollRequirement> | null;
}


interface CollapsiblePanelProps {
  isOpen: boolean;
  toggle: () => void;
  course: Course;
}

function CompletionRequiermentsCollapsiblePanel({ isOpen, toggle, course }: CollapsiblePanelProps) {
  return (
    <div className={styles.cell}>
      <button
        onClick={toggle}
        className={styles.toggle_button}
      >
        {"to pass info."}
        <span style={{ transform: isOpen ? "rotate(0deg)" : "rotate(90deg)", transition: "transform 0.3s ease", marginLeft: "10px"}}>
          {"▼"}
        </span>
      </button>
      <div
        className={styles.collapsible_pannel}
        style={{
          overflow: "hidden",
          height: isOpen ? "auto" : "0",
          opacity: isOpen ? 1 : 0,
          padding: isOpen ? "10px" : "0",
          transition: "height 0.3s ease, opacity 0.3s ease, padding 0.3s ease"
        }}
      >
        <div>
        <table className={styles.table}>
          <thead>
            <tr className={styles.headerRow}>
              <th className={styles.headerCell}>Type</th>
              <th className={styles.headerCell}>Min. score</th>
            </tr>
          </thead>
          <tbody>
            { course.completionRequirements.map((requirement, index) => (
              <tr key={index} className={styles.row}>
                <td className={styles.cell}>{requirement.type}</td>
                <td className={styles.cell}>{requirement.minScore}</td>
              </tr>
            ))}
          </tbody>
        </table>
        </div>
      </div>
    </div>
  );
}

function EnrollRequiermentsCollapsiblePanel({ isOpen, toggle, course }: CollapsiblePanelProps) {
  return (
    <div className={styles.cell}>
      <button
        onClick={toggle}
        className={styles.toggle_button}
      >
        {"to enroll info."}
        <span style={{ transform: isOpen ? "rotate(0deg)" : "rotate(90deg)", transition: "transform 0.3s ease", marginLeft: "10px"}}>
          {"▼"}
        </span>
      </button>
      <div
        className={styles.collapsible_pannel}
        style={{
          overflow: "hidden",
          height: isOpen ? "auto" : "0",
          opacity: isOpen ? 1 : 0,
          padding: isOpen ? "10px" : "0",
          transition: "height 0.3s ease, opacity 0.3s ease, padding 0.3s ease"
        }}
      >
        <div>
          {course.enrollRequirements && course.enrollRequirements.length > 0 ? (
            course.enrollRequirements
            .map((requirement, index) => (
              <React.Fragment key={index}>
              {requirement.minEcts && <li style={{ marginBottom: "5px" }}>min ects: {requirement.minEcts}</li>}
              {requirement.complitedCourse && <li style={{ marginBottom: "5px" }}>complited course: {requirement.complitedCourse.name}</li>}
              </React.Fragment>
          ))): (
            <div>No enrollment requirements are needed</div>
          )}
        </div>
      </div>
    </div>
  );
}

export default function CoursesTable() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [openPanel, setOpenPanel] = useState<number | null>(null);

  const togglePanel = (index: number): void => {
    setOpenPanel((prevIndex: number | null) => (prevIndex === index ? null : index))};

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
        href="/addcourse"
        rel="noopener noreferrer"
      >
        add course
      </a>
      <table className={styles.table}>
        <thead>
          <tr className={styles.headerRow}>
            <th className={styles.headerCell}>Name</th>
            <th className={styles.headerCell}>Ects points</th>
            <th className={styles.headerCell}>Completion Requirements</th>
            <th className={styles.headerCell}>Requirements to enroll</th>
          </tr>
        </thead>
        <tbody>
          {courses.map((course, index) => (
            <tr key={index} className={styles.row}>
              <td className={styles.cell}>{course.name}</td>
              <td className={styles.cell}>{course.ects}</td>
              <td>
                <CompletionRequiermentsCollapsiblePanel
                  key={index}
                  course={course}
                  isOpen={openPanel === index}
                  toggle={() => togglePanel(index)}
                />
              </td>
              <td>
                <EnrollRequiermentsCollapsiblePanel
                  key={index}
                  course={course}
                  isOpen={openPanel === index}
                  toggle={() => togglePanel(index)}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
