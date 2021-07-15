package gui.utility.fxmlscene.alerts;

/**
 * Alert Base for required operations to an Alert Scene Object.
 * <p>
 * I don't know what I'm doing :)
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public interface AlertBase {

    /**
     * Set's the 'Header' text of this to the provided String.
     *
     * @param s String to display as the header.
     */
    void setHeader(final String s);

    /**
     * @return Current header text.
     */
    String getHeader();

    /**
     * Set's the 'Description' text of this to the provided String.
     *
     * @param s String to display as the Description.
     */
    void setDescription(final String s);

    /**
     * @return Current Description text.
     */
    String getDescription();

    /**
     * Set the Style Sheet of the Alert to the provided Style Sheet.
     *
     * @param cssPath Style sheet to load.
     */
    void setStyleSheet(final String cssPath);
}
