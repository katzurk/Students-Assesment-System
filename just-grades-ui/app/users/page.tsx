"use client"
import { useEffect, useState } from 'react';
import styles from './UserTable.module.css';

interface User {
  firstName: string;
  lastName: string;
  passwordHash: string;
  _links: {
    self: { href: string };
    user: { href: string };
  };
}

export default function UsersTable() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch('http://localhost:8080/users')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        setUsers(data || []);
        setLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading data...</p>;
  if (error) return <p>--Error: {error}</p>;

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Users list</h1>
      <table className={styles.table}>
        <thead>
          <tr className={styles.headerRow}>
            <th className={styles.headerCell}>Name</th>
            <th className={styles.headerCell}>Surname</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user, index) => (
            <tr key={index} className={styles.row}>
              <td className={styles.cell}>{user.firstName}</td>
              <td className={styles.cell}>{user.lastName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
