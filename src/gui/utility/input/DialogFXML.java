package gui.utility.input;

import gui.utility.input.fileinput.FileInputController;
import gui.utility.input.stringinput.TextInputController;

import java.net.URL;

/**
 * Enum ordinals referencing a bunch of Dialog Scenes for quick access and
 * loading.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public enum DialogFXML {
    STRING_INPUT(TextInputController.class.getResource("Text_Input.fxml")),
    FILE_INPUT(FileInputController.class.getResource("File_Input.fxml"));

    /**
     * Referenced FXML file as a URL ready to be loaded as a Scene.
     */
    private final URL fxml;

    /**
     * Initialises the enum and assigns the URL of the FXML file to the
     */
    DialogFXML(final URL url) {
        this.fxml = url;
    }

    /**
     * @return FXML URL of this enum ordinal.
     */
    public URL getFxml() {
        return this.fxml;
    }
}
