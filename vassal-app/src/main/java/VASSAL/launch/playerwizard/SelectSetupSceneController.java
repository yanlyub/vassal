package VASSAL.launch.playerwizard;

import VASSAL.build.module.PredefinedSetup;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

public class SelectSetupSceneController {

  @FXML
  private ComboBox<PredefinedSetup> setups;

  private WizardModel wizardModel;

  public SelectSetupSceneController() {}

  @FXML
  public void initialize() {
    setups.setButtonCell(new PredefinedSetupListCell());
    setups.setCellFactory(predefinedSetupListView -> new PredefinedSetupListCell());
  }

  public void setWizardModel(WizardModel wizardModel) {
    this.wizardModel = wizardModel;
  }

  public void setValues() {
    setups.getItems().addAll(wizardModel.getPredefinedSetups());
  }

  private static class PredefinedSetupListCell extends ListCell<PredefinedSetup> {
    @Override
    protected void updateItem(PredefinedSetup predefinedSetup, boolean empty) {
      super.updateItem(predefinedSetup, empty);
      final String itemText = (!empty && predefinedSetup != null)
        ? predefinedSetup.getConfigureName()
        : null;
      setText(itemText);
    }
  }
}
