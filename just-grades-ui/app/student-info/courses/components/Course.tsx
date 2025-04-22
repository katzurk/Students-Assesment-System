import {ICourse} from "@/app/student-info/courses/page";
import styles from './Course.module.css';
import {Box, Button, Card, CardActions, CardContent, ListItem, ListItemText, Typography} from "@mui/material";
import Link from "next/link";

export const Course = (props: ICourse) => {
    return (
        <Box className={styles.course} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1}}>
            <Box>
                <Typography variant="h6">{props.name}</Typography>
                <Typography variant="body2">ECTS: {props.ects}</Typography>
            </Box>
            <Box>
                <Button size="small" variant="contained" sx={{ mr: 1 }}>Classes</Button>
                <Link href={`courses/${props.id}`}>
                    <Button size="small" variant="contained">Grades</Button>
                </Link>
            </Box>
        </Box>
    );
}