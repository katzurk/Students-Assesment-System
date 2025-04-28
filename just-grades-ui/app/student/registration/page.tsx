"use client"
import {Box, Button, Container, Stack, Typography} from "@mui/material";
import {Course, ICourseRegistered} from "@/app/student-info/courses/components/Course";
import styles from "@/app/student-info/courses/components/Course.module.css";
import React, {useEffect, useState} from "react";
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import PersonRemoveIcon from '@mui/icons-material/PersonRemove';
import {StudentService} from "@/services/StudentService";

export default function Registration() {
    const [registrations, setRegistrations] = useState<ICourseRegistered[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

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
        await StudentService.registerForCourse(courseId);
        fetchRegistrations();
    };

    const handleDeregister = async (courseId: number) => {
        await StudentService.deregisterFromCourse(courseId);
        fetchRegistrations();
    };

    if (loading) return <p>Loading data...</p>;
    if (error) return <p>--Error: {error}</p>;

    return (
        <Container maxWidth="xl">
            <Typography variant="h4">Registration</Typography>
            <br></br>
            <Stack spacing={2}>
                {registrations?.map((course: ICourseRegistered) => (
                    <Box key={course.id} className={styles.course} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1}}>
                        <Course {...course}/>
                        {course.registered ?
                            <Button onClick={() => handleDeregister(course.id)} variant={"contained"} color={"error"} size="large" startIcon={<PersonRemoveIcon />} sx={{width: 200}}>
                                Deregister
                            </Button>
                        :
                            <Button onClick={() => handleRegister(course.id)} variant={"contained"} color={"success"} size="large" startIcon={<PersonAddIcon />} sx={{width: 200}}>
                                Register
                            </Button>
                        }
                    </Box>
                ))}
            </Stack>
        </Container>
    );
}
