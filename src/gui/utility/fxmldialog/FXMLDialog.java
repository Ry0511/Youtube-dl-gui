package gui.utility.fxmldialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.net.URL;

/**
 * FXML Dialog window, which allows the implementation of {@code .fxml} files
 * with the {@link Dialog} without having to setup a lot of the backend
 * yourself. By just passing the URL to the FXML file to load.
 *
 * @param <T> Return type of this Dialog. i.e., {@code File, String, Integer}
 *            and so on.
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class FXMLDialog<T> extends Dialog<T> {

    /**
     * Reference to the FXML File.
     */
    private final URL fxmlFile;

    /**
     * Reference to the Controller Class.
     */
    private final FXMLDialogController<T> controller;

    /**
     * Root of the FXML content once loaded.
     */
    private final Parent root;

    /**
     * Constructs an FXMLDialog and initialises all required data.
     *
     * @param fxml Link to {@code .FXML} containing a Scene to show.
     * @throws MalformedParametersException if the FXML file provided is
     *                                      {@code null}.
     * @throws IOException                  if the initialisation of the
     *                                      FXMLoader causes any exceptions.
     *                                      (This exception is thrown/passed
     *                                      up).
     */
    public FXMLDialog(final URL fxml) throws IOException {
        if (fxml != null) {

            // Load scene
            this.fxmlFile = fxml;
            FXMLLoader loader = new FXMLLoader(fxml);
            this.root = loader.load();
            this.controller = loader.getController();

            // Setting scene/stage
            DialogPane dp = new DialogPane();
            dp.setContent(root);
            this.setDialogPane(dp);
            this.controller.setDialog(this);
        } else {
            throw new MalformedParametersException("'Null' .FXML file...");
        }
    }

    /**
     * @return Controller Instance associated with {@code 'this'} Dialog.
     */
    public FXMLDialogController<T> getController() {
        return this.controller;
    }

    /**
     * @return FXML File associated with this Dialog.
     */
    public URL getFxmlFile() {
        return this.fxmlFile;
    }

    /**
     * @return Parent root content loaded from FXML file.
     */
    public Parent getRoot() {
        return this.root;
    }
}
