"use client"
import React, {useEffect, useState} from "react";
import axios from "axios";
import {Course} from "./Course"
import styles from './Course.module.css';

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
        axios.get('http://localhost:8080/student-info/courses', {
            withCredentials: true
        })
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
        <div className={styles.container}>
            <div>
                <h1>Student Courses</h1>
                {courses?.map((course: CourseInterface) => (
                    <Course key={course.id} {...course}/>
                ))}
            </div>
        </div>
    );
}
