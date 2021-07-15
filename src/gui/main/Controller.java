package gui.main;

import gui.downloadtracker.TrackerWindow;
import gui.downloadtracker.trackercontainer.TrackerContainer;
import gui.optionbuilder.OptionPicker;
import gui.utility.SceneUtils;
import gui.utility.fxmlscene.FXMLScene;
import gui.utility.fxmldialog.FXMLDialog;
import gui.utility.fxmldialog.dialogs.FXMLScenes;
import gui.utility.fxmldialog.dialogs.fileinput.FileInputController;
import gui.utility.manager.GridManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import youtube.dl.wrapper.command.builder.YoutubeCommandBuilder;
import youtube.dl.wrapper.ytdl.YoutubeDl;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the Main GUI page for the program.
 *
 * @author -Ry
 * @version 0.1 [First Build]
 * Copyright: N/A
 */
public class Controller {

    /**
     * Static Reference to the Default location for the "youtube-dl.exe" file.
     */
    private static final YoutubeDl YT_DL = new YoutubeDl(new File(
            "Required/yt-dl/youtube-dl.exe"));

    /**
     * Static Reference to the Default "FFMPEG.exe" file. (Located inside of
     * Binaries.)
     */
    private static final File FFMPEG_EXE = new File(
            "Required/ffmpeg/bin/ffmpeg.exe");

    /**
     * GridPane which contains all currently Downloading Objects and their
     * Tracking Status.
     */
    @FXML
    private GridPane gridPane;

    /**
     *
     */
    private GridManager gridManager;

    /**
     *
     */
    private static YoutubeCommandBuilder getBuilder() throws IOException,
            JAXBException {
        FXMLDialog<File> e = new FXMLDialog<>(FXMLScenes.FILE_INPUT.getFxml());
        FileInputController fic = (FileInputController) e.getController();

        fic.listFiles(OptionPicker.SAVES_DIR.listFiles());

        Optional<File> result = e.showAndWait();

        if (result.isPresent()) {
            return (YoutubeCommandBuilder) SceneUtils
                    .unmarshallXml(result.get(), YoutubeCommandBuilder.class);
        } else {
            return null;
        }
    }

    /**
     *
     */
    private static FXMLScene buildTrackerContainer() {
        FXMLScene e = new FXMLScene(new TrackerContainer());
        Stage s = new Stage();
        s.setScene(e.getScene());
        s.setTitle("Tracker Container");
        return e;
    }

    /**
     * Constructs a Tracker Window Object.
     *
     * @return Constructed tracker window; through the means of FXMLScene.
     */
    private static FXMLScene buildTrackerWindow() {
        return new FXMLScene(new TrackerWindow());
    }

    /**
     * @return String Link to a webpage.
     */
    private static String getLink() throws IOException {
        final String header = "Post a single link to download...";
        final String desc = "Link should not contain any new lines and should"
                + " conform to the Universal Resource Loader requirements.";

        return SceneUtils.promptUserGetString(header, desc);
    }

    /**
     * This will be removed, as it's primarily for testing. In it's place it
     * there will just be information about yt-dl, and ffmpeg.
     */
    public void initialise() {
        // todo remove this?
    }

    /**
     * Loads the Option Builder Scene.
     */
    public void loadOptionBuilder() {
        // Construct scene from class extending FXMLSceneController
        FXMLScene scene = new FXMLScene(new OptionPicker());

        // Create stage, and set scene
        Stage s = new Stage();
        s.setScene(scene.getScene());

        // poof
        s.showAndWait();
    }

    /**
     *
     */
    @FXML
    private void downloadSingle() throws IOException, JAXBException {
        // Setup view
        final YoutubeCommandBuilder ytb = getBuilder();
        final String link  = getLink();
        FXMLScene container = buildTrackerContainer();
        FXMLScene task = buildTrackerWindow();

        // Assign task to container
        TrackerWindow tw = (TrackerWindow) task.getController();
        TrackerContainer tc = (TrackerContainer) container.getController();
        tc.addTask(tw);

        // Start task
        container.getStage().show();
        YT_DL.windowsDownload(link, ytb, tw::acceptDownload);
    }

    /**
     *
     */
    @FXML
    private void downloadBatch() {
    }

    /**
     *
     */
    @FXML
    private void runSingle() throws JAXBException, IOException {
        // Setup view
        YoutubeCommandBuilder ytb = getBuilder();
        FXMLScene container = buildTrackerContainer();
        FXMLScene task = buildTrackerWindow();

        // Assign task to container
        TrackerWindow tw = (TrackerWindow) task.getController();
        TrackerContainer tc = (TrackerContainer) container.getController();
        tc.addTask(tw);

        // Start task
        container.getStage().show();
        YT_DL.windowsExec(ytb, tw::acceptCommand);
    }

    /**
     *
     */
    @FXML
    private void runBatch() {
    }

    /**
     *
     */
    @FXML
    private void cutFile() {
    }

    /**
     *
     */
    @FXML
    private void compressFile() {
    }
}
