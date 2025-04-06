'use client';
import axios from 'axios';

export default function LogoutButton() {
    const handleLogout = async () => {
        try {
            const res = await axios.get('http://localhost:8080/auth/logout', {
                withCredentials: true
            });
            console.log("Wylogowano:", res.data);
            window.location.href = '/login';
        } catch (error) {
            console.error("Logout error", error);
        }
    };

    return <button className="logout_button" onClick={handleLogout}>Logout</button>;
}
