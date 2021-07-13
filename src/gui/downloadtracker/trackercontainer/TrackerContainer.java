package gui.downloadtracker.trackercontainer;

import gui.downloadtracker.TrackerWindow;
import gui.utility.fxmlscene.FXMLScene;
import gui.utility.fxmlscene.FXMLSceneController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;

/**
 *
 */
public class TrackerContainer extends FXMLSceneController {

    /**
     *
     */
    private static final String FXML_FILE = "Tracker_Container.fxml";
    public ScrollPane scrollPane;
    public VBox itemVbox;


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

        for (int i = 0; i < 9; i++) {
            FXMLScene scene = new FXMLScene(new TrackerWindow());
            TrackerWindow tc = (TrackerWindow) scene.getController();

            tc.setParentContainer(this);
            itemVbox.getChildren().add(tc.sceneToRoot(scene.getScene()));
        }
    }

    public void removeWindow(final TrackerWindow window) {
        if (window != null) {
            itemVbox.getChildren().remove(window.getBorderPane());
        }
    }
}
