import Image from "next/image";
import styles from "./page.module.css";

export default function Home() {
  return (
    <div className={styles.page}>
      <Image
        className={styles.logo_main}
        src="/justgrades_logo.png"
        alt="JustGrades logo"
        width= {395}
        height= {288}
        priority
      />
      <main className={styles.main}>
        <ol>
            Welcome!
        </ol>

      </main>
      <a
        className={styles.primary}
        href="/student"
        rel="noopener noreferrer"
      >
        Student
      </a>
      <a
        className={styles.primary}
        href="/courses"
        rel="noopener noreferrer"
      >
        Lecturer
      </a>
    </div>
  );
}
