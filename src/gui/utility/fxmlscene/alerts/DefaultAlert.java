package gui.utility.fxmlscene.alerts;

import gui.utility.fxmlscene.FXMLSceneController;
import gui.utility.fxmlscene.alerts.textalert.TextAlertScene;

/**
 *
 */
public enum DefaultAlert {
    TEXT_ALERT(new TextAlertScene());

    /**
     *
     */
    private final FXMLSceneController controller;

    /**
     *
     */
    DefaultAlert(final FXMLSceneController c) {
        this.controller = c;
    }

    /**
     *
     */
    public FXMLSceneController getController() {
        return this.controller;
    }
}
