package gui.utility.fxmlscene.alerts.textalert;

import gui.utility.fxmlscene.FXMLSceneController;
import gui.utility.fxmlscene.alerts.AlertBase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.net.URL;

/**
 * Text Alert which is meant to display some "Header" and "Text" for the user
 * to read, Typically used for logging large amounts of text or just to
 * inform of a state change.
 * <p>
 * Construct this through {@link gui.utility.fxmlscene.alerts.FXMLAlert} and
 * use the interface methods provided from the alert object.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class TextAlertScene extends FXMLSceneController implements AlertBase {

    /**
     * FXML File for this scene.
     */
    private static final String FXML_FILE = "TEXT_ALERT.fxml";

    /**
     * Root container Node.
     */
    @FXML
    private BorderPane borderPane;

    /**
     * Header label which contains the header String for this alert.
     */
    @FXML
    private Label headerLabel;

    /**
     * TextArea which will display description String data.
     */
    @FXML
    private TextArea textArea;

    /**
     * @return URL Instance of the .FXML file associated with {@code this}
     * Controller.
     */
    @Override
    protected URL getFXML() {
        return getClass().getResource(FXML_FILE);
    }

    /**
     * Abstract initialise method, which sets up the scene data that is
     * required to be loaded via the Controller. This data is not limited to
     * one thing but can be anything such as:
     * <ul>
     *     <li>Populating data fields</li>
     *     <li>Storing data, and constructing Objects</li>
     * </ul>
     */
    @Override
    protected void initialise() {
        // Unused
    }

    /**
     * Set's the 'Header' text of this to the provided String.
     *
     * @param s String to display as the header.
     */
    @Override
    public void setHeader(final String s) {
        this.headerLabel.setText(s);
    }

    /**
     * @return Current header text.
     */
    @Override
    public String getHeader() {
        return this.headerLabel.getText();
    }

    /**
     * Set's the 'Description' text of this to the provided String.
     *
     * @param s String to display as the Description.
     */
    @Override
    public void setDescription(final String s) {
        this.textArea.setText(s);
    }

    /**
     * @return Current Description text.
     */
    @Override
    public String getDescription() {
        return this.textArea.getText();
    }

    /**
     * Set the Style Sheet of the Alert to the provided Style Sheet.
     *
     * @param cssPath Style sheet to load.
     */
    @Override
    public void setStyleSheet(final String cssPath) {
        this.getScene().getScene().getStylesheets().add(cssPath);
    }

    /**
     *
     */
    public void closeStage() {
        this.getScene().closeStage();
    }
}
