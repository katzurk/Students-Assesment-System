import Image from "next/image";
import styles from "./admin.module.css";
import Link from "next/link";

export default function Home() {
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
                <button className={styles.button}>Open Semester</button>
                <button className={styles.button}>Close Semester</button>
                <button className={styles.button}>Close All Courses</button>
            </main>
        </div>
    );
}
