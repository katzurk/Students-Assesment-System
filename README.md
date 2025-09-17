# JustGrades - Student Assesment System

- [Polish](README-PL.md)

---

## Authors:
* Diana Pelin
* Alesia Filinkova
* Katarzyna Kanicka
* Weronika Maślana

## Environment installation
Install NVM:

```curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.1/install.sh | bash```

Node.js 22:

```nvm install 22 && nvm use 22```

pnpm:

```npm install -g pnpm@latest-10```

Dependencies:

```pnpm install```

---

## Running the application

**Just-grades-rest (Spring)**:

```clear && ./mvnw spring-boot:run```

In your browser, go to:

```localhost:8080```

**Just-grades-ui (React)**:

```pnpm dev```

In your browser, go to:

```localhost:3000```

Swagger documentation: 

```http://localhost:8080/swagger-ui/index.html#/ ```   

---

## Project implementation:
* Relational database: Oracle
* Application code written in Java using the Spring framework and Hibernate library
* Frontend: Next.js, React - Typescript

---

## Functionalities

### 1. User login

---

### Roles and permissions

#### Student
3. View of final grades  
4. View of points earned in a course  
5. Enrolling on courses
6. Withdrawing from courses  

#### Lecturer
7. Grading students 
8. Viewing student profiles for a given course  
   - collection of points for given student enrolled in the course  
9. Creating a course outline  
   - e.g., number of labs and points for assignments, whether there is an exam and how many points are needed to pass  
10. Closing the course and automatically calculating the final grade  
11. Opening and closing the semester  
12. Opening and closing the course registration  

#### Both
13. Displaying reports  
   - e.g., a breakdown of test scores, distribution of final grades among students in a given course  


---

## Project stages
1. Defining the conceptual model,
2. Defining the logical data model for the relational database and the application design, with part of the application logic to be implemented in the form of stored procedures in the database,
3. Creating the physical model, implementing the database and application, providing test data, testing.
