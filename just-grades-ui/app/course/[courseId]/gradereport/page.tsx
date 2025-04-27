'use client';

import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';
import styles from '../../../courses/CoursesTable.module.css';
import React from 'react';
import { Typography, Button } from '@mui/material';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

interface Grade {
  id: number;
  grade: number;
  recorded: Date;
//   student: Student | null;
  type: string;
  courseId: number;
//   course: Course;
}

interface Report {
  grade: number;
  studentsCount: number;
}

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);


function GradeReport({ courseId }: { courseId: number }) {
    const [report, setReport] = useState<Report[]>([]);
    const [grades, setGrades] = useState<Grade[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedType, setSelectedType] = useState<string>('--');


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

    const handleTypeChange = async (type: string) => {
      setSelectedType(type);
      setLoading(true);

      fetch(`http://localhost:8080/course/${courseId}/gradereport/${type}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then((data) => {
          setReport(data || []);
          setLoading(false);
        })
        .catch((error) => {
          setError(error.message);
          setLoading(false);
        });

      if (loading) return <Typography>Loading data...</Typography>;
      if (error) return <Typography>--Error: {error}</Typography>;
    }


    const uniqueTypes = Array.from(new Set(grades.map(grade => grade.type)));

    const chartData = {
      labels: report.map((r) => r.grade),
      datasets: [
        {
          label: 'Students Count',
          data: report.map((r) => r.studentsCount),
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgb(75, 192, 192)',
          borderWidth: 3,
        },
      ],
    };

    const options = {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
        },
      },
      plugins: {
        legend: {
          display: true,
        },
        tooltip: {
          enabled: true,
        },
      },
    };

    if (loading) return <Typography>Loading data...</Typography>;
    if (error) return <Typography>Error: {error}</Typography>;


    return (
      <div>
        <h1 className={styles.title}>Grades Report</h1>

        <div className={styles.field_label}>
          <label >
            Grade type :
            <select name="type" value={selectedType} onChange={(e) => handleTypeChange(e.target.value)} >
              {(uniqueTypes || []).map((type, index3) => (
                <option key={index3} value={type}>
                  {type}
                </option>
              ))}
              <option value='--'>
                --
              </option>
            </select>
          </label>
        </div>

        <div className={styles.chartContainer}>
          <h2 className={styles.title} >Grade Distribution</h2>
          {report.length > 0 && <Bar data={chartData} options={options} />}
        </div>
      </div>
    )
}



export default function Page() {
  const params = useParams();
  const courseId = Number(params.courseId);

  if (isNaN(courseId)) return <Typography>Invalid course ID</Typography>;

  return <GradeReport courseId={courseId} />;
}