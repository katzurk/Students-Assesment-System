import {Alert, Snackbar} from "@mui/material";
import {SyntheticEvent, useEffect, useState} from "react";
import { styled } from "@mui/material/styles";

interface IAlertSnackBar {
    status: string | null,
    onClose: () => void,
}

const CustomAlert = styled(Alert)(() => ({
    backgroundColor: 'var(--foreground)',
    color: 'var(--background)',
    borderRadius: 'var(--border-radius)',
    '& .MuiAlert-icon': {
        color: 'var(--background)',
    },
}));

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
            <CustomAlert onClose={handleClose} sx={{ width: "100%" }}>
                {props.status}
            </CustomAlert>
        </Snackbar>
    );
}