package gui.utility;

import gui.utility.input.TextInputDialog;

import java.util.Optional;

/**
 *
 */
public final class SceneUtils {

    /**
     * Hide constructor.
     */
    private SceneUtils() {
    }

    /**
     *
     */
    public static String promptUserGetString(final String displayText) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setResizable(true);
        textInputDialog.setHeaderText(displayText);

        Optional<String> result = textInputDialog.showAndWait();

        return result.orElse("%s");
    }
}
