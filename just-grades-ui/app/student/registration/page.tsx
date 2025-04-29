"use client"
import {Box, Container, Stack, Typography} from "@mui/material";
import {Course, ICourseRegistered} from "@/app/student-info/courses/components/Course";
import styles from "@/app/student-info/courses/components/Course.module.css";
import React, {useEffect, useState} from "react";
import {StudentService} from "@/services/StudentService";
import {FilterButtons} from "@/app/student/registration/components/FilterButtons";
import {ActionButton} from "@/app/student/registration/components/ActionButton";
import {AlertSnackbar} from "@/app/student/registration/components/AlertSnackbar";

export default function Registration() {
    const [registrations, setRegistrations] = useState<ICourseRegistered[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [filter, setFilter] = useState<string>("all");
    const [snackbarStatus, setSnackbarStatus] = useState<string | null>(null);

    useEffect(() => {
        fetchRegistrations()
    }, []);

    const fetchRegistrations = () => {
        StudentService.getAllOpenedCourses()
            .then(setRegistrations)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }

    const handleRegister = async (courseId: number) => {
        const res = await StudentService.registerForCourse(courseId);
        fetchRegistrations();
        setSnackbarStatus(res)
    };

    const handleDeregister = async (courseId: number) => {
        const res = await StudentService.deregisterFromCourse(courseId);
        fetchRegistrations();
        setSnackbarStatus(res)
    };

    const filteredRegistrations = registrations.filter(course => {
        if (filter === "registered") return course.status === "ACTIVE";
        if (filter === "pending") return course.status === "application submitted";
        if (filter === "not registered") return course.status === null;
        return true;
    });

    if (loading) return <p>Loading data...</p>;
    if (error) return <p>--Error: {error}</p>;

    return (
        <Container maxWidth="xl">
            <AlertSnackbar status={snackbarStatus} onClose={() => setSnackbarStatus(null)} />
            <Box display="flex" flexDirection="row" gap={100}>
                <Typography variant="h4">Registration</Typography>
                <FilterButtons filter={filter} setFilter={setFilter}/>
            </Box>
            <br></br>
            <Stack spacing={2}>
                {filteredRegistrations?.map((course: ICourseRegistered) => (
                    <Box key={course.id} className={styles.course} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1}}>
                        <Course {...course}/>
                        <ActionButton courseId={course.id} status={course.status} onDeregister={handleDeregister} onRegister={handleRegister}/>
                    </Box>
                ))}
            </Stack>
        </Container>
    );
}
