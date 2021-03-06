package gui.utility.fxmldialog.dialogs;

import gui.utility.fxmldialog.dialogs.booleandialog.BooleanDialog;
import gui.utility.fxmldialog.dialogs.fileinput.FileInputController;
import gui.utility.fxmldialog.dialogs.stringinput.TextInputController;

import java.net.URL;

/**
 * Enum ordinals referencing a bunch of Dialog Scenes for quick access and
 * loading.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public enum FXMLScenes {
    // Could be improved, but works fine for now.
    //todo improve
    STRING_INPUT(TextInputController.class.getResource("Text_Input.fxml")),
    FILE_INPUT(FileInputController.class.getResource("File_Input.fxml")),
    BOOLEAN_INPUT(BooleanDialog.class.getResource("Boolean_Dialog.fxml"));

    /**
     * Referenced FXML file as a URL ready to be loaded as a Scene.
     */
    private final URL fxml;

    /**
     * Initialises the enum and assigns the URL of the FXML file to the
     */
    FXMLScenes(final URL url) {
        this.fxml = url;
    }

    /**
     * @return FXML URL of this enum ordinal.
     */
    public URL getFxml() {
        return this.fxml;
    }
}
