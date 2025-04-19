'use client';

import Image from 'next/image';
import { Box, Typography, Button } from '@mui/material';
import styles from './page.module.css';

export default function Home() {
  return (
    <Box className={styles.page}>
      <Box className={styles.logo_main}>
        <Image
          src="/justgrades_logo.png"
          alt="JustGrades logo"
          width={395}
          height={288}
          priority
        />
      </Box>

      <Typography component="main" className={styles.main}>
        Welcome!
      </Typography>

      <Button
        href="/login"
        className={styles.primary}
      >
        Login
      </Button>
    </Box>
  );
}
