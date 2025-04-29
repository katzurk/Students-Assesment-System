import {Alert, Snackbar} from "@mui/material";
import {SyntheticEvent, useEffect, useState} from "react";

interface IAlertSnackBar {
    status: string | null,
    onClose: () => void,
}

export const AlertSnackbar = (props: IAlertSnackBar) => {
    const [open, setOpen] = useState<boolean>(false);

    useEffect(() => {
        if (props.status) {
            setOpen(true);
        }
    }, [props.status]);

    const handleClose = (event: SyntheticEvent | Event, reason?: string) => {
        if (reason === "clickaway") return;
        setOpen(false)
        props.onClose;
    };

    return (
        <Snackbar
            open={open}
            autoHideDuration={5000}
            onClose={handleClose}
            anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        >
            <Alert onClose={handleClose} sx={{ width: "100%" }}>
                {props.status}
            </Alert>
        </Snackbar>
    );
}