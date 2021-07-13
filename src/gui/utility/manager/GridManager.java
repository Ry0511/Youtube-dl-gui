package gui.utility.manager;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Re-created Class which closely mimics and has a lot of the same core
 * principles as my GridState from my Osu!ManiaUtils and CypherGram program.
 * But due to being heavily inexperienced and even now still being
 * inexperienced i've decided it's best to re-create with modular design in
 * mind.
 *
 * @author -Ry
 * @version 0.1
 * Copyright: N/A
 */
public class GridManager {

    /**
     *
     */
    private static final double COMPUTED_SIZE = -1.0;

    /**
     * The GridPane being managed.
     */
    private final GridPane gridPane;

    /**
     * Only tracks the RAW Object as for the application I want I need the
     * RAW Objects, but to avoid building around a single Object wrapping
     * this as Generic and Casting allows for Generic Use and also the
     * ability to have Hierarchy Chains (This can easily Store and Cast to
     * {@link Node}).
     */
    private Object[][] storedNodes;

    /**
     * Scale percentage value.
     */
    private static final int SCALE_PERCENTAGE = -1;

    /**
     * Initialise GridManager by providing the GridPane.
     *
     * @param gp The GridPane being Managed.
     */
    public GridManager(final GridPane gp) {
        this.gridPane = gp;
        this.clearGrid();
        setupStoredNodes();
    }

    /**
     *
     */
    public void clearGrid() {
        this.gridPane.getColumnConstraints().clear();
        this.gridPane.getRowConstraints().clear();
        this.gridPane.getChildren().clear();
    }

    /**
     * Initialises the {@link #storedNodes} 2D Array a new {@code Object[][]}
     * of the size of the GridPane using {@link #getColCount()} and
     * {@link #getRowCount()}.
     */
    private void setupStoredNodes() {
        this.storedNodes = new Object[this.getRowCount()][this.getColCount()];
    }

    /**
     * @return The number of {@link ColumnConstraints} present in the
     * {@link #gridPane}; Note this method returns the same value that {@code
     * gridPane.getColumnConstraints().size();} would.
     */
    public int getColCount() {
        return gridPane.getColumnConstraints().size();
    }

    /**
     * Clears the ColumnConstraints and then iteratively adds 'n' columns to
     * the GridPane.
     * <p>
     * NOTE: THIS METHOD CLEARS THE COLUMN CONSTRAINTS OF ALL NODES ON IT
     * MEANING THAT ALL NODES PRESENT ARE LOST. However the {@code Object
     * [][]} containing all the Nodes that were once there are not cleared
     * upon using this method and as such you can repopulate the Grid using
     * the Object[][] containing the Nodes.
     *
     * @param columns The number of Columns to add/set the GridPane to.
     */
    public void setColCount(final int columns) {
        gridPane.getColumnConstraints().clear();
        for (int i = 0; i < columns; i++) {
            final ColumnConstraints c = new ColumnConstraints();
            c.setHgrow(Priority.ALWAYS);

            c.setPrefWidth(COMPUTED_SIZE);
            c.setMinWidth(COMPUTED_SIZE);
            c.setMaxWidth(COMPUTED_SIZE);

            c.setHalignment(HPos.CENTER);
            c.setPercentWidth(SCALE_PERCENTAGE);
            gridPane.getColumnConstraints().add(c);
        }
    }

    /**
     * @return The number of {@link RowConstraints} present in the
     * {@link #gridPane}; Note this method returns the same value that {@code
     * gridPane.getRowConstraints().size();} would.
     */
    public int getRowCount() {
        return gridPane.getRowConstraints().size();
    }

    /**
     * Clears the RowConstraints and then iteratively adds 'n' Rows to
     * the GridPane.
     * <p>
     * NOTE: THIS METHOD CLEARS THE ROW CONSTRAINTS OF ALL NODES ON IT
     * MEANING THAT ALL NODES PRESENT ARE LOST. However the {@code Object
     * [][]} containing all the Nodes that were once there are not cleared
     * upon using this method and as such you can repopulate the Grid using
     * the Object[][] containing the Nodes.
     *
     * @param rows The number of Rows to add/set the GridPane to.
     */
    public void setRowCount(final int rows) {
        gridPane.getRowConstraints().clear();

        for (int i = 0; i < rows; i++) {
            final RowConstraints r = new RowConstraints();
            r.setVgrow(Priority.ALWAYS);

            r.setMinHeight(COMPUTED_SIZE);
            r.setPrefHeight(COMPUTED_SIZE);
            r.setMaxHeight(COMPUTED_SIZE);

            r.setPercentHeight(SCALE_PERCENTAGE);
            r.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(r);
        }
    }

