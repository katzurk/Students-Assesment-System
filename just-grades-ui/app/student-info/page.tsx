"use client"
import React, {useEffect, useState} from "react";
import Link from "next/link";
import {Container, Grid, Button, Box, Typography} from "@mui/material";
import {StudentCard} from "@/app/student-info/student-card";

export default function StudentInfo() {
    return (
        <Container maxWidth="xl">
            <Grid container spacing={2}>
                <Grid size={4}>
                    <StudentCard />
                    <br></br>
                    <Box display="flex" gap={2}>
                        <Link href="student-info/courses">
                            <Button variant="contained">My courses</Button>
                        </Link>
                        <Link href="student-info/final-grades">
                            <Button variant="contained">Final grades</Button>
                        </Link>
                    </Box>
                </Grid>
                <Grid size={6}>
                    <Typography variant="h5">Courses</Typography>
                </Grid>
            </Grid>
        </Container>
    );
}
