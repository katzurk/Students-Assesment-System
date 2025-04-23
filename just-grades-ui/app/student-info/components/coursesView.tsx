import {List, ListItem, ListItemText} from "@mui/material";
import styles from '../StudentInfo.module.css';
import React, {useEffect, useState} from "react";
import {ICourse} from "@/app/student-info/courses/components/Course";
import {StudentService} from "@/app/services/StudentService";
import {useStudentCourses} from "@/app/student-info/hooks/useStudentCourses";

export const CoursesView = () => {
    const {courses, loading, error} = useStudentCourses();

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