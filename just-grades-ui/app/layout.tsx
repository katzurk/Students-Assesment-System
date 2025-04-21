import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Link from 'next/link';
import Image from "next/image";
import LogoutButton from './LogoutButton';
import axios from 'axios';

axios.defaults.withCredentials = true;

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "JustGrades",
  description: "app for grading",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <nav style={{ padding: '10px'}}>
          <a href="/">
            <Image
              className="logo"
              src="/justgrades_logo.png"
              alt="JustGrades logo"
              width= {109}
              height= {80}
              priority
            />
          </a>
          <Link href="/login">Login</Link> |{' '}
          <Link href="/register">Registration</Link> |{' '}
          <LogoutButton  />
        <hr className="logo_line"></hr>
        </nav>
        <main>{children}</main>
      </body>
    </html>
  );
}