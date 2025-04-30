import React, {useEffect, useState} from "react";
import {Box, Card, CardContent, Typography} from "@mui/material";
import {StudentService} from "@/services/StudentService";
import styles from '../StudentInfo.module.css';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

export interface IStudent {
    userId: number;
    firstName: string;
    lastName: string;
    studentNumber: string;
    status: string;
    libraryCardNumber: string;
}

export const StudentCard = () => {
    const [student, setStudent] = useState<IStudent | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        StudentService.getStudentInfo()
            .then(setStudent)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Loading data...</p>;
    if (error) return <p>--Error: {error}</p>;

    return (
        <Card>
            <CardContent  className={styles.studentCard}>
                <Box component="span" sx={{ display: "flex", alignItems: "center", gap: 1}}>
                    <AccountCircleIcon sx={{fontSize: 40}}/>
                    <Typography variant="h4">{student?.firstName} {student?.lastName}</Typography>
                </Box>
                <Typography variant="h6">Student</Typography>
                <Typography>Student Number: {student?.studentNumber}</Typography>
                <Typography>Status: {student?.status}</Typography>
                <Typography>Library Card Number: {student?.libraryCardNumber}</Typography>
            </CardContent>
        </Card>
    )
}