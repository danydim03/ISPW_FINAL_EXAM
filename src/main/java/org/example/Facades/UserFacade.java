package org.example.Facades;

import org.example.beans_general.PendingEventBean;
import org.example.exceptions.*;
import org.example.use_cases.controllers_general.pending_event.PendingEventController;

import java.util.List;

public class UserFacade {

    private PendingEventController controller;

    public UserFacade() {
        this.controller = new PendingEventController();
    }

    public List<PendingEventBean> retrievePendingEvents(String tokenKey) throws DAOException, UserNotFoundException, WrongListQueryIdentifierValue, ObjectNotFoundException, UnrecognizedRoleException, MissingAuthorizationException, WrongDegreeCourseCodeException {
        return controller.retrievePendingEvents(tokenKey);
    }
}
