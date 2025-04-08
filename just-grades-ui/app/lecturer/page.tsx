import Image from "next/image";
import styles from "../page.module.css";

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
                <ol>
                    Welcome Lecturer!
                </ol>

            </main>
        </div>
    );
}
