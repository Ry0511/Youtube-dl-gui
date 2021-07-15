package gui.utility.fxmldialog.dialogs.booleandialog;

import gui.utility.fxmldialog.FXMLDialogController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Dialog Box which returns a Boolean value.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class BooleanDialog extends FXMLDialogController<Boolean> {

    /**
     * Reference to the Yes Button.
     */
    @FXML
    public Button yesBtn;

    /**
     * Reference to the Description text box.
     */
    @FXML
    public TextArea descriptionTextArea;

    /**
     * Reference to the header label.
     */
    @FXML
    public Label headerLabel;

    /**
     * Result of this dialog.
     */
    private boolean result;

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
    protected Boolean getResult() {
        return result;
    }

    /**
     * Sets the result and then exits.
     *
     * @param actionEvent Result is based on this.
     */
    public void btnClicked(ActionEvent actionEvent) {
        result = actionEvent.getSource().equals(yesBtn);
        this.getDialog().setResult(getResult());
        this.getDialog().close();
    }

    /**
     * Set the header label text to the provided text.
     *
     * @param s Header text to use.
     */
    public void setHeader(final String s) {
        this.headerLabel.setText(s);
    }

    /**
     * Set the description to the provided text.
     *
     * @param s Description text.
     */
    public void setDescription(final String s) {
        this.descriptionTextArea.setText(s);
    }
}
