'use client';

import { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/navigation';

export default function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const router = useRouter(); // ⬅️ użycie routera

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await axios.post('http://localhost:8080/login', {
                email,
                password
            }, {
                withCredentials: true
            });

            const role = res.data;

            // ✅ ZAMIANA window.location.href → router.push
            switch (role) {
                case 'STUDENT':
                    router.push('/student-info');
                    break;
                case 'LECTURER':
                    router.push('/courses');
                    break;
                case 'ADMIN':
                    router.push('/admin');
                    break;
                default:
                    setMessage('Nieznana rola użytkownika');
            }
        } catch {
            setMessage('Login Error');
        }
    };

    return (
        <div className="login-container">
            <h2>Logowanie</h2>
            <br />
            <form onSubmit={handleLogin} className="login-form">
                <input
                    className="input"
                    type="email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    placeholder="Email"
                />
                <input
                    className="input"
                    type="password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    placeholder="Hasło"
                />
                <button className="button" type="submit">Log in</button>
            </form>
            <p>{message}</p>
        </div>
    );
}
