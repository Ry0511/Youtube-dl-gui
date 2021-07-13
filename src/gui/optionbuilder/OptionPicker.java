package gui.optionbuilder;

import gui.main.Main;
import gui.utility.fxmlscene.FXMLSceneController;
import gui.utility.SceneUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.commons.io.FileUtils;
import youtube.dl.wrapper.command.builder.YoutubeCommandBuilder;
import youtube.dl.wrapper.command.builder.YoutubeHelp;
import youtube.dl.wrapper.command.builder.YoutubeOption;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 */
public class OptionPicker extends FXMLSceneController {

    /**
     * Reference to the FXML File associated with this Controller.
     */
    private static final String FXML_FILE = "Option_Picker.fxml";

    /**
     * File reference to the save location for the Option Builder.
     */
    public static final File SAVES_DIR = new File(Main.ABSOLUTE_PATH
            + "/option/saved/");

    /**
     * Default Help Language.
     */
    private static final YoutubeHelp DEFAULT_LANGUAGE = YoutubeHelp.EN;

    /**
     * List of all selected options.
     */
    @FXML
    private ListView<YoutubeOption> selectedView;

    /**
     * List of all possible Options to build.
     */
    @FXML
    private ListView<YoutubeOption> optionView;

    /**
     * Text Area which contains current information about the currently
     * selected Option. From either {@link #selectedView} or
     * {@link #optionView} context based selection.
     */
    @FXML
    private TextArea textArea;

    /**
     * @return URL Instance of the .FXML file associated with {@code this}
     * Controller.
     */
    @Override
    protected URL getFXML() {
        return getClass().getResource(FXML_FILE);
    }

    /**
     * Populates Option View with all required Data.
     */
    public void initialise() {
        optionView.getItems().addAll(YoutubeOption.values());
    }

    /**
     * Removes the Currently selected item from the {@link #selectedView} if
     * the item is not {@code null}.
     */
    public void removeSelectedOptionButtonClick() {
        final YoutubeOption ytb =
                selectedView.getSelectionModel().getSelectedItem();
        if (ytb != null) {
            selectedView.getItems().remove(ytb);
        }
    }

    /**
     * Adds the currently selected item from the {@link #optionView} if the
     * item is not {@code null}.
     */
    public void addSelectedOptionButtonClick() throws IOException {
        final YoutubeOption ytb =
                optionView.getSelectionModel().getSelectedItem();
        if (ytb != null) {
            addOption(ytb, selectedView.getItems());
        }
    }

    /**
     * Adds the provided option to the provided list, while also checking for
     * {@code null} and also prompting if the 'Option' requires user args.
     * <p>
     * See: {@link YoutubeOption#requiresInput()}.
     *
     * @param list List to add to.
     * @param opt  Option to add.
     */
    private void addOption(final YoutubeOption opt,
                           final List<YoutubeOption> list)
            throws IOException {
        if (opt != null) {
            if (opt.requiresInput()) {
                String input = SceneUtils.promptUserGetString(opt.name(),
                        opt.getHelp(DEFAULT_LANGUAGE));
                opt.setUserInput(input);
            }
            list.add(opt);
        }
    }

    /**
     * Updates Option based on target; If Selected View changed then, update
     * with Selected View and if Option changed then update with Option view.
     *
     * @param event Contains the Key Pressed, we only care about Arrow Up and
     *              Arrow Down, depending on which was pressed, the currently
     *              selected items index will shift accordingly.
     */
    public void updateDisplayOptionKeyPress(final KeyEvent event)
            throws IOException {
        final ListView<YoutubeOption> view =
                event.getTarget().equals(optionView) ? optionView
                        : selectedView;
        final YoutubeOption item = view.getSelectionModel().getSelectedItem();

        final boolean isUpArrow = event.getCode().getName()
                .equals(KeyCode.UP.getName());
        final boolean isDownArrow = event.getCode().getName()
                .equals(KeyCode.DOWN.getName());

        if (isUpArrow) {
            getAndSetNowOption(view, item, -1);

        } else if (isDownArrow) {
            getAndSetNowOption(view, item, 1);
        }
    }

