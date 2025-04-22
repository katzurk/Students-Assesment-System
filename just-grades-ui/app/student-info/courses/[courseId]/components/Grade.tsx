import styles from "@/app/student-info/courses/[courseId]/components/Grade.module.css";
import {Box, Button, Typography} from "@mui/material";
import {IGrade} from "@/app/student-info/courses/[courseId]/page";

export const Grade = (props: IGrade) => {
    return (
            <Box className={styles.grade} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', p: 1}}>
                <Box>
                    <Typography variant="h6">{props.type}</Typography>
                    <Typography variant="body2">Uploaded: {new Date(props.receivedDate).toLocaleDateString()}</Typography>
                </Box>
                <Typography variant="h3">{props.grade}</Typography>
            </Box>
    )
}