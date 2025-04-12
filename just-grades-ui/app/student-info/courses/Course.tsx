import {CourseInterface} from "@/app/student-info/courses/page";


export const Course = (props: CourseInterface) => {
    return <div>
        <h1>Name {props.name}</h1>
        <p>ects {props.ects}</p>
    </div>
}