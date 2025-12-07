package org.example.dao_manager;

import org.example.PropertiesHandler;
import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.PersistenceTypeEnum;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.role.Cliente.Cliente;
import org.example.model.role.Cliente.ClienteDAOInterface;
import org.example.model.ordine.OrdineDAOInterface;
//import org.example.model.timer.TimerDAOInterface;
//import org.example.model.association_classes.degree_course_enrollment.DegreeCourseEnrollmentDAOInterface;
//import org.example.model.association_classes.exam_enrollment.ExamEnrollmentDAOInterface;
//import org.example.model.association_classes.subject_course_assignment.SubjectCourseAssignmentDAOInterface;
//import org.example.model.association_classes.subject_course_enrollment.AbstractSubjectCourseEnrollmentDAO;
//import org.example.model.association_classes.test_reservation.TestReservationDAOAbstract;
//import org.example.model.degree_course.DegreeCourseDAOInterface;
//import org.example.model.exam.ExamDAOInterface;
//import org.example.model.exam_result.ExamResultDAOInterface;
//import org.example.model.pending_events.PendingEventDAOInterface;
import org.example.model.role.Kebabbaro.Kebabbaro;
import org.example.model.role.Kebabbaro.KebabbaroDAOInterface;
import org.example.model.role.Amministratore.AmministratoreDAOInterface;
//import org.example.model.role.student.StudentDAOInterface;
//import org.example.model.subject_course.SubjectCourseDAOInterface;
//import org.example.model.test.TestDAOAbstract;
//import org.example.model.title.TitleDAOInterface;


import org.example.model.user.UserDAOInterface;

public abstract class                                                                                                                DAOFactoryAbstract {

    private static DAOFactoryAbstract me = null;

    protected DAOFactoryAbstract() {

    }

    public static synchronized DAOFactoryAbstract getInstance() throws ResourceNotFoundException, PropertyException {
        if (me == null) {
            PersistenceTypeEnum persistenceType = PersistenceTypeEnum.getPersistenceTypeByValue(PropertiesHandler.getInstance().getProperty("persistence_type"));
            if (persistenceType != null)
                switch (persistenceType) {
                    case DB -> me = new DAOFactoryDB();
                    //  case MEM -> me = new DAOFactoryMEM();
                    default -> throw new PropertyException(ExceptionMessagesEnum.UNEXPECTED_PROPERTY_NAME.message);
                }
            else
                throw new ResourceNotFoundException(ExceptionMessagesEnum.RESOURCE_NOT_FOUND.message);
        }
        return me;
    }

    public abstract KebabbaroDAOInterface getKebabbaroDAO();
   public abstract AmministratoreDAOInterface getAmministratoreDAO();
    public abstract ClienteDAOInterface getClienteDAO();
    public abstract OrdineDAOInterface getOrdineDAO();

    public abstract UserDAOInterface getUserDAO();

}