package org.example;


import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.FrontEndTypeEnum;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.loggers_general.GeneralLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.model.user.UserDAODB;
import org.example.PropertiesHandler;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public  abstract  class MAIN_TEST extends Application {
    //test ci analysis
    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());


    public static void testExistsUserByEmailSafe_UtenteEsistente() {
        // Usa un'email che sai esistere nel database per questo test
        UserDAODB dao = (UserDAODB) UserDAODB.getInstance();
        boolean result = dao.existsUserByEmailSafe("mario.rossi@example.com");

        // assertTrue(result, "L'utente dovrebbe esistere");
    }

    public void testExistsUserByEmailSafe_UtenteNonEsistente() {
        UserDAODB dao = (UserDAODB) UserDAODB.getInstance();
        boolean result = dao.existsUserByEmailSafe("mario.rossi@example.com");

        //  assertFalse(result, "L'utente non dovrebbe esistere");
    }

    public void testExistsUserByEmailSafe_EmailNull() {
        UserDAODB dao = (UserDAODB) UserDAODB.getInstance();
        boolean result = dao.existsUserByEmailSafe(null);

        //assertFalse(result, "Con email null dovrebbe restituire false");
    }



    public static void main(String[] args) {

        testExistsUserByEmailSafe_UtenteEsistente();


    }

}






