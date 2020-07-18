package VASSAL.launch.playerwizard;

import java.util.List;

import VASSAL.build.module.PredefinedSetup;

public class WizardModel {
  private List<PredefinedSetup> predefinedSetups;

  public void setPredefinedSetups(List<PredefinedSetup> predefinedSetups) {
    this.predefinedSetups = predefinedSetups;
  }

  public List<PredefinedSetup> getPredefinedSetups() {
    return predefinedSetups;
  }
}
