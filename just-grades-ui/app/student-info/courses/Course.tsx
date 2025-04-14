import {CourseInterface} from "@/app/student-info/courses/page";
import styles from './Course.module.css';

export const Course = (props: CourseInterface) => {
    return <div className={styles.course}>
        <h2>Name: {props.name}</h2>
        <p>ects: {props.ects}</p>
    </div>
}