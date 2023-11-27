package pm3.hs23.it22a_win.team1.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorData;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorSmallController;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoBigController;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoDecorator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {

    @FXML
    private GridPane gridPane;

    private final List<WidgetWrapper> widgetWrappers = new ArrayList<>();

    private Pane draggedWidget;

    private Map<Class, WidgetData> widgetModels;

    JsonHelper jsonHelper = new JsonHelper();

    @FXML
    private void initialize() {

        createGridPane();
        System.out.println("Initialized");
    }

    private void createGridPane() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                stackPane.setMinHeight(100);
                stackPane.setMaxHeight(100);
                stackPane.setMinWidth(100);
                stackPane.setMaxWidth(100);
                //stackPane.setStyle("-fx-border-color: black");
                setDropEvent(stackPane);
                gridPane.add(stackPane, i, j);
            }
        }
    }

    private void loadCustomGridWidgetsLayout() {
        try {
            Map<WidgetType, GridPosition> gridWidgetsLayout = jsonHelper.loadObjectFromJsonFile(new TypeReference<>() {}, new File("src/main/resources/pm3/hs23/it22a_win/team1/dashboard/Data/DashboardGridLayout.json"), false);
            gridWidgetsLayout.forEach((widgetType, gridPosition) -> {
                try {
                    Pane widget = loadWidget(widgetType);
                    if (widget != null) {
                        setDragEvent(widget);
                        widgetWrappers.add(new WidgetWrapper(widgetType, widget));
                        gridPane.add(widget, gridPosition.getX(), gridPosition.getY(), (int) (widget.getPrefWidth() / 100), (int) (widget.getPrefHeight() / 100));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pane loadWidget(WidgetType widgetType) throws IOException {
        WidgetController controller;
        return switch (widgetType) {
            case TO_DO_BIG -> {
                controller = new ToDoBigController();
                yield controller.loadModelNode(widgetModels.get(ToDoDecorator.class));
            }
            case GRADE_CALCULATOR_SMALL -> {
                controller = new GradeCalculatorSmallController();
                yield controller.loadModelNode(widgetModels.get(GradeCalculatorData.class));
            }
            default -> null;
        };
    }

    private void setDropEvent(Node node) {
        node.setOnDragOver(event -> {
            if (event.getGestureSource() != node && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                node.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
            }

            event.consume();
        });

        node.setOnDragExited(event -> {
            // Remove the highlight when the drag moves away
            node.setStyle("");
        });


        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                int x = GridPane.getColumnIndex(node);
                int y = GridPane.getRowIndex(node);
                int x2 = GridPane.getColumnIndex(draggedWidget);
                int y2 = GridPane.getRowIndex(draggedWidget);
                gridPane.getChildren().remove(node);
                gridPane.getChildren().remove(draggedWidget);
                gridPane.add(node, x2, y2);
                gridPane.add(draggedWidget, x, y, (int) (draggedWidget.getWidth() / 100), (int) (draggedWidget.getHeight() / 100));
                safeDashboardLayout(widgetWrappers);
                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }

            event.consume();
        });
    }

    private void setDragEvent(Pane widgetPane) {
        widgetPane.setOnDragDetected(event -> {
            draggedWidget = widgetPane;
            Dragboard dragboard = widgetPane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("pane");
            dragboard.setDragView(widgetPane.snapshot(null, null));
            dragboard.setDragViewOffsetX(event.getX());
            dragboard.setDragViewOffsetY(event.getY());
            dragboard.setContent(content);
            event.consume();
        });
    }

    private void safeDashboardLayout(List<WidgetWrapper> widgetWrappers) {
        Map<WidgetType, GridPosition> gridWidgetsLayout = new HashMap<>();
        for (WidgetWrapper widgetWrapper : widgetWrappers) {
            GridPosition gridPosition = new GridPosition(GridPane.getColumnIndex(widgetWrapper.widget), GridPane.getRowIndex(widgetWrapper.widget));
            gridWidgetsLayout.put(widgetWrapper.widgetType, gridPosition);
        }
        jsonHelper.saveObjectInJsonFile(gridWidgetsLayout, new File("src/main/resources/pm3/hs23/it22a_win/team1/dashboard/Data/DashboardGridLayout.json"));
    }

    public void setWidgetModels(Map<Class, WidgetData> widgetModels) {
        this.widgetModels = widgetModels;
        loadCustomGridWidgetsLayout();
        jsonHelper.saveObjectInJsonFile(widgetModels, new File("src/main/resources/pm3/hs23/it22a_win/team1/dashboard/Data/DataModels.json"));
    }
}
