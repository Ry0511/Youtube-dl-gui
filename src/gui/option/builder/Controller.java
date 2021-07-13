package gui.option.builder;

import gui.main.Main;
import gui.option.builder.fileloader.OptionFileLoader;
import gui.utility.SceneUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import youtube.dl.wrapper.command.builder.YoutubeCommandBuilder;
import youtube.dl.wrapper.command.builder.YoutubeHelp;
import youtube.dl.wrapper.command.builder.YoutubeOption;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static gui.utility.SceneUtils.marshallObject;
import static gui.utility.SceneUtils.unmarshallXml;
import static gui.utility.SceneUtils.unnamedFile;

/**
 * Controller Class for the Option Builder Scene. Which allows the user to
 * build a {@link youtube.dl.wrapper.command.builder.YoutubeCommandBuilder}
 * directly from the GUI.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class Controller {

    /**
     * ListView containing all possible options to build into our command
     * builder.
     */
    @FXML
    private ListView<YoutubeOption> optionView;

    /**
     * All the items which have been selected and are going to be parsed into
     * our {@link youtube.dl.wrapper.command.builder.YoutubeCommandBuilder}.
     */
    @FXML
    private ListView<YoutubeOption> selectedView;

    /**
     * TextArea which displays information about the currently selected
     * Option from either the {@link #optionView} or {@link #selectedView}.
     */
    @FXML
    private TextArea textArea;

    /**
     * File reference to the save location for the Option Builder.
     */
    public static final File SAVES_DIR = new File(Main.ABSOLUTE_PATH
            + "/option/saved/");

    /**
     * Reference to the FXML File containing the Scene Object data.
     */
    private static final String FXML_FILE = "Option_Picker.fxml";

    /**
     * Populates the {@link #optionView} with all the items inside of
     * {@link YoutubeOption}.
     */
    public void initialise() {

        // Load data into Option view
        if (optionView.getItems().size() == 0) {
            Arrays.stream(YoutubeOption.values())
                    .forEachOrdered(e -> optionView.getItems()
                            .add(e));
        }

        // Setup output paths
        if (!SAVES_DIR.getParentFile().exists()) {
            if (SAVES_DIR.getParentFile().mkdir()) {
                if (SAVES_DIR.mkdir()) {
                    System.out.println("Saves directory created...");
                }
            }
        }
    }

    /**
     * @return FXML File for this Controller class.
     */
    public static URL getFXML() {
        return Controller.class.getResource(FXML_FILE);
    }

    /**
     * @param btn   Button type to check for double click.
     * @param event Event which contains the button clicked and how many
     *              clicks occurred.
     * @return {@code true} if the provided Mouse Event was a double click,
     * on the provided button type.
     */
    private static boolean isDoubleClick(final MouseEvent event,
                                         final MouseButton btn) {
        if (event.getButton() == btn) {
            return event.getClickCount() == 2;
        }
        return false;
    }

    /**
     * Moves the selected Option to the {@link #selectedView}.
     *
     * @param event Mouse Event attached to the Mouse button which triggered
     *              this method.
     */
    public void addOption(final MouseEvent event) throws IOException {
        YoutubeOption option =
                optionView.getSelectionModel().getSelectedItem();
        if (option != null) {
            selectionChanged(option);
            if (isDoubleClick(event, MouseButton.PRIMARY)) {
                addSelectedOption();
            }
        }
    }

    /**
     * @param e YT option which contains the content display message.
     * @return String User input from a dialog window.
     */
    private String getUserInput(final YoutubeOption e) throws IOException {
        final String header = String.format("%s [%s]", e.name(), e.getCommand());

        return SceneUtils.promptUserGetString(header, e.getHelp(YoutubeHelp.EN));
    }

    /**
     * Updates the {@link #textArea} text to display the currently selected
     * {@link YoutubeOption} and it's related information.
     *
     * @param e The Option to display information about.
     */
    private void selectionChanged(final YoutubeOption e) throws IOException {
        String s = String.format("%s%n%n%s%n%n%s",
                e.name(),
                e.getCommand(),
                e.getHelp(YoutubeHelp.EN));
        textArea.setText(s);
    }

    /**
     * Reloads the main Scene using {@link Main#restoreMainScene()}.
     */
    @FXML
    private void showMainScene() {

    }

    /**
     * Removes the selected option from the {@link #selectedView}.
     *
     * @param event Mouse Event which triggerred the event.
     */
    @FXML
    private void removeOption(final MouseEvent event) throws IOException {
        // Could get away with only verifying the selected item
        final Object selectedItem =
                selectedView.getSelectionModel().getSelectedItem();
        if (selectedView.getItems().size() > 0 && selectedItem != null) {
            selectionChanged(this.selectedView
                    .getSelectionModel().getSelectedItem());
            if (isDoubleClick(event, MouseButton.PRIMARY)) {
                removeSelectedOption();
            }
        }
    }

    /**
     * Upon a new item being selected via key-press {@link KeyCode#UP} or
     * {@link KeyCode#DOWN} the currently displayed help text at
     * {@link #textArea} will be updated to whatever item is currently
     * selected. (Based on which target option triggered the event).
     *
     * @param event Key event which triggered the event.
     */
    @FXML
    private void updateDisplayOption(final KeyEvent event)
            throws IOException {
        boolean isUpArrow =
                event.getCode().getName().equals(KeyCode.UP.getName());
        boolean isDownArrow =
                event.getCode().getName().equals(KeyCode.DOWN.getName());

        // Setup target information
        ListView<YoutubeOption> listView = this.optionView;
        if (event.getTarget().equals(selectedView)) {
            listView = selectedView;
        }
        ObservableList<YoutubeOption> list = listView.getItems();


        // Only update target on Arrow up/down
        if (list.size() > 1) {
            if (isUpArrow || isDownArrow) {

                // Option display checking
                YoutubeOption option =
                        listView.getSelectionModel().getSelectedItem();

                // index checking
                int curIndex = listView.getSelectionModel().getSelectedIndex();
                int index = indexFind(curIndex, list, isUpArrow);

                // Updating
                selectionChanged(list.get(index));
            }
        }
    }

    /**
     * Increments or Decrements an item based on its index inside of the
     * List, note it only does so if it is inbounds and will not cause a
     * {@link IndexOutOfBoundsException}.
     *
     * @param curIndex  Current index to increment.
     * @param isUpArrow If true index is decremented, else it is incremented.
     * @param items     List of items containing size scope (0 to length -1)
     * @return Incremented index or decremented index if those indexes are in
     * bounds. But if they're not inbounds then it returns the index
     * unmodified.
     */
    private static int indexFind(final int curIndex,
                                 final ObservableList<YoutubeOption> items,
                                 final boolean isUpArrow) {
        // Shift index by +1 or -1 depending on arrow key pressed
        int index = isUpArrow ? -1 : 1;
        index = curIndex + index;

        // Keep values in bounds
        if (index < items.size() && index >= 0) {
            return index;

        } else {
            return curIndex;
        }
    }

    /**
     * Parses the {@link #selectedView} and all the {@link YoutubeOption}
     * contained into a Single {@link YoutubeCommandBuilder} Object.
     *
     * @return All Selected 'Options' inside of {@link #selectedView} parsed
     * into a {@link YoutubeCommandBuilder}. (Can return an empty builder).
     */
    private YoutubeCommandBuilder parseSelected() {
        ObservableList<YoutubeOption> optionList = selectedView.getItems();
        YoutubeCommandBuilder builder = new YoutubeCommandBuilder();
        for (YoutubeOption option : optionList) {
            builder.addOption(option);
        }
        return builder;
    }

    /**
     *
     */
    public void addSelectedOption() throws IOException {
        YoutubeOption option = optionView.getSelectionModel().getSelectedItem();
        if (option != null) {
            if (option.requiresInput()) {
                option.setUserInput(getUserInput(option));
            }
            selectedView.getItems().add(option);
        }
    }

    /**
     *
     */
    public void removeSelectedOption() {
        YoutubeOption e = selectedView.getSelectionModel()
                .getSelectedItem();
        selectedView.getItems().remove(e);
    }

    /**
     *
     */
    public void loadOption() throws IOException, JAXBException {
        final File f = OptionFileLoader.getSelection(
                OptionFileLoader.DEFAULT_TITLE,
                OptionFileLoader.DEFAULT_WIDTH,
                OptionFileLoader.DEFAULT_HEIGHT,
                SAVES_DIR
        );

        if (f != null && f.isFile()) {
            YoutubeCommandBuilder ytb = (YoutubeCommandBuilder) unmarshallXml(f,
                    YoutubeCommandBuilder.class);
            selectedView.getItems().clear();
            selectedView.getItems().addAll(ytb.getOptions());
        }
    }

    /**
     *
     */
    public void saveOption() throws JAXBException, IOException {
        if (selectedView.getItems().size() > 0) {
            String xmlData = marshallObject(parseSelected(),
                    YoutubeCommandBuilder.class);

            String fileName = getFileName();

            // Write to File
            File output = new File(SAVES_DIR + "/" + fileName);
            if (!output.exists()) {
                if (output.createNewFile()) {
                    FileUtils.write(output, xmlData, StandardCharsets.UTF_8);
                    Alert e = new Alert(Alert.AlertType.INFORMATION,
                            "File: " + output + " has been created.");
                    e.showAndWait();
                }
            }
        } else {
            final String message = "Invalid options provided.";
            Alert e = new Alert(Alert.AlertType.INFORMATION, message);
            e.showAndWait();
        }
    }

    /**
     * @return Asks the 'User' for a String and if the string they provide is
     * valid then it's returned but if it's not then a prefix is used which
     * is "UnnamedFile_x" where 'x' is just an Integer value.
     */
    private String getFileName() throws IOException {
        final String header = "Enter a filename.";
        final String promptText = "What should this 'Option' be "
                + "called/referenced as?";
        String fileName = SceneUtils.promptUserGetString(header, promptText);

        return ensureFileName(fileName);
    }

    /**
     * Ensures that the provided Filename is a valid one, by applying some
     * verification checks and replacing all invalid characters with '_'.
     * However if the provided filename is an Empty String then the returned
     * String is prefixed to: "UnnamedFile_x" where 'x' is the largest found
     * instance of the files matching "UnnamedFile_x".
     * <p>
     * @param fileName Filename to verify.
     * @return Filename which is a valid Path and that does not contain
     * illegal characters. Default exit value is: "UnnamedFile_x" where 'x' is
     * the largest number of that prefix.
     */
    private String ensureFileName(final String fileName) {
        final String invalidChars = "[^a-zA-Z0-9.\\-]";
        String name = fileName.replaceAll(invalidChars, "_");
        final String extension = ".xml";

        // If no name is typed give it one
        if (name.length() == 0) {
            // If multiple un named files
            name = unnamedFile(SAVES_DIR, "UnnamedFile_");
        }

        return name + extension;
    }
}
