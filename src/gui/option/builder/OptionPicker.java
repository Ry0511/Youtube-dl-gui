package gui.option.builder;

import gui.main.Main;
import gui.utility.FXMLSceneController;
import gui.utility.SceneUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import youtube.dl.wrapper.command.builder.YoutubeCommandBuilder;
import youtube.dl.wrapper.command.builder.YoutubeHelp;
import youtube.dl.wrapper.command.builder.YoutubeOption;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private static final File SAVES_DIR = new File(Main.ABSOLUTE_PATH
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
     *
     */
    public void removeSelectedOptionButtonClick() {
        final YoutubeOption ytb =
                selectedView.getSelectionModel().getSelectedItem();
        if (ytb != null) {
            selectedView.getItems().remove(ytb);
        }
    }

    /**
     *
     */
    public void addSelectedOptionButtonClick() {
        final YoutubeOption ytb =
                optionView.getSelectionModel().getSelectedItem();
        if (ytb != null) {
            selectedView.getItems().add(ytb);
        }
    }

    /**
     * Updates Option based on target; If Selected View changed then, update
     * with Selected View and if Option changed then update with Option view.
     */
    public void updateDisplayOptionKeyPress(final KeyEvent event) throws IOException {
        ListView<YoutubeOption> view = event.getTarget().equals(optionView)
                ? optionView : selectedView;
        YoutubeOption item = view.getSelectionModel().getSelectedItem();

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
     *
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
     *
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
                    selectedView.getItems().add(item);
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
     *
     */
    public void saveOption() {
    }

    /**
     *
     */
    public void closeScene() {
        this.getScene().closeStage();
    }
}
