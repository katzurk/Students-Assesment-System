"use client"
import Image from "next/image";
import styles from "../page.module.css";
import React, {useEffect, useState} from "react";
import axios from "axios";
import Link from "next/link";

interface Student {
    userId: number;
    firstName: string;
    lastName: string;
    studentNumber: string;
    status: string;
    libraryCardNumber: string;
}

export default function StudentInfo() {
    const [student, setStudent] = useState<Student | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        axios.get('http://localhost:8080/student-info')
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
        <main className={styles.main}>
                <div>
                    <h1>Student Info</h1>
                    <p>{student?.firstName} {student?.lastName}</p>
                    <p>Student Number: {student?.studentNumber}</p>
                    <p>Status: {student?.status}</p>
                    <p>Library Card Number: {student?.libraryCardNumber}</p>
                </div>
            <Link href="student-info/courses">
                <button className="button">My courses</button>
            </Link>
        </main>
    );
}
