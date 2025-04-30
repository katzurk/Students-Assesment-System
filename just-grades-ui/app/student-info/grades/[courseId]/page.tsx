"use client"

import React, {useEffect, useState} from "react";
import {useParams} from "next/navigation";
import {Button, Container, Stack, Typography} from "@mui/material";
import {Grade, IGrade} from "@/app/student-info/grades/[courseId]/components/Grade";
import {StudentService} from "@/services/StudentService";
import styles from '../../courses/components/Course.module.css';

export default function CourseGrades() {
    const { courseId } = useParams();
    const [grades, setGrades] = useState<IGrade[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        StudentService.getStudentGradesByCourse(courseId)
            .then(setGrades)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [courseId]);

    if (loading) return <p>Loading data...</p>;
    if (error) return <p>--Error: {error}</p>;

    const goToGradeReport = async () => {
        window.location.href = `/course/${courseId}/gradereport`;
    }

    return (
        <Container maxWidth="xl">
            <Typography variant="h4" sx={{ fontWeight: 'bold' }}>{grades?.[0]?.course?.name}</Typography>

            <Button className={styles.classes_button} onClick={() => goToGradeReport()}>
                Show Report
            </Button>

            <br></br>
            <br></br>
            <Typography variant="h5">Grades</Typography>

            <Stack spacing={2}>
                {grades?.map((grade: IGrade) => (
                    <Grade key={grade.id} {...grade}/>
                ))}
            </Stack>
        </Container>
    );
}