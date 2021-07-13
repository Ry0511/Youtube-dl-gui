package gui.utility.input.fileinput;

import gui.utility.fxmldialog.FXMLDialogController;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

/**
 *
 */
public class FileInputController extends FXMLDialogController<File> {

    /**
     * Files to view.
     */
    @FXML
    private ListView<File> fileView;

    /**
     * Upon the Dialog being 'completed' or terminated by the user, this
     * method should be called in-order to get the users response and assign
     * it. This should contain a Base case such as {@code null} or any other
     * specified 'Default Value'.
     * <p>
     * However, the proper exit shall be getting the value of the input
     * fields for this Dialog such as returning the content inside of a
     * {@link TextArea}.
     * <p>
     * Note: The data gathered here should be passed to
     * {@link Dialog#setResult(Object)} in someone upon
     * the termination condition being met.
     *
     * @return 'Default Value' or the data this Dialog has been given.
     */
    @Override
    protected File getResult() {
        return fileView.getSelectionModel().getSelectedItem();
    }

    /**
     * Shows all {@code Files} which from the provided files which are {@code
     * NotNull} and are true files which pass {@link File#isFile()}.
     *
     * @param files Files to show.
     */
    public void listFiles(final File[] files) {
        if (files != null) {
            Stream.of(files)
                    .filter(Objects::nonNull)
                    .filter(File::isFile)
                    .forEach(i -> fileView.getItems().add(i));
        }
    }

    /**
     *
     */
    @FXML
    private void deleteFile() {
        final File item = fileView.getSelectionModel().getSelectedItem();

        // Delete if exists
        if (item != null && hasItems()) {
            fileView.getItems().remove(item);
            FileUtils.deleteQuietly(item);
        }
    }

    /**
     *
     */
    @FXML
    private void selectFile() {
        final File item = fileView.getSelectionModel().getSelectedItem();

        if (item != null && hasItems()) {
            this.getDialog().setResult(getResult());
            this.getDialog().close();
        }
    }

    /**
     * @return {@code true} if the number of items inside of the Fileview is
     * greater than zero.
     */
    private boolean hasItems() {
        return this.fileView.getItems().size() > 0;
    }
}
