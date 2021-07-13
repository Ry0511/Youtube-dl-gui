package gui.utility.input.stringinput;

import gui.utility.fxmldialog.FXMLDialogController;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Dialog which contains a 'Header' label and two Text Areas, One for
 * Description the other for the user to type into.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class TextInputController extends FXMLDialogController<String> {

    /**
     * Reference to the header label.
     */
    @FXML
    private Label headerLabel;

    /**
     * Reference to the Input text where the user will type their data/answer.
     */
    @FXML
    private TextArea inputText;

    /**
     * Reference to the description text where the information related to the
     * header is held.
     */
    @FXML
    private TextArea descriptionText;

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
    protected String getResult() {
        return inputText.getText();
    }

    /**
     * Closes the scene, sets the result and exits.
     */
    @FXML
    private void closeScene() {
        this.getDialog().setResult(getResult());
        this.getDialog().close();
    }

    /**
     * Sets the Header Label text to the provided text.
     *
     * @param text Provided text to use as the header (Not window title).
     */
    public void setHeaderLabel(final String text) {
        this.headerLabel.setText(text);
    }

    /**
     * Sets the description of the header to the provided text.
     *
     * @param desc Provided text to use as the description for the header.
     */
    public void setDescriptionText(final String desc) {
        this.descriptionText.setText(desc);
    }
}
