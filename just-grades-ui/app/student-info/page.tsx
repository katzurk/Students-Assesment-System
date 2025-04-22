"use client"
import React, {useEffect, useState} from "react";
import Link from "next/link";
import styles from './StudentInfo.module.css';
import {Container, Grid, Button, Box, Typography} from "@mui/material";
import {StudentCard} from "@/app/student-info/student-card";
import {CoursesView} from "@/app/student-info/courses-view";

export default function StudentInfo() {
    return (
        <Container maxWidth="xl">
            <Grid container spacing={2}>
                <Grid size={{xs: 12, md: 4}}>
                    <StudentCard />
                    <Box display="flex" gap={2} sx={{mt: 2}}>
                        <Link href="student-info/courses">
                            <Button variant="contained">My courses</Button>
                        </Link>
                        <Link href="student-info/final-grades">
                            <Button variant="contained">Final grades</Button>
                        </Link>
                    </Box>
                </Grid>
                <Grid size={{xs: 12, md: 8}}>
                    <Box className={styles.coursesContainer}>
                        <Typography variant="h5">Courses</Typography>
                        <CoursesView/>
                    </Box>
                </Grid>
            </Grid>
        </Container>
    );
}
