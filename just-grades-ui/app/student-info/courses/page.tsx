"use client"
import React from "react";
import {Course, ICourse} from "./components/Course"
import {Container, Stack, Typography} from "@mui/material";
import {useStudentCourses} from "@/app/student-info/hooks/useStudentCourses";

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
                    <Course key={course.id} {...course}/>
                ))}
            </Stack>
        </Container>
    );
}
