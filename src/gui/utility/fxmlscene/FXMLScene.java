package gui.utility.fxmlscene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.net.URL;

/**
 * FXML File Scene loader and Object Data Storage Object; allowing the
 * grabbing of all relevant FXML Object data, and it's Controller class.
 * <p>
 *
 * Usage Extending Controller (Allows access to the instantiated
 * {@link FXMLScene}):<br><br>
 * <code>
 *     // Construct scene from class extending FXMLSceneController<br>
 *     FXMLScene scene = new FXMLScene(new OptionPicker());<br><br>
 *     // Create stage, and set scene<br>
 *     Stage s = new Stage();<br>
 *     s.setScene(scene.getScene());<br><br>
 *     // poof<br>
 *     s.showAndWait();<br><br>
 * </code>
 * <p>
 *
 * <p>
 * Extra Constructor provided to allow the loading from a {@link URL} in
 * which everything is the same as above except providing the URL instead of
 * the Controller.
 *
 * <p>
 * To grab the controller use the methods provided here
 * {@link #getController()}.
 *
 * @author -Ry
 * @version 0.3
 * Copyright: N/A
 */
public class FXMLScene {

    /**
     * URL to the {@code .FXML File}.
     */
    private final URL fxml;

    /**
     * FXMLLoader attached to the {@code .FXML} file provided upon Construction.
     */
    private final FXMLLoader loader;

    /**
     * Root objects loaded from the FXML file provided at Construction;
     * loaded through {@link FXMLLoader#load()}.
     */
    private final Parent root;

    /**
     * FXML Objects loaded from {@link #fxml} into a Scene.
     */
    private final Scene scene;

    /**
     * Initialises and stores all FXML Data locally for ease of access.
     *
     * @param url Link to a {@code .FXML} file which contains Objects to load.
     * @throws MalformedParametersException Upon FXML Loading failing in
     *                                      anyway, typically something
     *                                      would be wrong with the URL.
     */
    public FXMLScene(final URL url) {
        try {
            // Assign object data
            this.fxml = url;
            this.loader = new FXMLLoader(url);
            this.root = this.loader.load();
            scene = new Scene(root);
        } catch (IOException e) {
            // Compile variables, and post detail error message.
            throw new MalformedParametersException(buildErrorLog()
                    + e.getMessage());
        }
    }

    /**
     * Construct FXML Scene from Controller extending
     * {@link FXMLSceneController} note the passed Controller isn't used, you
     * should use {@link #getController()} to get the actual controller.
     * <p>
     * Note: That for the Controller
     * {@link FXMLSceneController#setScene(FXMLScene)} where {@code 'this'}
     * is passed as the FXMLScene and then
     * {@link FXMLSceneController#initialise()} is called on the Controller
     * at the end of Construction to allow initialisation.
     *
     * @param controller Controller extending {@link FXMLSceneController}.
     * @throws MalformedParametersException Same reasons as
     *                                      {@link #FXMLScene(URL)}.
     */
    public FXMLScene(final FXMLSceneController controller) {
        try {
            // Assign object data
            final URL url = controller.getFXML();
            this.fxml = url;
            this.loader = new FXMLLoader(url);
            this.root = this.loader.load();
            scene = new Scene(root);
            FXMLSceneController instance = loader.getController();
            instance.initialise();
            instance.setScene(this);
        } catch (IOException e) {
            // Compile variables, and post detail error message.
            throw new MalformedParametersException(buildErrorLog()
                    + e.getMessage());
        }
    }

    /**
     * Builds relevant fields into data about them in order to help deduce
     * what exactly went wrong when the FXML file was loaded.
     *
     * @return Formatted String of the URL, FXMLLoader, and Root objects.
     * Assuming they're {@code not null}; in which they're formatted to a
     * default String.
     */
    private String buildErrorLog() {
        // Take note of null variables
        final String urlError = fxml == null ? "Null URL" : fxml.toString();
        final String loaderError = loader == null ? "Loader Null" : "Loader"
                + " fine.";
        final String rootError = root == null ? "Parent/Root Null" : "Parent"
                + "/Root fine";

        return String.format("[%s] ~ [%s] ~ [%s]%nIOException Detail:%n",
                urlError,
                loaderError,
                rootError
        );
    }

    /**
     * @return URL provided at Construction; {@link #fxml}.
     */
    public URL getFxml() {
        return this.fxml;
    }

    /**
     * @return Loader attached to URL; {@link #loader}.
     */
    public FXMLLoader getLoader() {
        return this.loader;
    }

    /**
     * @return Parent content loaded from FXML Loader; {@link #root}.
     */
    public Parent getRoot() {
        return this.root;
    }

    /**
     * @return Controller class associated with 'this' instance of the FXML
     * Objects. Note this is bounded generic and should be casted to the
     * proper Controller class.
     */
    public Object getController() {
        return this.loader.getController();
    }

    /**
     * @return Root content loaded onto a Scene; {@link #scene}.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Assuming that by this time the {@link #scene} has been loaded into a
     * Stage. This will load, from the Scene, its stage Via:
     * <ul>
     *     <li>{@code (Stage) scene.getWindow}</li>
     * </ul>
     *
     * @return Stage parsed from Scene; {@link #scene}, {@link #root}.
     */
    public Stage getStage() {
        return (Stage) this.scene.getWindow();
    }

    /**
     * Assumes this Scene has been loaded into a Stage in someway.
     */
    public void closeStage() {
        getStage().close();
    }
}
