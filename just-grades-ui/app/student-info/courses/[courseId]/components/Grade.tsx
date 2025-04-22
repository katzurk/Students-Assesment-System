import styles from "@/app/student-info/courses/[courseId]/components/Grade.module.css";
import {Box, Typography} from "@mui/material";
import {ICourse} from "@/app/student-info/courses/components/Course";

export interface ILecturer {
    firstName: string;
    lastName: string;
    academic_title: string;
}

export interface IGrade {
    id: number;
    grade: number;
    receivedDate: Date;
    type: string;
    course: ICourse;
    lecturer: ILecturer;
}

export const Grade = (props: IGrade) => {
    return (
            <Box className={styles.grade} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1}}>
                <Box>
                    <Typography variant="h6">{props.type}</Typography>
                    <Typography variant="body2">Uploaded: {new Date(props.receivedDate).toLocaleDateString()}</Typography>
                    <Typography variant="body2">
                        By: {props.lecturer.academic_title} {props.lecturer.firstName} {props.lecturer.lastName}
                    </Typography>
                </Box>
                <Typography variant="h3">{props.grade}</Typography>
            </Box>
    )
}