    /**
     * Finds the item in the provided ListView and then shifts the current
     * display option by the items (index + increment), assuming that the
     * increment is inbounds.
     * <p>
     * Uses {@link #updateTextOption(YoutubeOption)} for updating option,
     * this only determines what item to set, use.
     * <p>
     * Doesn't return anything, though the element set is either 'item' or
     * that items index + increment, assuming that the index is found to be
     * inbounds.
     *
     * @param view      ListView containing all items, of which 'item' should
     *                  be a member of.
     * @param item      Item to search for and get the index of.
     * @param increment Value to shift the 'item' index by.
     */
    private void getAndSetNowOption(final ListView<YoutubeOption> view,
                                    final YoutubeOption item,
                                    final int increment) throws IOException {
        final int doesNotContain = -1;
        if (item != null) {
            int index = view.getItems().indexOf(item);

            if (index != doesNotContain) {
                final int temp = index + increment;
                if (inBounds(view.getItems(), temp)) {
                    updateTextOption(view.getItems().get(temp));
                } else {
                    updateTextOption(item);
                }
            }
        }
    }

    /**
     * @param items List of items to, use and the boundaries for evaluation; 0
     *              to list length.
     * @param value Value to determine if it's in-bounds.
     * @return {@code true} if the provided 'value' lies within 0 to 'items
     * .length' else will return {@code false}.
     */
    private boolean inBounds(final List<?> items, final int value) {
        final int min = 0;
        final int max = items.size();

        return ((value >= min) && (value < max));
    }

    /**
     *
     */
    private void updateTextOption(final YoutubeOption opt) throws IOException {
        final String header = opt.name();
        final String command = String.format(opt.getCommand(), opt.getArgs());
        final String detail = opt.getHelp(DEFAULT_LANGUAGE);

        textArea.setText(String.format("%s%n%n%s%n%n%s%n",
                header, command, detail)
        );
    }

