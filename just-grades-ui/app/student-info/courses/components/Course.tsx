import {Box, Typography} from "@mui/material";

export interface ICourse {
    id: number;
    name: string;
    ects: number;
}

export interface ICourseRegistered extends ICourse {
    status: string;
}

export const Course = (props: ICourse) => {
    return (
        <Box>
            <Typography variant="h6">{props.name}</Typography>
            <Typography variant="body2">ECTS: {props.ects}</Typography>
        </Box>
    );
}