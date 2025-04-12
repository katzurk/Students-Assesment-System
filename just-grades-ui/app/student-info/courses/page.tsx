"use client"
import Image from "next/image";
import styles from "../../page.module.css";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {Course} from "./Course"

export interface CourseInterface {
    id: number;
    name: string;
    ects: number;
}

export default function StudentCourses() {
    const [courses, setCourses] = useState<CourseInterface[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        axios.get('http://localhost:8080/student-info/courses')
            .then((response) => {
                setCourses(response.data || []);
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
                <h1>Student Courses</h1>
                {courses?.map((course: CourseInterface) => (
                    <Course key={course.id} {...course}/>
                ))}
            </div>
        </main>
    );
}
