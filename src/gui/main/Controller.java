package gui.main;

import gui.main.gridpane.manager.GridManager;
import gui.main.process.tracker.gui.TrackerWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import youtube.dl.wrapper.ytdl.YoutubeDl;

import java.io.File;
import java.io.IOException;

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
    private static final YoutubeDl YT_DL = new YoutubeDl(new File
            ("Required/yt-dl/youtube-dl.exe"));

    /**
     * Static Reference to the Default "FFMPEG.exe" file. (Located inside of
     * Binaries.)
     */
    private static final File FFMPEG_EXE = new File
            ("Required/ffmpeg/bin/ffmpeg.exe");

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
        final int rows = 3;
        final int columns = 2;
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
     *
     */
    @FXML
    private void downloadSingle(ActionEvent actionEvent) {

    }

    /**
     *
     */
    @FXML
    private void downloadBatch(ActionEvent actionEvent) throws IOException {
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
     *
     */
    @FXML
    private void runSingle(ActionEvent actionEvent) {
    }

    /**
     *
     */
    @FXML
    private void runBatch(ActionEvent actionEvent) {
    }

    /**
     *
     */
    @FXML
    private void cutFile(ActionEvent actionEvent) {
    }

    /**
     *
     */
    @FXML
    private void compressFile(ActionEvent actionEvent) {
    }
}
