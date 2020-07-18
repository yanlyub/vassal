package VASSAL.launch.playerwizard;

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class WizardMainController {

  @FXML
  private Button newGameButton;

  private WizardPanel wizardPanel;
  private WizardModel wizardModel;

//  @FXML
//  private Label mainTitle;
//
//  @FXML
//  private Label subTitle;

  public void newGameButtonClicked(ActionEvent actionEvent) {
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = getClass().getResource("selectSetupScene.fxml");
    loader.setLocation(xmlUrl);
    final Parent root;
    try {
      root = loader.load();
      SelectSetupSceneController selectSetupSceneController = loader.getController();
      selectSetupSceneController.setWizardModel(wizardModel);
      selectSetupSceneController.setValues();
      Platform.runLater(() -> wizardPanel.setScene(root));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setWizardPanel(WizardPanel wizardPanel) {
    this.wizardPanel = wizardPanel;
  }

  public void setWizardModel(WizardModel wizardModel) {
    this.wizardModel = wizardModel;
  }
}
