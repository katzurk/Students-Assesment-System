'use client';

import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper
} from '@mui/material';
import styles from '../StudentInfo.module.css';

export interface GradesInterface {
    name: string;
    grade: number;
}

export const Grades = ({ data }: { data: GradesInterface[] }) => {
    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow className={styles.headerRow}>
                        <TableCell className={styles.headerCell}><strong>Course Name</strong></TableCell>
                        <TableCell className={styles.headerCell}><strong>Final Grade</strong></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {data.map((row, index) => (
                        <TableRow key={index}>
                            <TableCell className={styles.cell}>{row.name}</TableCell>
                            <TableCell className={styles.cell}>{row.grade}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};
