import {List, ListItem, ListItemText, Typography} from "@mui/material";
import styles from '../StudentInfo.module.css';
import React from "react";
import {useStudentCourses} from "@/hooks/useStudentCourses";

export const StudentCoursesView = () => {
    const {courses, loading, error} = useStudentCourses();

    if (loading) return <p>Loading data...</p>;
    if (error) return <p>--Error: {error}</p>;

    return (
        <List>
            {courses.length === 0 ? (
                    <Typography variant="h6">
                        You are not registered for any courses.
                    </Typography>
            ):
            courses.map((course, index) => (
                <ListItem className={styles.course} key={index}>
                    <ListItemText primary={course.name} />
                </ListItem>
            ))}
        </List>
    )
}