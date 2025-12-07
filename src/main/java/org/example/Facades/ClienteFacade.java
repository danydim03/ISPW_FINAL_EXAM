//package org.example.Facades;
//
//import org.example.exceptions.MissingAuthorizationException;
//import org.example.session_manager.SessionManager;
////import org.example.use_cases.enroll_to_degree_course.EnrollToDegreeCourseStudentFacade;
//import org.example.use_cases.insert_students_grades.InsertStudentsGradesStudentFacade;
//
//public class ClienteFacade {
//    private InsertStudentsGradesStudentFacade insertStudentsGrades;
//    private EnrollToDegreeCourseStudentFacade enrollToDegreeCourse;
//
//    public ClienteFacade(String tokenKey) throws MissingAuthorizationException{
//        SessionManager.getInstance().getSessionUserByTokenKey(tokenKey).getRole().getStudentRole();
//    }
//
//    public void insertStudentsGrades(String tokenKey) throws MissingAuthorizationException{
//        insertStudentsGrades = new InsertStudentsGradesStudentFacade(tokenKey);
//    }
//    public void enrollToDegreeCourse(String tokenKey) throws MissingAuthorizationException{
//        enrollToDegreeCourse = new EnrollToDegreeCourseStudentFacade(tokenKey);
//    }
//
//}
