'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Grades, GradesInterface } from './Grades';

export default function StudentFinalGrades() {
    const [grades, setGrades] = useState<GradesInterface[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        axios.get('http://localhost:8080/student-info/final-grades', {
            withCredentials: true
        })
            .then((response) => {
                const data = response.data;
                const converted: GradesInterface[] = Object.entries(data).map(([name, grade]) => ({
                    name,
                    grade: grade as number
                }));
                setGrades(converted);
                setLoading(false);
            })
            .catch((error) => {
                setError(error.message);
                setLoading(false);
            });
    }, []);

    if (loading) return <p>Loading data...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div style={{ padding: '2rem' }}>
            <h1>Final Grades</h1>
            <Grades data={grades} />
        </div>
    );
}