    /**
     * Method name is Misleading and process should probably be pulled into
     * several methods, but as of 12-07-2021, this method handles:
     * <ul>
     *     <li>Determining Primary click of the event</li>
     *     <li>Determining click count</li>
     *     <li>Determining currently selected item in triggered ListView</li>
     *     <li>Updating Display text on Single Click via
     *     {@link #updateTextOption(YoutubeOption)}</li>
     *     <li>Adding Item to Selected View, or Removing item, depending on
     *     which ListView triggered the event; in case of Multi Primary
     *     Clicks.</li>
     * </ul>
     */
    public void updateDisplayOptionMouseClick(final MouseEvent event)
            throws IOException {
        // This should always be one or the other
        final ListView<YoutubeOption> view =
                event.getSource().equals(optionView) ? optionView
                        : selectedView;

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            final int clickCount = event.getClickCount();
            final YoutubeOption item = view.getSelectionModel()
                    .getSelectedItem();

            // Update display item
            if (clickCount == 1 && item != null) {
                updateTextOption(item);
            }

            // Add selected item on multi click
            if (clickCount > 1 && item != null) {

                // For clarity reasons, 'view' is only used for verification
                if (view.equals(optionView)) {
                    addOption(item, selectedView.getItems());
                } else {
                    selectedView.getItems().remove(item);
                }
            }
        }
    }

    /**
     *
     */
    public void loadOption() throws IOException, JAXBException {
        final File xml = SceneUtils.promptUserGetFile(SAVES_DIR.listFiles());

        final YoutubeCommandBuilder ytb = (YoutubeCommandBuilder)
                SceneUtils.unmarshallXml(xml, YoutubeCommandBuilder.class);

        setSelectedOptions(ytb);
    }

    private void setSelectedOptions(final YoutubeCommandBuilder ytb) {
        final ObservableList<YoutubeOption> options =
                this.selectedView.getItems();
        options.clear();
        options.addAll(ytb.getOptions());
    }

    /**
     * Saves the current {@link #selectedView} to an .xml file assuming that
     * the View contains at least one item. If it doesn't then this method
     * just creates an Alert.
     *
     * Method Trace See (Order of Execution):
     * <ul>
     *     <li>{@link #compileBuilder()}</li>
     *     <li>{@link #getFileName()}</li>
     *     <li>{@link SceneUtils#marshallObject(Object, Class)}</li>
     *     <li>{@link #createAndWriteFile(File, String)}</li>
     * </ul>
     */
    public void saveOption() throws IOException, JAXBException {
        YoutubeCommandBuilder ytb = compileBuilder();

        if (ytb != null) {
            File output = getFileName();
            String xmlData =
                    SceneUtils.marshallObject(ytb,
                            YoutubeCommandBuilder.class);
            File xmlFile = new File(SAVES_DIR + "/" + output.getName()
                    + ".xml");
            createAndWriteFile(xmlFile, xmlData);

            alertOfFileState(xmlFile);
            // Error can't save empty file
        } else {
            Alert e = new Alert(Alert.AlertType.INFORMATION);
            e.setTitle("No Options Chosen!");
            e.setHeaderText("At-least one Option must be selected in order to"
                    + " create a Builder.");
            e.showAndWait();
        }
    }

    /**
     * Prompts the user for a file name and then returns it.
     *
     * @return File name with unfriendly characters removed. See
     * {@link SceneUtils#promptUserGetFileName(String, String)} for
     * implementation.
     */
    private File getFileName() throws IOException {
        final String header = "Please give this 'Option' a name...";
        final String desc = "Note that illegal filename characters will be " +
                "removed and all spaces will be removed as well...";

        return SceneUtils.promptUserGetFileName(header, desc);
    }

    /**
     * @return Parses all Options found in {@link #selectedView} to a
     * {@link YoutubeCommandBuilder} and then returns it. Unless there are no
     * items in the Builder, in which then it will return {@code null}.
     */
    private YoutubeCommandBuilder compileBuilder() {
        ObservableList<YoutubeOption> view = selectedView.getItems();

        if (view.size() > 0) {
            YoutubeCommandBuilder ytb = new YoutubeCommandBuilder();
            view.forEach(ytb::addOption);

            return ytb;

        } else {
            return null;
        }
    }

    /**
     * Creates the provided file, and then writes the provided data to it;
     * Does so in UTF-8.
     *
     * @param f File to create and write to.
     * @param data Data to write to file (String).
     */
    private void createAndWriteFile(final File f, final String data) {
        // Create file (Does not create a folder structure)
        if (!f.exists()) {
            try {
                if (f.createNewFile()) {
                    System.out.println("Created file: " + f);
                }
            } catch (IOException e) {
                System.out.println("Failed to create file!");
            }
        }

        // Write file
        if (f.isFile()) {
            try {
                FileUtils.write(f, data, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("Failed to write to file!");
            }
        }
    }

    /**
     * Posts an Alert providing information about a File. Stating whether or
     * not it has been created successfully or not and where it is/what it is.
     *
     * @param f File to alert of.
     */
    private void alertOfFileState(final File f) {
        Alert e = new Alert(Alert.AlertType.INFORMATION);
        String title;
        String desc;

        if (f.exists()) {
            title = "File Created!";
            desc = String.format("File: %s has been successfully created!", f);
        } else {
            title = "File Creation Failed!";
            desc = String.format("File: %s has not been successful in its " +
                    "creation.", f);
        }

        e.setTitle(title);
        e.setHeaderText(desc);
        e.showAndWait();
    }

    /**
     * Gets the Scene from {@link #getScene()} and then closes it.
     */
    public void closeScene() {
        this.getScene().closeStage();
    }

    /**
     * Event on, 'edit' button being pressed.
     * <p>
     * If current selected item at: {@link #selectedView} is not 'null' and
     * requires 'args' ({@link YoutubeOption#requiresInput()}) then this will
     * prompt for input as the args.
     * <p>
     * Note this allows the user to type anything, and nothing.
     */
    public void setArgs() throws IOException {
        YoutubeOption o = selectedView.getSelectionModel().getSelectedItem();

        if (o != null && o.requiresInput()) {
            o.setUserInput(getArgs(o));
            updateTextOption(o);
        }
    }

    /**
     * Displays input window and provides details about the provided Option.
     * Note this assumes that the provided option is not {@code null}, and
     * does require args.
     *
     * @param o Option to assign user args to.
     * @return User input as a String.
     */
    private String getArgs(final YoutubeOption o) throws IOException {
        final String header = String.format("%s ~ Args [%s]", o.name(),
                o.getArgs());
        final String description = o.getHelp(DEFAULT_LANGUAGE);
        return SceneUtils.promptUserGetString(header, description);
    }
}
