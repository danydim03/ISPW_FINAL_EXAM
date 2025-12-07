// java
package org.example.dao_manager;

//import org.example.model.association_classes.degree_course_enrollment.DegreeCourseEnrollmentDAODB;
//import org.example.model.association_classes.degree_course_enrollment.DegreeCourseEnrollmentDAOInterface;
//import org.example.model.association_classes.exam_enrollment.ExamEnrollmentDAODB;
//import org.example.model.association_classes.exam_enrollment.ExamEnrollmentDAOInterface;
//import org.example.model.association_classes.subject_course_assignment.SubjectCourseAssignmentDAODB;
//import org.example.model.association_classes.subject_course_assignment.SubjectCourseAssignmentDAOInterface;
//import org.example.model.association_classes.subject_course_enrollment.AbstractSubjectCourseEnrollmentDAO;
//import org.example.model.association_classes.subject_course_enrollment.SubjectCourseEnrollmentDAODB;
//import org.example.model.association_classes.test_reservation.TestReservationDAOAbstract;
//import org.example.model.association_classes.test_reservation.TestReservationDAODB;
//import org.example.model.degree_course.DegreeCourseDAODB;
//import org.example.model.degree_course.DegreeCourseDAOInterface;
//import org.example.model.exam.ExamDAODB;
//import org.example.model.exam.ExamDAOInterface;
//import org.example.model.exam_result.ExamResultDAODB;
//import org.example.model.exam_result.ExamResultDAOInterface;
//import org.example.model.pending_events.PendingEventDAODB;
//import org.example.model.pending_events.PendingEventDAOInterface;
//import org.example.model.role.Kebabbaro.AbstractKebabbaroDAO;
//import org.example.model.role.Kebabbaro.KebabbaroDAODB;
//import org.example.model.role.Amministratore.AbstractAmministratoreDAO;
//import org.example.model.role.Amministratore.AmministratoreDAODB;

//import org.example.model.subject_course.SubjectCourseDAODB;
//import org.example.model.subject_course.SubjectCourseDAOInterface;
//import org.example.model.test.TestDAOAbstract;
//import org.example.model.test.TestDAODB;
//import org.example.model.timer.TimerDAODB;
//import org.example.model.timer.TimerDAOInterface;
//import org.example.model.title.TitleDAODB;
//import org.example.model.title.TitleDAOInterface;
import org.example.model.role.Amministratore.AmministratoreDAODB;
import org.example.model.role.Amministratore.AmministratoreDAOInterface;
import org.example.model.role.Cliente.ClientiDAODB;
import org.example.model.role.Kebabbaro.KebabbaroDAODB;
import org.example.model.role.Kebabbaro.KebabbaroDAOInterface;
import org.example.model.user.UserDAODB;
import org.example.model.user.UserDAOInterface;
import org.example.model.role.Cliente.ClienteDAOInterface;

public class DAOFactoryDB extends DAOFactoryAbstract {
    //    @Override
//    public SubjectCourseAssignmentDAOInterface getCourseAssignmentDAO(){
//        return SubjectCourseAssignmentDAODB.getInstance();
//    }
//    @Override
//    public DegreeCourseEnrollmentDAOInterface getDegreeCourseEnrollmentDAO(){
//        return DegreeCourseEnrollmentDAODB.getInstance();
//    }
//    @Override
//    public ExamEnrollmentDAOInterface getExamEnrollmentDAO(){
//        return ExamEnrollmentDAODB.getInstance();
//    }
//    @Override
//    public AbstractSubjectCourseEnrollmentDAO getSubjectCourseEnrollmentDAO(){
//        return SubjectCourseEnrollmentDAODB.getInstance();
//    }
//    @Override
//    public DegreeCourseDAOInterface getDegreeCourseDAO(){
//        return DegreeCourseDAODB.getInstance();
//    }
//    @Override
//    public ExamDAOInterface getExamDAO(){
//        return ExamDAODB.getInstance();
//    }
//    @Override
//    public ExamResultDAOInterface getExamResultDAO(){
//        return ExamResultDAODB.getInstance();
//    }
//    @Override
//    public PendingEventDAOInterface getPendingEventDAO(){
//        return PendingEventDAODB.getInstance();
//    }
    @Override
    public KebabbaroDAOInterface getKebabbaroDAO(){
        return KebabbaroDAODB.getInstance();
    }
    @Override
   public AmministratoreDAOInterface  getAmministratoreDAO(){
        return AmministratoreDAODB.getInstance();
    }
//    @Override
    public ClienteDAOInterface getStudentDAO(){
        return ClientiDAODB.getInstance();
    }

    @Override
    public ClienteDAOInterface getClienteDAO(){
        return ClientiDAODB.getInstance();
    }
//    @Override
//    public SubjectCourseDAOInterface getSubjectCourseDAO(){
//        return SubjectCourseDAODB.getInstance();
//    }
//    @Override
//    public TimerDAOInterface getTimerDAO(){
//        return TimerDAODB.getInstance();
//    }
//    @Override
//    public TitleDAOInterface getTitleDAO() {
//        return TitleDAODB.getInstance();
//    }

    @Override
    public UserDAOInterface getUserDAO(){
        return UserDAODB.getInstance();
    }

//    @Override
//    public TestDAOAbstract getTestDAO() {
//        return TestDAODB.getInstance();
//    }
//
//    @Override
//    public TestReservationDAOAbstract getTestReservationDAO() {
//        return TestReservationDAODB.getInstance();
//    }
//
//    @Override
//    public SubjectCourseAssignmentDAOInterface getSubjectCourseAssignmentDAO() {
//        return SubjectCourseAssignmentDAODB.getInstance();
//    }
}
