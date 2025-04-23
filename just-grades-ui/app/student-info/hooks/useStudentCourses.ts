import { useEffect, useState } from "react";
import { StudentService } from "@/app/services/StudentService";
import { ICourse } from "@/app/student-info/courses/components/Course";

export const useStudentCourses = () => {
    const [courses, setCourses] = useState<ICourse[] | []>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        StudentService.getAllStudentCourses()
            .then(setCourses)
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, []);

    return { courses, loading, error };
};
