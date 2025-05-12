'use client';

import Image from "next/image";
import styles from "./admin.module.css";
import Link from "next/link";
import { useState } from "react";

export default function Home() {
    const [message, setMessage] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const cleanOracleError = (message: string): string => {
        const match = message.match(/ORA-20001:\s*(.*)/);
        if (match) {
            return match[1].trim();
        }
        return message.split("\n")[0];
    };
    
    const handleOpenSemester = async () => {
        setMessage(null);
        setError(null);
        try {
            const response = await fetch("http://localhost:8080/api/admin/open-semester", {
                method: "POST",
                credentials: "include"
            });
    
            const text = await response.text();

            if (text.includes("ORA-20001")) {
                throw new Error(cleanOracleError(text));
            }
    
            setMessage(text);
        } catch (err: any) {
            setError(err.message);
        }
    };
    
    
    const handleCloseSemester = async () => {
        setMessage(null);
        setError(null);
        try {
            const response = await fetch("http://localhost:8080/api/admin/close-semester", {
                method: "POST",
                credentials: "include"
            });
    
            const text = await response.text();

            if (text.includes("ORA-20001")) {
                throw new Error(cleanOracleError(text));
            }
    
            setMessage(text);
        } catch (err: any) {
            setError(err.message);
        }
    };

    const handleCloseAllCourses = async () => {
        setMessage(null);
        setError(null);
        try {
            const response = await fetch("http://localhost:8080/api/admin/close-all-courses", {
                method: "POST",
                credentials: "include"
            });
    
            const text = await response.text();
    
            if (text.includes("ORA-20001")) {
                throw new Error(cleanOracleError(text));
            }
    
            setMessage(text);
        } catch (err: any) {
            setError(err.message);
        }
    };
    
    
    
    return (
        <div className={styles.page}>
            <Image
                className={styles.logo_main}
                src="/justgrades_logo.png"
                alt="JustGrades logo"
                width={395}
                height={288}
                priority
            />
            <main className={styles.main}>
                <p>Welcome Admin!</p>

                <Link href="/student-special">
                    <button className={styles.button}>Add Student Specialization</button>
                </Link>

                <button className={styles.button} onClick={handleOpenSemester}>Open Semester</button>
                <button className={styles.button} onClick={handleCloseSemester}>Close Semester</button>
                <button className={styles.button} onClick={handleCloseAllCourses}>Close All Courses</button>

                {message && <p style={{ color: "green", marginTop: "16px" }}>{message}</p>}
                {error && <p style={{ color: "red", marginTop: "16px" }}>{error}</p>}
            </main>
        </div>
    );
}

