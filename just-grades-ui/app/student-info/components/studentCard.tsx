import React, {useEffect, useState} from "react";
import {Card, CardContent, Typography} from "@mui/material";
import {StudentService} from "@/app/services/StudentService";
import styles from '../StudentInfo.module.css';

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
                <Typography variant="h4">Student Info</Typography>
                <Typography variant="h6">{student?.firstName} {student?.lastName}</Typography>
                <Typography>Student Number: {student?.studentNumber}</Typography>
                <Typography>Status: {student?.status}</Typography>
                <Typography>Library Card Number: {student?.libraryCardNumber}</Typography>
            </CardContent>
        </Card>
    )
}