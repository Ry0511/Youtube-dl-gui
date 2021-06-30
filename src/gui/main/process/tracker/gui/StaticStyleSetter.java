package gui.main.process.tracker.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;

/**
 *
 */
public final class StaticStyleSetter {

    /**
     * Hide Constructor
     */
    private StaticStyleSetter() {
    }

    /**
     *
     */
    public static void setLabelDefaults(final Label[] labels) {
        for (Label l : labels) {
            applyComputedSizes(l);
            l.setTextAlignment(TextAlignment.JUSTIFY);
            l.setWrapText(true);
            l.setTextOverrun(OverrunStyle.ELLIPSIS);
            l.setMaxWidth(450.0);
            l.setAlignment(Pos.CENTER);
            l.setStyle("-fx-font-size: 18px;");
            l.applyCss();
        }
    }

    /**
     *
     */
    public static void setFlowPaneDefaults(final FlowPane[] flowPanes) {
        for (FlowPane p : flowPanes) {
            applyComputedSizes(p);
            p.setPadding(new Insets(4));
            p.setAlignment(Pos.CENTER);
            p.setRowValignment(VPos.CENTER);
            p.setColumnHalignment(HPos.CENTER);
        }
    }

    /**
     *
     */
    public static void setProgressBarDefaults(final ProgressBar[] proBars) {
        final double xScale = 3;
        for (ProgressBar p : proBars) {
            applyComputedSizes(p);
            p.setScaleX(xScale);
            p.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            p.setCenterShape(true);
        }
    }

    /**
     *
     */
    public static void setRegionDefaults(final Region[] regions) {
        final double spacing = 50;
        for (Region r : regions) {
            applyComputedSizes(r);
            r.setCenterShape(true);
            r.setPadding(new Insets(0, spacing,0, spacing));
        }
    }

    /**
     *
     */
    public static void setBorderPaneDefaults(final BorderPane[] borderPanes) {
        for (BorderPane b : borderPanes) {
            applyComputedSizes(b);
            b.setPadding(new Insets(6));
            b.setCenterShape(true);
        }
    }

    /**
     *
     */
    public static void applyComputedSizes(final Node n) {
        Region r = (Region) n;
        r.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        r.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        r.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    }
}
