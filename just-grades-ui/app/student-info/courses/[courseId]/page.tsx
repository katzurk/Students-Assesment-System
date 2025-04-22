"use client"

import React, {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "next/navigation";
import {Container, Stack, Typography} from "@mui/material";
import {Grade, IGrade} from "@/app/student-info/courses/[courseId]/components/Grade";

export default function CourseGrades() {
    const { courseId } = useParams();
    const [grades, setGrades] = useState<IGrade[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        axios.get(`http://localhost:8080/student-info/courses/${courseId}`, {
            withCredentials: true
        }).then((response) => {
            setGrades(response.data || []);
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
            <Typography variant="h4" sx={{ fontWeight: 'bold' }}>{grades?.[0]?.course?.name}</Typography>
            <Typography variant="h5">Grades</Typography>
            <br></br>
            <Stack spacing={2}>
                {grades?.map((grade: IGrade) => (
                    <Grade key={grade.id} {...grade}/>
                ))}
            </Stack>
        </Container>
    );
}