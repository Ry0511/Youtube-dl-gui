package gui.downloadtracker;

import gui.downloadtracker.trackercontainer.TrackerContainer;
import gui.utility.fxmlscene.FXMLSceneController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.lang.reflect.MalformedParametersException;
import java.net.URL;
import java.util.Random;

/**
 *
 */
public class TrackerWindow extends FXMLSceneController {

    /**
     * Reference to the {@code FXML} file associated with this Scene.
     */
    private static final String FXML_FILE = "Tracker_Window.fxml";

    /**
     *
     */
    private TrackerContainer parentContainer;

    /**
     * Root container in which all Objects for this scene reside on.
     */
    @FXML
    private BorderPane borderPane;

    /**
     * TextArea dedicated for console messages.
     */
    @FXML
    private TextArea consoleTextArea;

    /**
     * Progress bar for tracking current progress of the task being executed.
     */
    @FXML
    private ProgressBar progressBar;

    /**
     * Label to be updated to display the current download speed.
     */
    @FXML
    private Label speedLabel;

    /**
     * Label to be updated to display the current expected time for the
     * download.
     */
    @FXML
    private Label etaLabel;

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
    public void initialise() {
        System.out.println("Initialised!");
    }

    /**
     *
     */
    public void setParentContainer(TrackerContainer tc) {
        this.parentContainer = tc;
    }

    /**
     *
     */
    private final StringBuilder consoleLog = new StringBuilder();

    /**
     * @param o Object which should be a {@link javafx.scene.Scene}.
     * @return {@link #borderPane} if 'o' is the Scene.
     * @throws MalformedParametersException If 'o' is not the Scene for this
     *                                      Object.
     */
    public Node sceneToRoot(final Object o) {
        if (o.equals(this.getScene().getScene())) {
            return this.borderPane;
        } else {
            throw new MalformedParametersException("How did this happen...");
        }
    }

    /**
     *
     */
    public BorderPane getBorderPane() {
        return this.borderPane;
    }

    /**
     * Set the Speed label to the provided String; Note this is 'Speed: ' +
     * string and so only the number is required.
     *
     * @param s Speed to display.
     */
    public void updateSpeedLabel(final String s) {
        this.speedLabel.setText("Speed: " + s);
    }

    /**
     * Set the ETA label to the provided String; Note that this is 'ETA: ' +
     * etaString.
     *
     * @param s ETA String to use.
     */
    public void updateEtaLabel(final String s) {
        this.etaLabel.setText("ETA: " + s);
    }

    /**
     * Set the progress to the provided number. This value should be 0 -> 1
     * and as such this isn't incremental either.
     *
     * @param value Percentage progress to use. [0.0 to 1.0]
     */
    public void updateProgress(final double value) {
        this.progressBar.setProgress(value);
    }

    /**
     * Logs the console message and then displays the message.
     *
     * @param s Message to display.
     */
    public void setConsoleMessage(final String s) {
        this.consoleLog.append(s).append("\n");
        this.consoleTextArea.setText(s);
    }

    /**
     * @return All messages that were displayed to the
     * {@link #consoleTextArea} this can be no-messages to, dozens of
     * messages, each message separated by a new line.
     */
    public String getConsoleLog() {
        return this.consoleLog.toString();
    }

    /**
     *
     */
    public void doStuff() {
        Random r = new Random();

        this.updateSpeedLabel(String.valueOf(r.nextInt(9999)));
        this.updateEtaLabel(String.valueOf(r.nextInt(9999)));
        this.updateProgress(r.nextDouble());
        this.setConsoleMessage("Some stuff is happening + this: " + r.nextInt(999999));
    }

    /**
     *
     */
    public void getLog() {
        Alert e = new Alert(Alert.AlertType.INFORMATION);
        e.setTitle("Log Information...");
        e.setHeaderText(consoleLog.toString());
        e.showAndWait();
    }

    /**
     *
     */
    public void abortTask() {
        this.parentContainer.removeWindow(this);
    }
}
