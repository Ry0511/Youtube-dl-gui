package gui.utility.input;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Dialog Input Box for getting user Input through prompt. Might be removed
 * for my own implementation, though this works well as is.
 * <p>
 * Source:
 * https://stackoverflow.com/questions/53490172/javafx-creating-a-text-input-box
 *
 * @author Zephyr (Original)
 * @version 0.1
 * Copyright: N/A
 */
public class TextInputDialog extends Dialog<String> {

    /**
     *
     */
    private final GridPane grid;

    /**
     *
     */
    private final TextArea textArea;

    /**
     *
     */
    private final String defaultValue;

    /**
     * Creates a new TextInputDialog without a default value entered into the
     * dialog {@link TextField}.
     */
    public TextInputDialog() {
        this("");
    }

    /**
     *
     */
    public TextInputDialog(final String defaultValue) {
        final DialogPane dialogPane = getDialogPane();

        // TextArea Style
        this.textArea = new TextArea(defaultValue);
        this.textArea.setMaxSize(Region.USE_COMPUTED_SIZE,
                Region.USE_COMPUTED_SIZE);
        this.textArea.setWrapText(true);
        this.textArea.setPrefSize(Region.USE_COMPUTED_SIZE,
                Region.USE_COMPUTED_SIZE);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setFillWidth(textArea, true);

        this.defaultValue = defaultValue;

        // Grid Style
        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxSize(Region.USE_COMPUTED_SIZE,
                Region.USE_COMPUTED_SIZE);
        this.grid.setAlignment(Pos.CENTER_LEFT);


        dialogPane.setMaxSize(600, Region.USE_COMPUTED_SIZE);
        dialogPane.contentTextProperty().addListener(o -> update());
        dialogPane.setPadding(new Insets(6));
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        update();

        setResultConverter(this::setResult);
    }

    /**
     * Returns the {@link TextField} used within this dialog.
     */
    public final TextArea getEditor() {
        return textArea;
    }

    /**
     * Returns the default value that was specified in the constructor.
     */
    public final String getDefaultValue() {
        return defaultValue;
    }

    /**
     *
     */
    private String setResult(final ButtonType dialogButton) {
        ButtonBar.ButtonData data;
        if (dialogButton == null) {
            data = null;
        } else {
            data = dialogButton.getButtonData();
        }
        return data == ButtonBar.ButtonData.OK_DONE ? textArea.getText() : null;
    }

    /**
     *
     */
    private void update() {
        grid.getChildren().clear();

        grid.add(textArea, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(textArea::requestFocus);
    }
}
