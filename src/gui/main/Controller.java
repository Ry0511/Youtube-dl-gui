package gui.main;

import gui.option.builder.OptionPicker;
import gui.utility.FXMLScene;
import gui.utility.FXMLSceneController;
import gui.utility.fxmldialog.FXMLDialog;
import gui.utility.input.DialogFXML;
import gui.utility.input.fileinput.FileInputController;
import gui.utility.manager.GridManager;
import gui.utility.gui.TrackerWindow;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import youtube.dl.wrapper.ytdl.YoutubeDl;

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
    public void initialise() {
        gridManager = new GridManager(this.gridPane);
        final int rows = 2;
        final int columns = 3;
        gridManager.setGridSize(rows, columns, true);

        // Place Nodes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                TrackerWindow t = new TrackerWindow();
                gridManager.addNode(t, i, j);
                gridManager.showNode(i, j, t::getIfMatch);
                System.out.printf("Placed Node: {%s}, {%s, %s}%n", t, i, j);
            }
        }
    }

    /**
     * Loads the Option Builder Scene.
     */
    public void loadOptionBuilder() {
        FXMLSceneController controller = new OptionPicker();
        FXMLScene scene = new FXMLScene(controller);

        Stage s = new Stage();
        s.setScene(scene.getScene());

        s.showAndWait();
    }

    /**
     *
     */
    @FXML
    private void downloadSingle() throws IOException {
        FXMLDialog<File> e = new FXMLDialog<>(DialogFXML.FILE_INPUT.getFxml());
        FileInputController fic = (FileInputController) e.getController();

        fic.listFiles(gui.option.builder.Controller.SAVES_DIR.listFiles());

        Optional<File> result = e.showAndWait();

        result.ifPresent(file ->
                System.out.printf("%nObtained a result!!! [%s]%n", file));
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
    private void runSingle() {
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
