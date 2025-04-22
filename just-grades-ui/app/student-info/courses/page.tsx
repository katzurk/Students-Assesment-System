"use client"
import React, {useEffect, useState} from "react";
import axios from "axios";
import {Course, ICourse} from "./components/Course"
import {Container, Stack, Typography} from "@mui/material";

export default function StudentCourses() {
    const [courses, setCourses] = useState<ICourse[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        axios.get('http://localhost:8080/student-info/courses', {
            withCredentials: true
        }).then((response) => {
                setCourses(response.data || []);
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
