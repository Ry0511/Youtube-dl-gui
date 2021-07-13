package gui.utility;

import gui.utility.fxmldialog.FXMLDialog;
import gui.utility.input.FXMLScenes;
import gui.utility.input.fileinput.FileInputController;
import gui.utility.input.stringinput.TextInputController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;
import java.util.Random;

/**
 *
 */
public final class SceneUtils {

    /**
     * Default text for failed/null values.
     */
    public static final String ALT_EXIT = "No file grabbed";

    /**
     *
     */
    private static final String INVALID_FILE_CHARS = "[^a-zA-Z0-9.\\-]";

    /**
     * Hide constructor.
     */
    private SceneUtils() {
    }

    /**
     * Generates a Prompt text box with the text provided as the prompt. Of
     * which then a String element can be returned via the contents of the
     * TextArea.
     *
     * @param header      Header text to show.
     * @param description Description to show.
     * @return User Input, or "" if the TextArea has not been modified.
     */
    public static String promptUserGetString(final String header,
                                             final String description)
            throws IOException {

        FXMLDialog<String> fxmlDialog =
                new FXMLDialog<>(FXMLScenes.STRING_INPUT.getFxml());

        // Set text data
        TextInputController controller = (TextInputController)
                fxmlDialog.getController();
        controller.setHeaderLabel(header);
        controller.setDescriptionText(description);

        // Get result
        Optional<String> result = fxmlDialog.showAndWait();

        return result.orElse(ALT_EXIT);
    }

    /**
     * Shows a list of files and then allows the user to select a File from
     * the List.
     * @param file Files to show in the FileView.
     * @return {@code File} from the list of files provided, or a File
     * wrapping {@link #ALT_EXIT} if no File was grabbed.
     */
    public static File promptUserGetFile(final File[] file) throws IOException {
        final FXMLDialog<File> e =
                new FXMLDialog<>(FXMLScenes.FILE_INPUT.getFxml());
        final FileInputController fic = (FileInputController) e.getController();
        fic.listFiles(file);

        final Optional<File> result = e.showAndWait();

        return result.orElseGet(() -> new File(ALT_EXIT));
    }

    /**
     *
     */
    public static File promptUserGetFileName(final String header,
                                                   final String description)
            throws IOException {
        String s = promptUserGetString(header, description);

        // Remove invalid chars
        s = s.replaceAll(INVALID_FILE_CHARS, "");
        s = s.replaceAll(" ", "_");

        return new File(s);
    }

    /**
     * Marshall's the provided Object into XML format which is then returned
     * as a String.
     *
     * @param instance 'Object' to map into XML.
     * @param objClass Class to use as the base for the XML conversion.
     * @return XML Data as a String.
     */
    public static String marshallObject(final Object instance,
                                        final Class<?> objClass)
            throws JAXBException {
        // Setup marshalling
        JAXBContext context =
                JAXBContext.newInstance(objClass);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Marshall object
        StringWriter sw = new StringWriter();
        marshaller.marshal(instance, sw);

        // Return marshalled data
        return sw.toString();
    }

    /**
     * Parse a .XML file into an Object Instance.
     *
     * @param xml      File referencing a .XML file which can be Un-marshalled
     *                 into the provided Class.
     * @param objClass Class to Un-marshall into.
     * @return {@link Object} instance of the provided Class
     * @throws JAXBException If the XML file can't be parsed into the Class
     *                       provided.
     */
    public static Object unmarshallXml(final File xml,
                                       final Class<?> objClass)
            throws JAXBException {
        JAXBContext context =
                JAXBContext.newInstance(objClass);
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(xml);
    }

    /**
     * Increments the File name prefix based on the maximum prefix found.
     * See: {@link #countMaxPrefix(File[], String)}
     *
     * @param root   Root folder containing files which match the prefix.
     * @param prefix Prefix to match.
     * @return (Prefix + ( MaxValue + 1)). Else if the root is null or the
     * file is not a directory then (Prefix + {@link #randomInteger()}) is
     * returned.
     */
    public static String unnamedFile(final File root, final String prefix) {
        // Random name matching prefix
        String name = "err_" + prefix + randomInteger();

        // Get incremental name
        if (root != null && root.isDirectory()) {
            int maxValue = countMaxPrefix(root.listFiles(), prefix);
            name = (prefix + (maxValue + 1));
        }

        return name;
    }

    /**
     * Assumes that you have a folder structure along the lines of
     * "PREFIX_INTEGERVALUE" and returns the maximum "INTEGERVALUE" found.
     *
     * @param files  Files to search for a prefix and then note if its the
     *               largest number or not.
     * @param prefix Prefix to search for in the Files.
     * @return Highest/Maximum value found from the prefix. However the
     * default exit value is -1.
     */
    private static int countMaxPrefix(final File[] files, final String prefix) {
        int maxValue = -1;
        if (files != null) {
            for (File f : files) {
                String filename = f.getName();
                int prefixIndex = filename.indexOf(prefix);

                // Substring at prefix
                if (isValidSubString(prefixIndex,
                        filename.length(),
                        filename)) {

                    String value = filename.substring(prefixIndex);
                    value = value.replaceAll(prefix, "");
                    value = value.substring(0, value.indexOf("."));

                    // Only care about sub strings of integers
                    System.out.println(value);
                    if (value.matches("^[0-9]+$")) {
                        int parsedVal = Integer.parseInt(value);
                        maxValue = Math.max(parsedVal, maxValue);
                    }
                }
            }
        }
        return maxValue;
    }

    /**
     * @param start Start index of the substring.
     * @param end   End index of the substring.
     * @param str   String to substring.
     * @return {@code true} if the provided two indexes are a valid substring
     * in the provided String. Else will return {@code false}.
     */
    public static boolean isValidSubString(final int start,
                                           final int end,
                                           final String str) {
        final int length = str.length();

        // Ensure index
        boolean posBounds = (start >= 0) && (start <= length);
        boolean negBounds = (end > start) && (end <= length);

        return posBounds && negBounds;
    }

    /**
     * @return A Random Integer in the range 1 to 2147483647.
     */
    public static int randomInteger() {
        Random r = new Random();
        return r.nextInt(Integer.MAX_VALUE);
    }
}
