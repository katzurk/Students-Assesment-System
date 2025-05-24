"use client"
import { SetStateAction, useEffect, useState } from 'react';
import styles from './CoursesTable.module.css';
import React from 'react';
import axios from 'axios';
import { Button, Table, TableBody, TableCell, TableHead, TableRow, Typography } from '@mui/material';

interface CompletionRequirement {
  type: string;
  minScore: number;
}

interface EnrollRequirement {
  complitedCourse: Course | null;
  minEcts: number | null;
}

interface Course {
  id: number,
  name: string;
  ects: number;
  completionRequirements: Array<CompletionRequirement>;
  enrollRequirements: Array<EnrollRequirement> | null;
}

interface CourseWithMessages extends Course {
  message: string | null;
  messageType: "success" | "error" | null;
}

interface CollapsiblePanelProps {
  isOpen: boolean;
  toggle: () => void;
  course: CourseWithMessages;
}

function CompletionRequiermentsCollapsiblePanel({ isOpen, toggle, course }: CollapsiblePanelProps) {
  return (
    <div className={styles.cell}>
      <Button
        onClick={toggle}
        className={styles.toggle_button}
      >
        {"to pass info."}
        <span style={{ transform: isOpen ? "rotate(0deg)" : "rotate(90deg)", transition: "transform 0.3s ease", marginLeft: "10px" }}>
          {"▼"}
        </span>
      </Button >
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
          <Table className={styles.table}>
            <TableHead>
              <TableRow className={styles.headerRow}>
                <TableCell className={styles.headerCell}>Type</TableCell>
                <TableCell className={styles.headerCell}>Min. score</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {course.completionRequirements.map((requirement, index) => (
                <TableRow key={index} className={styles.row}>
                  <TableCell className={styles.cell}>{requirement.type}</TableCell>
                  <TableCell className={styles.cell}>{requirement.minScore}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </div>
    </div>
  );
}

function EnrollRequiermentsCollapsiblePanel({ isOpen, toggle, course }: CollapsiblePanelProps) {
  return (
    <div className={styles.cell}>
      <Button
        onClick={toggle}
        className={styles.toggle_button}
      >
        {"to enroll info."}
        <span style={{ transform: isOpen ? "rotate(0deg)" : "rotate(90deg)", transition: "transform 0.3s ease", marginLeft: "10px" }}>
          {"▼"}
        </span>
      </Button >
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
              ))) : (
            <Typography>No enrollment requirements are needed</Typography>
          )}
        </div>
      </div>
    </div>
  );
}

