package gui.utility;

import java.net.URL;

/**
 *
 */
public abstract class FXMLSceneController {

    /**
     *
     */
    private FXMLScene scene;

    /**
     * @return URL Instance of the .FXML file associated with {@code this}
     * Controller.
     */
    protected abstract URL getFXML();

    /**
     * Abstract initialise method, which sets up the scene data that is
     * required to be loaded via the Controller. This data is not limited to
     * one thing but can be anything such as:
     * <ul>
     *     <li>Populating data fields</li>
     *     <li>Storing data, and constructing Objects</li>
     * </ul>
     */
    protected abstract void initialise();

    /**
     * Set {@link FXMLScene} of 'this' Controller to the Provided {@code
     * FXMLScene}.
     *
     * @param sc Scene to save.
     */
    public void setScene(final FXMLScene sc) {
        this.scene = sc;
    }

    /**
     * @return {@link FXMLScene} associated with {@code 'this'} Controller.
     */
    public FXMLScene getScene() {
        return this.scene;
    }
}
