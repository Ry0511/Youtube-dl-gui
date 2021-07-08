package gui.option.builder;

import gui.main.Main;
import gui.option.builder.fileloader.OptionFileLoader;
import gui.utility.SceneUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.apache.commons.io.FileUtils;
import youtube.dl.wrapper.command.builder.YoutubeCommandBuilder;
import youtube.dl.wrapper.command.builder.YoutubeHelp;
import youtube.dl.wrapper.command.builder.YoutubeOption;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

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
    private static final File SAVES_DIR = new File(Main.ABSOLUTE_PATH
            + "/option/saved/");

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
     * Loads the Option Picker scene ({@code this}), this sets up the
     * backend and then calls {@link Main#showNewStage(Scene, String)} to set
     * the main scene to {@code this} scene.
     */
    public static void loadScene() throws IOException {
        final String optionScene = "Option_Picker.fxml";
        final String title = "Option Builder";
        FXMLLoader loader =
                new FXMLLoader(Controller.class.getResource(optionScene));
        Parent root = loader.load();
        Controller e = loader.getController();
        e.initialise();

        final int width = 852;
        final int height = 512;
        Main.showNewStage(new Scene(root, width, height), title);
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
     * @param event Mouse Event attached to the Mouse button which triggered
     *              this method.
     */
    public void addOption(final MouseEvent event) throws IOException {
        selectionChanged(this.optionView
                .getSelectionModel().getSelectedItem());
        if (isDoubleClick(event, MouseButton.PRIMARY)) {
            addSelectedOption();
        }
    }

    /**
     * @return String User input from a dialog window.
     * @param e YT option which contains the content display message.
     */
    private String getUserInput(final YoutubeOption e) throws IOException {
        String content = e.name()
                + "\n"
                + e.getHelp(YoutubeHelp.EN);
        return SceneUtils.promptUserGetString(content);
    }

    /**
     * Updates the {@link #textArea} text to display the currently selected
     * {@link YoutubeOption}.
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
    public void showMainScene(final ActionEvent actionEvent) {
        Main.restoreMainScene();
    }

    /**
     * Removes the selected option from the {@link #selectedView}.
     */
    @FXML
    private void removeOption(final MouseEvent event) throws IOException {
        if (selectedView.getItems().size() > 0) {
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
     */
    @FXML
    private void updateDisplayOption(KeyEvent event)
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
        ObservableList<YoutubeOption> optionView = listView.getItems();


        // Only update target on Arrow up/down
        if (optionView.size() > 1) {
            if (isUpArrow || isDownArrow) {

                // Option display checking
                YoutubeOption option =
                        listView.getSelectionModel().getSelectedItem();

                // index checking
                int curIndex = listView.getSelectionModel().getSelectedIndex();
                int index = indexFind(curIndex, optionView, isUpArrow);

                // Updating
                selectionChanged(optionView.get(index));
            }
        }
    }

    /**
     * Increments or Decrements an item based on its index inside of the
     * List, note it only does so if it is inbounds and will not cause a
     * {@link IndexOutOfBoundsException}.
     */
    private static int indexFind(final int curIndex,
                                 ObservableList<YoutubeOption> items,
                                 boolean isUpArrow) {
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
        YoutubeOption e = optionView.getSelectionModel().getSelectedItem();

        if (e.requiresInput()) {
            e.setUserInput(getUserInput(e));
        }
        selectedView.getItems().add(e);
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
            String xmlData = marshallObject(parseSelected());

            // File name
            final String promptText = "What should this 'Option' be " +
                    "called/referenced as?";
            String fileName = SceneUtils.promptUserGetString(promptText);
            fileName = ensureFileName(fileName);


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
     *
     */
    private String ensureFileName(final String fileName) {
        String name = fileName.replaceAll(" ", "_");
        final String extension = ".xml";

        // If no name is typed give it one
        if (name.length() == 0) {
            Random r = new Random();
            final int limit = 999_999_999;
            name = String.valueOf(r.nextInt(limit));
        }

        return name + extension;
    }

    /**
     *
     */
    private String marshallObject(YoutubeCommandBuilder e) throws JAXBException {
        // Setup marshalling
        JAXBContext context =
                JAXBContext.newInstance(YoutubeCommandBuilder.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Marshall object
        StringWriter sw = new StringWriter();
        YoutubeCommandBuilder builder = parseSelected();
        marshaller.marshal(builder, sw);

        return sw.toString();
    }

    /**
     *
     */
    private Object unmarshallXml(final File xml, final Class<?> objClass)
            throws JAXBException {
        JAXBContext context =
                JAXBContext.newInstance(objClass);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(xml);
    }
}
