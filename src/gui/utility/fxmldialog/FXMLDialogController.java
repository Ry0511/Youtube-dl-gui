package gui.utility.fxmldialog;

/**
 * FXML Dialog Controller, template Class; Class will allow the
 * implementation of {@code FXML} and {@link javafx.scene.control.Dialog}
 * without the extra setup data, only needing to provide the URL to the FXML
 * file.
 * <p>
 * Designed to be used in-tandem with {@link FXMLDialog} which initialises
 * the Dialog and allows it be shown.
 *
 *
 * @param <T> Return type of the data to be obtained through 'this' Dialog.
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public abstract class FXMLDialogController<T> {

    /**
     * Reference to Dialog.
     */
    private FXMLDialog<T> dialog;

    /**
     * Set the Dialog for this Controller. This is called through the main
     * {@link FXMLDialog} and is already setup.
     *
     * @param dia Reference to {@link FXMLDialog} which initialised this
     *            instance of the Controller.
     */
    public void setDialog(final FXMLDialog<T> dia) {
        this.dialog = dia;
    }

    /**
     * @return The Dialog associated with this Controller.
     */
    public FXMLDialog<T> getDialog() {
        return this.dialog;
    }

    /**
     * Closes the Stage associated with this Dialog.
     */
    public void closeStage() {
        this.dialog.close();
    }

    /**
     * Upon the Dialog being 'completed' or terminated by the user, this
     * method should be called in-order to get the users response and assign
     * it. This should contain a Base case such as {@code null} or any other
     * specified 'Default Value'.
     * <p>
     * However, the proper exit shall be getting the value of the input
     * fields for this Dialog such as returning the content inside of a
     * {@link javafx.scene.control.TextArea}.
     * <p>
     * Note: The data gathered here should be passed to
     * {@link javafx.scene.control.Dialog#setResult(Object)} in someone upon
     * the termination condition being met.
     * @return 'Default Value' or the data this Dialog has been given.
     */
    protected abstract T getResult();
}
