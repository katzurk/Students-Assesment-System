'use client';
import { useState } from 'react';
import axios from 'axios';

export default function RegisterPage() {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [role, setRole] = useState('');


    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await axios.post('http://localhost:8080/register', {
                firstName,
                lastName,
                email,
                password,
                role
            });
            setMessage(res.data);
        } catch (err) {
            setMessage('Registration error');
        }
    };

    return (
        <div className="login-container">
            <h2>Registration</h2>
            <br></br>
            <form onSubmit={handleRegister} className="login-form">
                <input
                    className="input"
                    type="text"
                    value={firstName}
                    onChange={e => setFirstName(e.target.value)}
                    placeholder="FirstName"
                />
                <input
                    className="input"
                    type="text"
                    value={lastName}
                    onChange={e => setLastName(e.target.value)}
                    placeholder="LastName"
                />
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
                    placeholder="Password"
                />
                <select className="input" value={role} onChange={e => setRole(e.target.value)}>
                    <option value="STUDENT">Student</option>
                    <option value="LECTURER">Lecturer</option>
                    <option value="ADMIN">Admin</option>
                </select>
                <button className="button" type="submit">Register</button>
            </form>
            <p>{message}</p>
        </div>
    );
}