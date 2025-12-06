package org.example.graphic_controllers_general.homepages;

import org.example.PageNavigationController;
//import it.uniroma2.dicii.ispw.gradely.use_cases.insert_students_grades.InsertStudentsGradesSecretaryFacade;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AmministratoreHomepageGraphicController implements Initializable {

    //private InsertStudentsGradesSecretaryFacade facade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Automatically called after FXMLLoader::load() method for initialization
    }

    @FXML
    private void underConstructionPageSecretary() {
        PageNavigationController.getInstance().navigateToUnderConstructionPage();
    }

    @FXML
    private void verbalizeExam() {
        PageNavigationController.getInstance().navigateTo("add_voucher");
    }
}
