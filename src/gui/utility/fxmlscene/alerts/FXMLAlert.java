package gui.utility.fxmlscene.alerts;

import gui.utility.fxmlscene.FXMLScene;
import gui.utility.fxmlscene.FXMLSceneController;
import javafx.stage.Stage;


/**
 * FXML Alert to display specialised Windows from {@code .FXML}.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class FXMLAlert implements AlertBase {

    /**
     * FXMLScene constructed upon, Object instantiation.
     */
    private final FXMLScene fxmlScene;

    /**
     * Construct an Alert from a {@link DefaultAlert} ordinal.
     *
     * @param defaultAlert Alert to construct.
     */
    public FXMLAlert(final DefaultAlert defaultAlert) {
        fxmlScene = new FXMLScene(defaultAlert.getController());
    }

    /**
     * @return {@link FXMLScene} for this Alert.
     */
    public FXMLScene getFxmlScene() {
        return this.fxmlScene;
    }

    /**
     * @return {@link FXMLSceneController} for the current Alert scene.
     */
    private FXMLSceneController getFxmlController() {
        return (FXMLSceneController) this.fxmlScene.getController();
    }

    /**
     * @return {@link AlertBase} from the FXML Controller. All instances
     * should allow this operation.
     */
    private AlertBase getAlertBase() {
        return (AlertBase) this.getFxmlController();
    }

    /**
     * Create stage, set scene, and wait until the alert is closed.
     */
    public void showAndWait() {
        final Stage s = new Stage();
        s.setScene(this.fxmlScene.getScene());
        s.showAndWait();
    }

    /**
     * Set's the 'Header' text of this to the provided String.
     *
     * @param s String to display as the header.
     */
    @Override
    public void setHeader(final String s) {
        this.getAlertBase().setHeader(s);
    }

    /**
     * @return Current header text.
     */
    @Override
    public String getHeader() {
        return this.getAlertBase().getHeader();
    }

    /**
     * Set's the 'Description' text of this to the provided String.
     *
     * @param s String to display as the Description.
     */
    @Override
    public void setDescription(final String s) {
        this.getAlertBase().setDescription(s);
    }

    /**
     * @return Current Description text.
     */
    @Override
    public String getDescription() {
        return this.getAlertBase().getDescription();
    }

    /**
     * Set the Style Sheet of the Alert to the provided Style Sheet.
     *
     * @param cssPath Style sheet to load.
     */
    @Override
    public void setStyleSheet(final String cssPath) {
        this.getAlertBase().setStyleSheet(cssPath);
    }
}
