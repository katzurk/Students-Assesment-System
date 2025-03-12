import Image from "next/image";
import styles from "./page.module.css";

export default function Home() {
  return (
    <div className={styles.page}>
      <Image
        className={styles.logo}
        src="/justgrades_logo.png"
        alt="JustGrades logo"
        width= {395}
        height= {288}
        priority
      />
      <hr className={styles.logo_line}></hr>
      <main className={styles.main}>
        <ol>
            Welcome!
        </ol>

      </main>
      <a
        className={styles.primary}
        href="/users"
        rel="noopener noreferrer"
      >
        Users
      </a>
    </div>
  );
}
