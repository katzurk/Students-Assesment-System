"use client"
import React from "react";
import Link from "next/link";
import styles from './StudentInfo.module.css';
import {Container, Grid, Button, Box, Typography} from "@mui/material";
import {StudentCard} from "@/app/student-info/components/StudentCard";
import {StudentCoursesView} from "@/app/student-info/components/StudentCoursesView";

export default function StudentInfo() {
    return (
        <Container maxWidth="xl">
            <Grid container spacing={2}>
                <Grid size={{xs: 12, md: 4}}>
                    <StudentCard />
                    <Box display="flex" flexDirection="column" gap={1} sx={{mt: 2}}>
                        <Link href="student-info/courses">
                            <Button className={styles.button} variant="contained">My courses</Button>
                        </Link>
                        <Link href="student-info/final-grades">
                            <Button className={styles.button} variant="contained">Final grades</Button>
                        </Link>
                        <Link href="student/registration">
                            <Button className={styles.button} variant="contained">Registration</Button>
                        </Link>
                    </Box>
                </Grid>
                <Grid size={{xs: 12, md: 8}}>
                    <Box className={styles.coursesContainer}>
                        <Typography variant="h5">Courses</Typography>
                        <StudentCoursesView/>
                    </Box>
                </Grid>
            </Grid>
        </Container>
    );
}
