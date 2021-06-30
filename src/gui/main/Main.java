package gui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 */
public class Main extends Application {

    private static final String MAIN_FXML = "Main.fxml";
    private static final String MAIN_TITLE = "yt-dl";
    private static Scene mainScene;
    private static Stage primaryStage;
    private static Controller mainController;

    /**
     *
     */
    @Override
    public void start(final Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_FXML));
        Parent root = loader.load();

        // Name and show stage
        stage.setTitle(MAIN_TITLE);
        Scene scene = new Scene(root, 1280, 768);
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
     * {@link Application#launch(String...)}
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
     *
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
