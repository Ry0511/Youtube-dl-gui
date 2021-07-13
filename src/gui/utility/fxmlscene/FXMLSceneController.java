package gui.utility.fxmlscene;

import java.net.URL;

/**
 * FXML Scene Controller which is to be used in conglomeration with
 * {@link FXMLScene}, this template Class defines the needed methods to allow
 * the ease of work, with the FXML Scene and linking of their respective
 * methods, and instances.
 * <p>
 * This class allows the FXML File type to be loaded and controlled simply
 * with a few method calls. For complete usage see {@link FXMLScene}.
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public abstract class FXMLSceneController {

    /**
     * {@link FXMLScene} which initialised this Controller.
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
