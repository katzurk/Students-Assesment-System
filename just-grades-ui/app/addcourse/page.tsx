"use client"
import { SetStateAction, useEffect, useState } from 'react';
import styles from './addCourse.module.css';
import { Button} from '@mui/material';


interface CompletionRequirement {
  type: string;
  minScore: number;
}

interface EnrollRequirement {
  complitedCourseId: number | null; //Course | null;
  minEcts: number | null;
}

interface Course {
  id: number,
  name: string;
  ects: number;
  completionRequirements: Array<CompletionRequirement>;
  enrollRequirements: Array<EnrollRequirement>; // | null;
}

export default function FillInForm() {
  const [course, setCourse] = useState<Course>({
    id: 0,
    name: "",
    ects: 4,
    completionRequirements: [{type: "exam", minScore: 51}],
    enrollRequirements: new Array<EnrollRequirement> ()
  });
  const [existingcourses, setExistingCourses] = useState<Course[]>([]);
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
        setExistingCourses(data || []);
      })
      .catch((error) => {
        setError(error.message);
      });
    }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setCourse(prev => ({
      ...prev,
      [name]: name === "ects" || name === "minScore" || name === "minEcts" ? Number(value) || "" : value,
    }));
  };

  const handleCRChange = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    switch (name) {
      case 'min_score':
        course.completionRequirements[index].minScore = Number(value);
        break;
      case 'type':
        course.completionRequirements[index].type = value;
        break;
    }
    setCourse(prev => ({...prev}));
  };

  const handleERChange = (index: number, e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    switch (name) {
      case 'min_ects':
        course.enrollRequirements[index].minEcts = Number(value);
        break;
      case 'complited_course':
        course.enrollRequirements[index].complitedCourseId = (value === '--' ? null : Number(value));
        break;
      }
    setCourse(prev => ({...prev}));
  };


  const addCompletionRequirement = () => {
    course.completionRequirements?.push({type: "test", minScore: 21});
    setCourse(prev => ({...prev}));
  };

  const addEnrollRequirement = () => {
    course.enrollRequirements?.push({complitedCourseId: 1, minEcts: 4});
    setCourse(prev => ({...prev}));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/addcourse", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(course),
      });
      window.location.href =('/courses');
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const removeCREntry = (index: number) => {
    course.completionRequirements.splice(index, 1)
    setCourse(prev => ({...prev}));
  }

  const removeEREntry = (index: number) => {
    course.enrollRequirements.splice(index, 1)
    setCourse(prev => ({...prev}));
  }


  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Add a new course</h1>

      <form onSubmit={handleSubmit} className={styles.from}>
        <h2 className={styles.subtitle}>Basic information</h2>
        <div className={styles.field_label}>
          <label>
            Name :
            <input type="text" name="name" value={course.name} onChange={handleChange} required />
          </label>
        </div>
        <div className={styles.field_label}>
          <label>
            ECTS points :
            <input type="number" name="ects" value={course.ects} onChange={handleChange} required />
          </label>
        </div>

        <h2 className={styles.subtitle}>Completion Requirements</h2>
          {(course.completionRequirements || []).map((completionReq, index) => (
          <div key={index}>
            <div className={styles.field_label}>
              <label >
                Type :
                <input type="text" name="type" value={completionReq.type} onChange={ev => handleCRChange(index, ev)} required />
              </label>
            </div>
            <div className={styles.field_label}>
              <label>
                Min. score :
                <input type="number" name="min_score" value={completionReq.minScore} onChange={ev => handleCRChange(index, ev)} required />
              </label>
            </div>
            {index === 0 ? "" :
              <Button type="button" className={styles.remove_button} onClick={() => removeCREntry(index)}>Remove above Requirement</Button >
            }
          </div>
         ))}
         <Button type="button" className={styles.req_button} onClick={addCompletionRequirement}>Add Requirement to complete</Button >


        <h2 className={styles.subtitle}>Requirements to enroll (optional)</h2>
        {(course.enrollRequirements || []).map((enrollReq, index2) => (
          <div key={index2}>
            <div className={styles.field_label}>
              <label>
                Min. ECTS :
                <input type="number" name="min_ects" value={enrollReq.minEcts?enrollReq.minEcts:0} onChange={ev => handleERChange(index2, ev)} />
              </label>
            </div>
            <div className={styles.field_label}>
              <label >
                Complited course :
                <select name="complited_course" value={enrollReq.complitedCourseId?enrollReq.complitedCourseId:'--'} onChange={ev => handleERChange(index2, ev)} >
                  {(existingcourses || []).map((course, index3) => (
                    <option key={index3} value={course.id}>
                      {course.name}
                    </option>
                  ))}
                  <option value='--'>
                    --
                  </option>
                </select>
              </label>
            </div>
            <Button type="button" className={styles.remove_button} onClick={() => removeEREntry(index2)}>Remove above Requirement</Button >
          </div>
        ))}
        <Button type="button" className={styles.req_button} onClick={addEnrollRequirement}>Add Requirement to enroll</Button >

        <Button
        className={styles.cancel_button}
        href="/courses"
        rel="noopener noreferrer"
        >
          Cancel
        </Button>
        <Button type="submit" className={styles.submit_button}>Submit</Button >
      </form>
    </div>
  );
}