    /**
     * Sets the Grid Size to the provided Two Values of Row, Col. Once setup
     * if:
     * <p>
     * {@code true} is passed through as the Third Arg then the
     * {@link #storedNodes} will be deleted and a new {@code Object[][]} will
     * be created in its place.
     * <p>
     * But if {@code false} is passed through as the Third Arg then the
     * {@link #storedNodes} will first be transferred over to the new Grid
     * before {@link #storedNodes} is recreated. Note that this is done
     * through {@link #getNodesInbound} and as such this will only map
     * valid indexes to the GridPane.
     */
    public void setGridSize(final int rows, final int cols,
                            final boolean clearNodes) {
        this.setRowCount(rows);
        this.setColCount(cols);
        if (clearNodes) {
            this.setupStoredNodes();
        } else {
            Object[][] nodes = getNodesInbound();
            this.setupStoredNodes();
            this.populateGrid(nodes);
        }
    }

    /**
     *
     */
    private Object[][] getNodesInbound() {
        final int rows = this.getRowCount();
        final int cols = this.getColCount();
        final Object[][] nodes = new Object[rows][cols];

        // Iterate Rows
        for (int i = 0; i < this.storedNodes.length; i++) {

            // Iterate Columns
            for (int j = 0; j < this.storedNodes[i].length; j++) {

                // Save nodes in-bounds
                final Object cur = storedNodes[i][j];
                if ((rows > i) && (cols > j) && (cur != null)) {
                    nodes[i][j] = cur;
                }
            }
        }
        return nodes;
    }

    /**
     * Populates the Grid with all non-null entries from the provided
     * Object[][].
     */
    public void populateGrid(final Object[][] nodes) {
        // Iterate Rows
        for (int i = 0; i < nodes.length; i++) {

            // Iterate Columns
            for (int j = 0; j < nodes[i].length; j++) {

                // Place Objects (I've been inconsistent with index
                // evaluation lmfao)
                final Object cur = nodes[i][j];
                final boolean inBounds = this.storedNodes[i].length > j;
                if (inBounds && cur != null) {
                    this.storedNodes[i][j] = cur;
                }
            }
        }
    }

    /**
     * Places the Provided Object into the Row, Col index provided if it is
     * in-bounds; Also note that the provided Object is placed in the index
     * specified regardless of if there is already an Object in that place/spot.
     *
     * @param obj The Object to place into the desired position.
     * @param row The Row to place the Object into.
     * @param col The Column to place the Object into.
     *
     * @throws MalformedParametersException If the provided (Row, Col) is
     * greater than the ({@link #getRowCount()}, {@link #getColCount()}).
     */
    public void addNode(final Object obj, final int row, final int col) {
        final boolean inRange = this.storedNodes[row].length > col;

        if (inRange) {
            this.storedNodes[row][col] = obj;

            // Exceptional
        } else {
            final String s = String.format("Placement Error: (%s, %s) > (%s, "
                            + "%s)%n", row, col,
                    this.getRowCount(), this.getColCount());
            throw new MalformedParametersException(s);
        }
    }

    /**
     * Grabs the Object at the provided Row, Col and passes it through a
     * Consumer Method.
     */
    public void showNode(final int row, final int col, final Function<Object,
            Node> fn) {
        final boolean inRange = this.storedNodes[row].length > col;

        if (inRange) {
            final Object o = this.storedNodes[row][col];
            this.gridPane.add(fn.apply(o), col, row);

            // Exceptional
        } else {
            final String s = String.format("Index Error: (%s, %s) > (%s, %s)"
                            + "[Provided ~ Actual Max]%n", row, col,
                    this.getRowCount(), this.getColCount());
            throw new MalformedParametersException(s);
        }
    }

    /**
     * Let's play operation kids.................
     */
    public void iterateAndApply(final Consumer<Object> operation) {
        for (Object[] storedNode : this.storedNodes) {
            for (final Object o : storedNode) {
                if (o != null) {
                    operation.accept(o);
                }
            }
        }
    }

    /**
     *
     */
    public ArrayList<Object> getAllNodes() {
        return Arrays.stream(this.storedNodes)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
