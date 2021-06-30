package gui.main.process.tracker.gui;


import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import java.lang.reflect.MalformedParametersException;

/**
 *
 */
public class TrackerWindow {

    /**
     * Error Message.
     */
    private static final String NOT_MATCHING = "Provided Object isn't 'this' "
            + "Object.";

    /**
     * Static Label which doesn't change and is only used to inform of what
     * {@link #dynamicSpeed} represents.
     */
    private final Label staticSpeed = new Label("Speed: ");

    /**
     * Dynamic Label which will have it's text changed upon context updates
     * controlled by the Object holder/Owner.
     */
    private final Label dynamicSpeed = new Label("?");

    /**
     * Region/Spacer which will separate the Speed and ETA Trackers.
     */
    private final Region topSpacer = new Region();

    /**
     * Static Label which doesn't change and is only used to inform of what
     * {@link #dynamicEta} represents.
     */
    private final Label staticEta = new Label("ETA: ");

    /**
     * Dynamic Label which will have it's text changed upon context updates
     * controlled by the Object holder/Owner.
     */
    private final Label dynamicEta = new Label("?");

    /**
     * Dynamic Console Message centered in the BorderPane which is used to
     * display full messages; This is contained inside of a FlowPane which
     * will give it enough space to still be readable/manageable.
     */
    private final Label consoleMessage = new Label("?");

    /**
     * Progress Bar to track the Current Completion status of the Process
     * current being tracked.
     */
    private final ProgressBar progressBar = new ProgressBar();

    /**
     * Top Container which wraps the Top Section of the
     * {@link #mainContainer}; Specifically this Wraps the Objects:
     * {@link #staticSpeed}, {@link #dynamicSpeed}, {@link #topSpacer},
     * {@link #staticEta}, and {@link #dynamicEta}.
     */
    private final FlowPane topContainer = new FlowPane(staticSpeed,
            dynamicSpeed, topSpacer, staticEta, dynamicEta);

    /**
     * Middle Container which will wrap a Single Object that Being the
     * {@link #consoleMessage}.
     */
    private final FlowPane middleContainer = new FlowPane(consoleMessage);

    /**
     *
     */
    private final FlowPane bottomContainer = new FlowPane(progressBar);

    /**
     * Main Container which Everything is held inside of. And by
     * everything I mean {@code EVERYTHING} is held inside of this as this
     * entire Object is wrapped in this.
     */
    private final BorderPane mainContainer = new BorderPane();

    /**
     * Constructs the TrackerWindow Initialising all the Required setup data
     * for the Node Objects.
     */
    public TrackerWindow() {
        mainContainer.setTop(topContainer);
        mainContainer.setCenter(middleContainer);
        mainContainer.setBottom(bottomContainer);

        setStyles();
    }

    /**
     *
     */
    public BorderPane getIfMatch(final Object e) {
        if (e.equals(this)) {
            return this.mainContainer;
        } else {
            throw new MalformedParametersException(NOT_MATCHING);
        }
    }

    /**
     *
     */
    private void setStyles() {
        // BorderPane
        StaticStyleSetter.setBorderPaneDefaults(new BorderPane[]
                {mainContainer});

        // Labels
        StaticStyleSetter.setLabelDefaults(new Label[]{staticSpeed,
                dynamicSpeed, staticEta, dynamicEta, consoleMessage});

        // FlowPanes
        StaticStyleSetter.setFlowPaneDefaults(new FlowPane[]{topContainer,
                middleContainer, bottomContainer});

        // Progress Bar
        StaticStyleSetter.setProgressBarDefaults(new ProgressBar[]
                {progressBar});

        // Region
        StaticStyleSetter.setRegionDefaults(new Region[]{topSpacer});
    }

    /**
     *
     */
    public void setSpeed(final String s) {
        this.dynamicSpeed.setText(s);
    }

    /**
     *
     */
    public void setDynamicEta(final String s) {
        this.dynamicEta.setText(s);
    }

    /**
     *
     */
    public void setConsole(final String s) {
        this.consoleMessage.setText(s);
    }

    /**
     *
     */
    public void setProgress(final double progress) {
        this.progressBar.setProgress(progress);
    }
}
