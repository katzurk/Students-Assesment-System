'use client';

import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';
import styles from '../../../courses/CoursesTable.module.css';
import React from 'react';
import { Typography, Button } from '@mui/material';
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, ArcElement, Title, Tooltip, Legend } from 'chart.js';


interface Report {
  gradeStatus: string;
  studentsCount: number;
}

ChartJS.register(
  CategoryScale,
  LinearScale,
  ArcElement,
  Title,
  Tooltip,
  Legend
);


function CourseReport({ courseId }: { courseId: number }) {
    const [report, setReport] = useState<Report[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
      fetch(`http://localhost:8080/course/${courseId}/coursereport`)
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
    }, [courseId]);

    if (loading) return <Typography>Loading data...</Typography>;
    if (error) return <Typography>--Error: {error}</Typography>;


    const chartData = {
      labels: report.map((r) => r.gradeStatus),
      datasets: [
        {
          label: 'Students Count',
          data: report.map((r) => r.studentsCount),
          backgroundColor: report.map((r) =>
            r.gradeStatus.toLowerCase() === 'passed' ? 'rgba(75, 192, 118, 0.7)' : 'rgba(215, 61, 94, 0.7)'
          ),
          borderColor: report.map((r) =>
            r.gradeStatus.toLowerCase() === 'passed' ? 'rgb(75, 192, 128)' : 'rgb(219, 85, 114)'
          ),
          borderWidth: 3,
        },
      ],
    };

    const options = {
        responsive: true,
        plugins: {
          legend: {
            display: true,
            position: 'top' as const,
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
        <h1 className={styles.title}>Course Report</h1>

        <div className={styles.pieChartContainer}>
          <h2 className={styles.title} >Final Grade Distribution</h2>
          {report.length > 0 && <Pie data={chartData} options={options} />}
        </div>
      </div>
    )
}



export default function Page() {
  const params = useParams();
  const courseId = Number(params.courseId);

  if (isNaN(courseId)) return <Typography>Invalid course ID</Typography>;

  return <CourseReport courseId={courseId} />;
}