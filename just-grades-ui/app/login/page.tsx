'use client';
import { useState } from 'react';
import axios from 'axios';

export default function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await axios.post('http://localhost:8080/auth/login', { email, password }, {
                withCredentials: true
            });
            setMessage(res.data);
        } catch {
            setMessage('Login Error');
        }
    };

    return (
        <div className="login-container">
            <h2>Logowanie</h2>
            <br></br>
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
