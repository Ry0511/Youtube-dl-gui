package gui.main.option.builder;

import gui.main.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
 *
 */
public class Controller {

    /**
     *
     */
    @FXML
    public ListView<YoutubeOption> optionView;

    /**
     *
     */
    @FXML
    public Button returnBtn;

    /**
     *
     */
    @FXML
    public ListView<YoutubeOption> selectedView;

    /**
     *
     */
    @FXML
    public TextArea textArea;

    /**
     *
     */
    public void initialise() {
        if (optionView.getItems().size() == 0) {
            Arrays.stream(YoutubeOption.values())
                    .forEachOrdered(e -> optionView.getItems()
                            .add(e));
        }
    }

    /**
     *
     */
    private static boolean isDoubleClick(final MouseEvent event,
                                         final MouseButton btn) {
        if (event.getButton() == btn) {
            return event.getClickCount() == 2;
        }
        return false;
    }

    /**
     *
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
     *
     */
    private void selectionChanged(YoutubeOption e) throws IOException {
        String s = String.format("%s%n%n%s%n%n%s",
                e.name(),
                e.getCommand(),
                e.getHelp(YoutubeHelp.EN));
        textArea.setText(s);
    }

    /**
     *
     */
    public void showMainScene(final ActionEvent actionEvent) {
        Main.restoreMainScene();
    }

    /**
     *
     */
    @FXML
    private void removeOption(final MouseEvent event) throws IOException {
        if (selectedView.getItems().size() > 0) {
            selectionChanged(this.selectedView
                    .getSelectionModel().getSelectedItem());
            if (isDoubleClick(event, MouseButton.PRIMARY)) {
                YoutubeOption e = selectedView.getSelectionModel().getSelectedItem();
                selectedView.getItems().remove(e);
            }
        }
    }

    /**
     *
     * @param event
     */
    @FXML
    private void updateDisplayOption(KeyEvent event)
            throws IOException {
        boolean isUpArrow =
                event.getCode().getName().equals(KeyCode.UP.getName());
        boolean isDownArrow =
                event.getCode().getName().equals(KeyCode.DOWN.getName());

        // Only update on Arrow up/down
        if (isUpArrow || isDownArrow) {

            // Option display checking
            YoutubeOption e =
                    optionView.getSelectionModel().getSelectedItem();
            if (event.getTarget().equals(selectedView)) {
                e = selectedView.getSelectionModel().getSelectedItem();
            }

            // index checking
            int curIndex = optionView.getItems().indexOf(e);
            int index = indexFind(curIndex, optionView.getItems(), isUpArrow);

            // Updating
            selectionChanged(optionView.getItems().get(index));
        }
    }

    /**
     *
     */
    private static int indexFind(final int curIndex,
                                 ObservableList<YoutubeOption> items,
                                 boolean isUpArrow) {
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
