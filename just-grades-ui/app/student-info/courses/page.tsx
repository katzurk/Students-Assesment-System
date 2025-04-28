"use client"
import React from "react";
import {Course, ICourse} from "./components/Course"
import {Box, Button, Container, Stack, Typography} from "@mui/material";
import {useStudentCourses} from "@/hooks/useStudentCourses";
import styles from './components/Course.module.css';
import Link from "next/link";

export default function StudentCourses() {
    const {courses, loading, error} = useStudentCourses();

    if (loading) return <p>Loading data...</p>;
    if (error) return <p>--Error: {error}</p>;

    return (
        <Container maxWidth="xl">
            <Typography variant="h4">Student Courses</Typography>
            <br></br>
            <Stack spacing={2}>
                {courses?.map((course: ICourse) => (
                    <Box key={course.id} className={styles.course} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1}}>
                        <Course {...course}/>
                        <Box>
                            <Button size="small" variant="contained" sx={{ mr: 1 }}>Classes</Button>
                            <Link href={`grades/${course.id}`}>
                                <Button size="small" variant="contained">Grades</Button>
                            </Link>
                        </Box>
                    </Box>
                ))}
            </Stack>
        </Container>
    );
}
