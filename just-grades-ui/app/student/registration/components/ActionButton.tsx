import {Box, Button} from "@mui/material";
import PersonRemoveIcon from "@mui/icons-material/PersonRemove";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import HowToRegIcon from '@mui/icons-material/HowToReg';
import React from "react";

interface IActionButton {
    courseId: number,
    status: string,
    onDeregister: (courseId: number) => void,
    onRegister: (courseId: number) => void,
}

export const ActionButton = (props: IActionButton) => {
    switch (props.status) {
        case "application submitted":
            return (
                <Box>
                    <Button
                        onClick={() => props.onDeregister(props.courseId)}
                        variant="contained"
                        color="error"
                        size="large"
                        startIcon={<PersonRemoveIcon />}
                        sx={{ width: 200 }}
                    >
                        Deregister
                    </Button>
                </Box>
            );
        case "ACTIVE":
            return (
                <Box>
                    <Button
                        variant="contained"
                        color="success"
                        size="large"
                        startIcon={<HowToRegIcon />}
                        sx={{ width: 200,
                            ":disabled": {
                                backgroundColor: "#4caf50",
                                color: "#fff",
                                opacity: 0.8,
                            }
                            ,}}
                        disabled
                    >
                        Registered
                    </Button>
                </Box>
            )
        default:
            return (
                <Box>
                    <Button
                        onClick={() => props.onRegister(props.courseId)}
                        variant="contained"
                        color="success"
                        size="large"
                        startIcon={<PersonAddIcon />}
                        sx={{ width: 200 }}
                    >
                        Register
                    </Button>
                </Box>
            );
    }
}