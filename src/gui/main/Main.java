package gui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * Scene GUI Launcher.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class Main extends Application {

    /**
     * Static name of the FXML file for the Main scene.
     */
    private static final String MAIN_FXML = "Main.fxml";

    /**
     * Static window title name.
     */
    private static final String MAIN_TITLE = "yt-dl";

    /**
     * Locally store the main scene for reference.
     */
    private static Scene mainScene;

    /**
     * Locally store the primary stage for reference.
     */
    private static Stage primaryStage;

    /**
     * Locally store Main Controller for reference.
     */
    private static Controller mainController;

    /**
     *
     */
    public static final String ABSOLUTE_PATH = new File("").getAbsolutePath();

    /**
     *
     */
    @Override
    public void start(final Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_FXML));
        Parent root = loader.load();

        final int windowWidth = 1280;
        final int windowHeight = 768;

        // Name and show stage
        stage.setTitle(MAIN_TITLE);
        Scene scene = new Scene(root, windowWidth, windowHeight);
        stage.setScene(scene);
        stage.show();

        // Save states
        mainScene = scene;
        primaryStage = stage;

        // Initialise Required
        Controller e = loader.getController();
        mainController = e;
        e.initialise();
    }

    /**
     * Default JavaFX Launcher.
     *
     * @param args {@link #start(Stage)} and
     *             {@link Application#launch(String...)}
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     */
    public static void showNewStage(final Scene scene, final String title) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Restores the Main scene to the {@code Main Scene}, also recalls
     * {@link Controller#initialise()} to re-initialise the Scene.
     * <p>
     * Note: This method will only show if {@link Stage#getScene} is not
     * {@link #mainScene}. If it is the main scene then this method does
     * nothing.
     */
    public static void restoreMainScene() {
        if (primaryStage.getScene() != mainScene) {
            primaryStage.setTitle(MAIN_TITLE);
            primaryStage.setScene(mainScene);
            primaryStage.show();
            mainController.initialise();
        }
    }
}
