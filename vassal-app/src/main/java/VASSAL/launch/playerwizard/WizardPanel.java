package VASSAL.launch.playerwizard;

import java.io.IOException;
import java.net.URL;

import VASSAL.launch.PlayerWindow;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class WizardPanel extends JFXPanel {

  private final PlayerWindow playerWindow;
  private WizardMainController controller;

  public WizardPanel(PlayerWindow playerWindow) {
    this.playerWindow = playerWindow;
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = getClass().getResource("mainScene.fxml");
    loader.setLocation(xmlUrl);
    final Parent root;
    try {
      root = loader.load();
      controller = loader.getController();
      controller.setWizardPanel(this);
      Platform.runLater(() -> setScene(root));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setScene(Parent root) {
    Scene primaryScene = new Scene(root);
    setScene(primaryScene);
  }

  public void closeWizard() {
    playerWindow.closeWizard();
  }

  public void setWizardModel(WizardModel wizardModel) {
    controller.setWizardModel(wizardModel);
  }

}