export default function CoursesTable() {
  const [courses, setCourses] = useState<CourseWithMessages[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [openPanel, setOpenPanel] = useState<number | null>(null);

  useEffect(() => {
    fetch('http://localhost:8080/courses')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        const coursesWithMessages = (data || []).map((course: Course) => ({
          ...course,
          message: null,
          messageType: null
        }));
        setCourses(coursesWithMessages);
        setLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <Typography>Loading data...</Typography>;
  if (error) return <Typography>--Error: {error}</Typography>;

  const togglePanel = (index: number): void => {
    setOpenPanel((prevIndex: number | null) => (prevIndex === index ? null : index))
  };

  const courseDetails = async (id: number) => {
    window.location.href = ("/course/" + id + "/grades");
  }

  const courseReport = async (id: number) => {
    window.location.href = ("/course/" + id + "/coursereport");
  }

  const removeCourse = async (id: number) => {
    try {
      fetch("http://localhost:8080/courses/" + id, {
        method: "DELETE"
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error('Can not delete course, it is needed to complete another course');
          }
          fetch('http://localhost:8080/courses')
            .then((resp) => {
              if (!resp.ok) {
                throw new Error('Network response was not ok');
              }
              return resp.json();
            })
            .then((data) => {
              const coursesWithMessages = (data || []).map((course: Course) => ({
                ...course,
                message: null,
                messageType: null
              }));
              setCourses(coursesWithMessages);
              setLoading(false);
            })
            .catch((error) => {
              setError(error.message);
              setLoading(false);
            });
        })
        .catch((error) => {
          setCourses(prevCourses => prevCourses.map(course => 
            course.id === id ? {
              ...course,
              message: error.message,
              messageType: "error"
            } : course
          ));
        });
    } catch (error) {
      console.error("Error:", error);
    }
  }

  const openRegistration = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/courses/${id}/open-registration`, {
        method: 'POST',
        credentials: "include"
      });
  
      if (!response.ok) {
        throw new Error('Failed to open registration');
      }
  
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: 'Registration successfully opened',
          messageType: 'success'
        } : course
      ));
    } catch (error) {
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: error instanceof Error ? error.message : 'Unknown error occurred',
          messageType: 'error'
        } : course
      ));
    }
    
    // Clear message after 5 seconds
    setTimeout(() => {
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: null,
          messageType: null
        } : course
      ));
    }, 5000);
  };
  
  const closeRegistration = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/courses/${id}/close-registration`, {
        method: 'POST',
        credentials: "include"
      });
  
      if (!response.ok) {
        throw new Error('Failed to close registration');
      }
  
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: 'Registration successfully closed',
          messageType: 'success'
        } : course
      ));
    } catch (error) {
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: error instanceof Error ? error.message : 'Unknown error occurred',
          messageType: 'error'
        } : course
      ));
    }
    
    // Clear message after 5 seconds
    setTimeout(() => {
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: null,
          messageType: null
        } : course
      ));
    }, 5000);
  };
  
  const closeCourse = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/courses/${id}/close`, {
        method: 'POST',
        credentials: "include"
      });
  
      if (!response.ok) {
        throw new Error('Failed to close course, course is not active');
      }
  
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: 'Course successfully closed',
          messageType: 'success'
        } : course
      ));
    } catch (error) {
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: error instanceof Error ? error.message : 'Unknown error occurred',
          messageType: 'error'
        } : course
      ));
    }
    
    // Clear message after 5 seconds
    setTimeout(() => {
      setCourses(prevCourses => prevCourses.map(course => 
        course.id === id ? {
          ...course,
          message: null,
          messageType: null
        } : course
      ));
    }, 5000);
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Courses list</h1>
      <Button
        className={styles.add_button}
        href="/addcourse"
        rel="noopener noreferrer"
      >
        add course
      </Button>
      <Table className={styles.table}>
        <TableHead>
          <TableRow className={styles.headerRow}>
            <TableCell className={styles.headerCell}>Name</TableCell>
            <TableCell className={styles.headerCell}>Ects points</TableCell>
            <TableCell className={styles.headerCell}>Completion Requirements</TableCell>
            <TableCell className={styles.headerCell}>Requirements to enroll</TableCell>
            <TableCell className={styles.headerCell}>Finals Reports</TableCell>
            <TableCell className={styles.headerCell}>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {courses.map((course, index) => (
            <React.Fragment key={index}>
              <TableRow className={styles.row}>
                <TableCell className={styles.cell}>{course.name}</TableCell>
                <TableCell className={styles.cell}>{course.ects}</TableCell>
                <TableCell>
                  <CompletionRequiermentsCollapsiblePanel
                    key={index}
                    course={course}
                    isOpen={openPanel === index}
                    toggle={() => togglePanel(index)}
                  />
                </TableCell>
                <TableCell>
                  <EnrollRequiermentsCollapsiblePanel
                    key={index}
                    course={course}
                    isOpen={openPanel === index}
                    toggle={() => togglePanel(index)}
                  />
                </TableCell>
                <TableCell className={styles.cell}>
                  <Button className={styles.report_button} onClick={() => courseReport(course.id)}>Report</Button >
                </TableCell>
                <TableCell className={styles.cell}>
                  <Button className={styles.delete_button} onClick={() => removeCourse(course.id)}>Delete</Button>
                  <br />
                  <Button className={styles.details_button} onClick={() => courseDetails(course.id)}>Details</Button>
                  <br />
                  <Button className={styles.open_button} onClick={() => openRegistration(course.id)}>Open Registration</Button>
                  <br />
                  <Button className={styles.close_button} onClick={() => closeRegistration(course.id)}>Close Registration</Button>
                  <br />
                  <Button className={styles.close_course_button} onClick={() => closeCourse(course.id)}>Close Course</Button>
                </TableCell>
              </TableRow>
              {course.message && (
                <TableRow>
                  <TableCell colSpan={6} style={{ 
                    color: course.messageType === "success" ? "green" : "red",
                    padding: "10px",
                    backgroundColor: course.messageType === "success" ? "rgba(0, 255, 0, 0.1)" : "rgba(255, 0, 0, 0.1)"
                  }}>
                    {course.message}
                  </TableCell>
                </TableRow>
              )}
            </React.Fragment>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
