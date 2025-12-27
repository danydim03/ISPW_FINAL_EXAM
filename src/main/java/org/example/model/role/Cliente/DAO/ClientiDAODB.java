package org.example.model.role.Cliente.DAO;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.role.Cliente.Cliente;
import org.example.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClientiDAODB extends DAODBAbstract<Cliente> implements ClienteDAOInterface {
    private static final String CLIENTE = "CLIENTE";
    private static final String CODICE = "ID";

    protected static ClienteDAOInterface instance;

    private ClientiDAODB() {
    }

    public static synchronized ClienteDAOInterface getInstance() {
        if (instance == null) {
            instance = new ClientiDAODB();
        }
        return instance;
    }

    /**
     * Returns the Student object correlated a given User
     *
     * @param user the User whose Student's data have to be retrieved
     * @return a Student object
     * @throws DAOException          thrown if errors occur while retrieving data
     *                               from persistence layer
     * @throws UserNotFoundException thrown if the given User has not a Student role
     */
    @Override
    public Cliente getClienteByUser(User user) throws DAOException, UserNotFoundException, PropertyException,
            ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue {
        System.out.println("DEBUG: Cerco Cliente con ID = '" + user.getCodiceFiscale() + "'");
        return getQuery(
                CLIENTE, // da cambiare

                List.of(CODICE),
                List.of(user.getId()),
                List.of(user));
    }

    @Override
    public void insert(Cliente cliente)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        // insertQuery(
        // CLIENTE,
        // List.of(student.getCodiceFiscale(),student.getMatricola())
        // ); da cambiare
    }

    @Override
    public void delete(Cliente cliente) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                CLIENTE,
                List.of(CODICE),
                List.of(cliente.getCodiceFiscale()));
    }

    @Override
    public void update(Cliente cliente)
            throws PropertyException, ResourceNotFoundException, DAOException, MissingAuthorizationException {
        // updateQuery(
        // CLIENTE,
        // List.of("matricola"),
        // List.of(student.getMatricola()), DA CAMBIARE
        // List.of(CODICE_FISCALE),
        // List.of(student.getCodiceFiscale())
        // );
    }

    protected Cliente queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException,
            PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, ObjectNotFoundException {
        Cliente Cliente = new Cliente((User) objects.get(0), rs.getString("ID"));
        // student.setDegreeCourseEnrollments(DegreeCourseEnrollmentLazyFactory.getInstance().getDegreeCourseEnrollmentsByStudent(student));
        // student.setTitles(TitleLazyFactory.getInstance().getTitlesByStudent(student));
        // student.setSubjectCourseEnrollments(SubjectCourseEnrollmentLazyFactory.getInstance().getSubjectCourseEnrollmentsByStudent(student));
        return Cliente;
    }

    //
    // @Override
    protected String setGetListQueryIdentifiersValue(Cliente cliente, int valueNumber) throws DAOException {
        return null;
    }

}
