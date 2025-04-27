import styles from './Course.module.css';
import {Box, Button, Typography} from "@mui/material";
import Link from "next/link";

export interface ICourse {
    id: number;
    name: string;
    ects: number;
}

export const Course = (props: ICourse) => {
    return (
        <Box className={styles.course} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1}}>
            <Box>
                <Typography variant="h6">{props.name}</Typography>
                <Typography variant="body2">ECTS: {props.ects}</Typography>
            </Box>
            <Box>
                <Button size="small" variant="contained" className={styles.classes_button}>Classes</Button>
                <Link href={`courses/${props.id}`}>
                    <Button size="small" variant="contained" className={styles.grades_button} >Grades</Button>
                </Link>
            </Box>
        </Box>
    );
}