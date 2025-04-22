import {List, ListItem, ListItemText} from "@mui/material";
import styles from './StudentInfo.module.css';
import React, {useEffect, useState} from "react";
import {ICourse} from "@/app/student-info/courses/components/Course";
import axios from "axios";

export const CoursesView = () => {
    const [courses, setCourses] = useState<ICourse[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        axios.get('http://localhost:8080/student-info/courses', {
            withCredentials: true
        }).then((response) => {
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
        <List>
            {courses.map((course, index) => (
                <ListItem className={styles.course} key={index}>
                    <ListItemText primary={course.name} />
                </ListItem>
            ))}
        </List>
    )
}