import axios from "axios";
import {IStudent} from "@/app/student-info/components/studentCard"
import {ICourse} from "@/app/student-info/courses/components/Course";
import {useEffect} from "react";
import {IGrade} from "@/app/student-info/courses/[courseId]/components/Grade";

async function getStudentInfo(): Promise<IStudent> {
    const res = await axios.get('http://localhost:8080/student-info', {
        withCredentials: true
    });
    return res.data || [];
}

async function getAllStudentCourses(): Promise<ICourse[]> {
    const res = await axios.get('http://localhost:8080/student-info/courses', {
        withCredentials: true
    });
    return res.data || [];
}

async function getStudentGradesByCourse(courseId: string | Array<string> | undefined): Promise<IGrade[]> {
    const res = await axios.get(`http://localhost:8080/student-info/courses/${courseId}`, {
        withCredentials: true
    });
    return res.data || [];
}

export const StudentService = {
    getStudentInfo,
    getAllStudentCourses,
    getStudentGradesByCourse,
};