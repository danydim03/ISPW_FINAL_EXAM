//package org.example.model.user;
//
//public class UserDAODBTest {
//
//
//    public void testExistsUserByEmailSafe_UtenteEsistente() {
//        // Usa un'email che sai esistere nel database per questo test
//        UserDAODB dao = (UserDAODB) UserDAODB.getInstance();
//        boolean result = dao.existsUserByEmailSafe("mario.rossi@example.com");
//
//       // assertTrue(result, "L'utente dovrebbe esistere");
//    }
//
//    public void testExistsUserByEmailSafe_UtenteNonEsistente() {
//        UserDAODB dao = (UserDAODB) UserDAODB.getInstance();
//        boolean result = dao.existsUserByEmailSafe("mario.rossi@example.com");
//
//      //  assertFalse(result, "L'utente non dovrebbe esistere");
//    }
//
//    public void testExistsUserByEmailSafe_EmailNull() {
//        UserDAODB dao = (UserDAODB) UserDAODB.getInstance();
//        boolean result = dao.existsUserByEmailSafe(null);
//
//        //assertFalse(result, "Con email null dovrebbe restituire false");
//    }
//}