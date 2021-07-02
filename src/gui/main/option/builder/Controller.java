package gui.main.option.builder;

import gui.main.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import youtube.dl.wrapper.command.builder.YoutubeHelp;
import youtube.dl.wrapper.command.builder.YoutubeOption;

import java.io.IOException;
import java.util.Arrays;

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
    public ListView<YoutubeOption> optionView;

    /**
     * All the items which have been selected and are going to be parsed into
     * our {@link youtube.dl.wrapper.command.builder.YoutubeCommandBuilder}.
     */
    @FXML
    public ListView<YoutubeOption> selectedView;

    /**
     * TextArea which displays information about the currently selected
     * Option from either the {@link #optionView} or {@link #selectedView}.
     */
    @FXML
    public TextArea textArea;

    /**
     * Populates the {@link #optionView} with all the items inside of
     * {@link YoutubeOption}.
     */
    public void initialise() {
        if (optionView.getItems().size() == 0) {
            Arrays.stream(YoutubeOption.values())
                    .forEachOrdered(e -> optionView.getItems()
                            .add(e));
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
                new FXMLLoader(gui.main.option.builder
                        .Controller.class.getResource(optionScene));
        Parent root = loader.load();
        gui.main.option.builder.Controller e = loader.getController();
        e.initialise();
        Main.showNewStage(new Scene(root, 852, 512), title);
    }

    /**
     * Determines if a MouseEvent was a Double Click or not.
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
     */
    public void addOption(final MouseEvent event) throws IOException {
        selectionChanged(this.optionView
                .getSelectionModel().getSelectedItem());
        if (isDoubleClick(event, MouseButton.PRIMARY)) {
            YoutubeOption e = optionView.getSelectionModel().getSelectedItem();
            selectedView.getItems().add(e);
        }
    }

    /**
     * Updates the {@link #textArea} text to display the currently selected
     * {@link YoutubeOption}.
     */
    private void selectionChanged(YoutubeOption e) throws IOException {
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
                YoutubeOption e = selectedView.getSelectionModel()
                        .getSelectedItem();
                selectedView.getItems().remove(e);
            }
        }
    }

    /**
     * Upon a new item being selected via keypress {@link KeyCode#UP} or
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
        int index = isUpArrow ? - 1 : 1;
        index = curIndex + index;

        // Keep values in bounds
        if (index < items.size() && index >= 0) {
            return index;

        } else {
            return curIndex;
        }
    }
}
