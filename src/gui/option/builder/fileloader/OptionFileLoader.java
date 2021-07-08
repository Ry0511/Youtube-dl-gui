package gui.option.builder.fileloader;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Option File Loader Windows to allow a User to see, delete, and select a
 * file to use.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class OptionFileLoader {

    /**
     * Static reference to the FXML file for the file loader.
     */
    private static final String FXML_FILE = "Option_Fileloader.fxml";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "File Loader";

    /**
     *
     */
    public static final int DEFAULT_WIDTH = 500;

    /**
     *
     */
    public static final int DEFAULT_HEIGHT = 550;

    /**
     * File view containing files to view, delete, and select.
     */
    @FXML
    private ListView<File> fileView;

    /**
     * The chosen element for this File Loader to load.
     */
    private File selection;

    /**
     * Current stage in which the {@link OptionFileLoader} is currently being
     * shown on.
     */
    private Stage curStage;

    /**
     * Allows the user to see a list of files in which they can, delete, or
     * select a file from the list.
     *
     * @return A selected file from the user, if the user selects a file. Or
     * if the user deletes all files and there are not files, or if the
     * window is closed using the 'X' then 'null' is returned.
     */
    public static File getSelection(final String title,
                                    final int width,
                                    final int height,
                                    final File dir) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(OptionFileLoader.class.getResource(FXML_FILE));
        Parent root = loader.load();

        // Name and show stage
        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);

        // Setup
        OptionFileLoader e = loader.getController();
        e.initialise(dir, stage);
        stage.showAndWait();

        return e.getSelection();
    }


    /**
     * Initialises the File View/File loader and its sets its current stage.
     * However if there are no files to view then the stage is terminated
     * immediately.
     *
     * @param s Current stage which the {@link OptionFileLoader} will be
     *          shown on.
     * @param file File or Directory in which to populate the
     * {@link #fileView} with.
     */
    private void initialise(final File file, final Stage s) {
        ObservableList<File> list = fileView.getItems();
        list.clear();

        // Directory Case
        if (file.isDirectory()) {
            File[] items = file.listFiles();
            if (items != null) {
                list.addAll(Arrays.asList(items));
            }

            // File case
        } else if (file.isFile()) {
            list.add(file);
        }

        this.curStage = s;
    }

    /**
     * Deletes the currently selected file in {@link #fileView} and also
     * removes it from the file view.
     *
     * Note: If upon deletion there are no files to load/show in the File
     * view then the stage will be closed.
     */
    @FXML
    public void deleteFile() {
        if (fileView.getItems().size() > 0) {
            File f = fileView.getSelectionModel().getSelectedItem();
            if (f.delete()) {
                fileView.getItems().remove(f);
                System.out.println("Deleted File: " + f);
            }
        }

        if (fileView.getItems().size() == 0) {
            curStage.close();
        }
    }

    /**
     * Sets {@link #selection} to the Currently selected item in the
     * {@link #fileView} and then closes the {@link #curStage}.
     */
    @FXML
    public void selectFile() {
        if (fileView.getItems().size() > 0) {
            this.selection = fileView.getSelectionModel().getSelectedItem();
            this.curStage.close();
        }
    }

    /**
     * @return The currently selected file determined by
     * {@link #selectFile()}. If this returns {@code null} then it means that
     * no file has been selected.
     */
    public File getSelection() {
        return this.selection;
    }
}
