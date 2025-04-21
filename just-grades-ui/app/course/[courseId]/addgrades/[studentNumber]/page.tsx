"use client"
import { SetStateAction, useEffect, useState } from 'react';
import styles from '../../../../addcourse/addCourse.module.css';
import { Button} from '@mui/material';
import { useRouter, useParams } from 'next/navigation';

interface Grade {
    id: number;
    grade: number;
    studentNumber: String | null;
    type: string;
    courseId: number;
  }

export default function FillInFormAddGrades() {
    const router = useRouter();
    const params = useParams();
    const courseId = Number(params.courseId);
    const studentNumber = String(params.studentNumber);
    const [grade, setGrade] = useState<Grade>({
      id: 0,
      grade: 0,
      studentNumber: studentNumber,
      type: "",
      courseId: courseId
    });
    const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setGrade(prev => ({
        ...prev,
        [name]: name === 'grade' ? Number(value) : value
      }));
  };


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      console.log('Sending data:', grade);
      const response = await fetch("http://localhost:8080/course/" + courseId + "/addgrades", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(grade),
      });
      window.location.href =('/course/' + courseId + '/grades');
    } catch (error) {
      console.error("Error:", error);
    }
  };


  return (
    <div className={styles.container}>
        <h1 className={styles.title}>Add a new grade</h1>

        <form onSubmit={handleSubmit} className={styles.from}>
            <div>
            <div className={styles.field_label}>
                <label >
                Type :
                <input type="text" name="type" value={grade.type} onChange={ev => handleChange(ev)} required />
                </label>
            </div>
            <div className={styles.field_label}>
                <label>
                Grade :
                <input type="number" name="grade" value={grade.grade} onChange={ev => handleChange(ev)} required />
                </label>
            </div>
            </div>

            <Button type="button"
            className={styles.cancel_button}
            onClick={() => router.push(`/course/${courseId}/grades`)}
            rel="noopener noreferrer"
            >
            Cancel
            </Button>
            <Button type="submit" className={styles.submit_button}>Submit</Button >
        </form>
    </div>
  );
}
