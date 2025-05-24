'use client';

import Image from "next/image";
import styles from "./admin.module.css";
import { useState } from "react";

export default function Home() {
    const [message, setMessage] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleOpenSemester = async () => {
        setMessage(null);
        setError(null);
        try {
            const response = await fetch("http://localhost:8080/api/admin/open-semester", {
                method: "POST",
                credentials: "include"
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                const errorMessage = errorData?.message || `Error: ${response.statusText}`;
                setError(errorMessage);
                return;
            }

            setMessage("Semester is opened successfully");
        } catch (err: any) {
            setError(err.message || "Unexpected error");
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

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                const errorMessage = errorData?.message || `Error: ${response.status} ${response.statusText}`;
                setError(errorMessage);
                return;
            }

            setMessage("Semester is closed successfully");
        } catch (err: any) {
            setError(err.message || "Unexpected error");
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
            setMessage("All courses are closed sucsessfully");
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
                <button className={styles.button} onClick={handleOpenSemester}>Open Semester</button>
                <button className={styles.button} onClick={handleCloseSemester}>Close Semester</button>
                <button className={styles.button} onClick={handleCloseAllCourses}>Close All Courses</button>

                {message && <p style={{ color: "green", marginTop: "16px" }}>{message}</p>}
                {error && <p style={{ color: "red", marginTop: "16px" }}>{error}</p>}
            </main>
        </div>
    );
}

