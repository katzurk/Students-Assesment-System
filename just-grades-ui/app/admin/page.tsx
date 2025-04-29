import Image from "next/image";
import styles from "../page.module.css";
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
                <ol>
                    Welcome Admin!
                </ol>
                <Link href="/student-special">
                    <button>add student specialization</button>
                </Link>

            </main>
        </div>
    );
}
