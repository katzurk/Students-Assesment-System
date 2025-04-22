import React, {useEffect, useState} from "react";
import axios from "axios";
import {Card, CardContent, Typography} from "@mui/material";

interface IStudent {
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
        axios.get('http://localhost:8080/student-info', {
            withCredentials: true
        })
            .then((response) => {
                setStudent(response.data);
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
        <Card>
            <CardContent>
                <Typography variant="h4">Student Info</Typography>
                <Typography variant="h6">{student?.firstName} {student?.lastName}</Typography>
                <Typography>Student Number: {student?.studentNumber}</Typography>
                <Typography>Status: {student?.status}</Typography>
                <Typography>Library Card Number: {student?.libraryCardNumber}</Typography>
            </CardContent>
        </Card>
    )
}