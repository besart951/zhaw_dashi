package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.SortingType;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.Task;

/**
 *
 * @author elmiglor
 * @version 2023-10-27
 */
public class ToDoBigController implements IsObserver {

    @FXML
    private ComboBox<String> comboBoxListSelector;

    @FXML
    private Label lblOrderBy;

    @FXML
    private ScrollPane scrollContainer;

    @FXML
    private VBox taskContainer;

    @FXML
    private Menu sortingMenu;

    private static final int ICON_SIZE = 18;
    private static final String CREATE_NEW_LIST = "+ neue Liste erstellen";
    private HBox atHboxAdd;
    private ToggleGroup sortingGroup = new ToggleGroup();
    private HBox taskToBeDragged;
    private int originalIndexOfDraggedTask;
    private Task draggedTask;
    private DataFormat HBOX_TYPE = new DataFormat("hbox");
    private Image imgPlus;
    private Image imgPriority;
    private Image imgDone;
    private Image imgDailyList;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private ToDoDecorator listContainer;

    @FXML
    void initialize() {
        for(SortingType type : SortingType.values()) {
            RadioMenuItem radioMenuItem = new RadioMenuItem(type.getCaption());
            radioMenuItem.setOnAction(e -> sortTasks(e));
            sortingGroup.getToggles().add(radioMenuItem);
            sortingMenu.getItems().add(radioMenuItem);
        }

        imgPlus = new Image(getClass().getResource("images/plus_big.png").toExternalForm());
        imgPriority = new Image(getClass().getResource("images/overdue_big.png").toExternalForm());
        imgDone = new Image(getClass().getResource("images/done_big.png").toExternalForm());
        imgDailyList = new Image(getClass().getResource("images/add_daily_list_big.png").toExternalForm());

        atHboxAdd = new HBox();
        atHboxAdd.getStyleClass().add("at-hbox-add");
        //atHboxAdd.setOnMouseClicked(e->addNewTask(new ActionEvent()));
        atHboxAdd.setOnMouseClicked(e -> addNewTask());
        StackPane atStackPanePlus = new StackPane();
        atStackPanePlus.getStyleClass().addAll("dt-stackpane-done", "dt-btn-default");
        ImageView atImgPlus = new ImageView(imgPlus);
        atImgPlus.setFitHeight(ICON_SIZE);
        atImgPlus.setFitWidth(ICON_SIZE);
        //dtImgDone.getStyleClass().add("dt-img-done");
        atStackPanePlus.getChildren().add(atImgPlus);
        Tooltip.install(atHboxAdd, new Tooltip("neue Aufgabe erstellen"));
        atHboxAdd.getChildren().add(atStackPanePlus);
    }

    void addNewTask() {
        openTaskWindow(Optional.empty());
    }

    private void openTaskWindow(Optional<Task> task) {
        String taskView = "Task.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(taskView));
        Parent loadScreen;
        try {
            loadScreen = (Parent) loader.load();
            ((TaskController)loader.getController()).insertData(listContainer, task);
            Scene scene = new Scene(loadScreen);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initOwner(taskContainer.getScene().getWindow());
            stage.setScene(scene);
            stage.setTitle("Test - ToDo");
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResource("images/plus_big.png").toExternalForm()));
            Window root = taskContainer.getScene().getWindow();
            Platform.runLater(() -> {
                stage.setX(root.getX()+(root.getWidth()-stage.getWidth())/2);
                stage.setY(root.getY()+(root.getHeight()-stage.getHeight())/2);
            });
            stage.show();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @FXML
    void deleteList(ActionEvent event) {
        listContainer.removeList(listContainer.getNameSelectedList());
        //refreshCbListSelector(); //TODO triggers change with null, null check(!)
    }

    @FXML
    void listSelectionChanged(ActionEvent event) {
        System.out.println("the "+comboBoxListSelector.getValue());
        if (comboBoxListSelector.getValue() != null /*&& !comboBoxListSelector.getValue().isBlank()*/) {
            if (listContainer.containsList(comboBoxListSelector.getValue())) {
                comboBoxListSelector.setEditable(false);
                comboBoxListSelector.getEditor().setBorder(null);
                if (!listContainer.getNameSelectedList().equals(comboBoxListSelector.getValue())) {
                    listContainer.setNameSelectedList(comboBoxListSelector.getValue());
                }
                System.out.println("got here some how");
            } else if (comboBoxListSelector.getValue().equals(CREATE_NEW_LIST)) {
                comboBoxListSelector.setEditable(true);
                Platform.runLater(()-> {
                    comboBoxListSelector.getEditor().clear();
                });
            } else {
                comboBoxListSelector.setEditable(false);
                comboBoxListSelector.getEditor().setBorder(null);
                if (listContainer.addNewList(comboBoxListSelector.getValue())) {
                    listContainer.setNameSelectedList(comboBoxListSelector.getValue()); //TODO Model oder controller?
                } else {
                    comboBoxListSelector.getEditor().setBorder(new Border(new BorderStroke(Color.rgb(255,0,0), BorderStrokeStyle.SOLID,null, BorderWidths.DEFAULT)));
                    comboBoxListSelector.getSelectionModel().select(CREATE_NEW_LIST);
                }

            }
        } else {
                comboBoxListSelector.setEditable(false);
                comboBoxListSelector.getSelectionModel().select(listContainer.getNameSelectedList());
                System.out.println("got here");
                update(UpdateEvent.UPDATE_TASKS); //TODO remove?
        }
    }

    void moveTask(MouseEvent event, Task task) {
        System.out.println("started dragging task");
        if (taskToBeDragged == null) {
            taskToBeDragged = (HBox)event.getSource();
            originalIndexOfDraggedTask = taskContainer.getChildren().indexOf(taskToBeDragged);
            draggedTask = task;

            Dragboard db = taskToBeDragged.startDragAndDrop(TransferMode.MOVE);
            //db.setDragView(((HBox)event.getSource()).snapshot(null,null));
            ClipboardContent content = new ClipboardContent();
            content.put(HBOX_TYPE, taskToBeDragged.toString());
            db.setContent(content);
            event.consume();
        }
    }

    void sortTasks(ActionEvent event) {
        System.out.println(((RadioMenuItem)event.getSource()).getText());
        listContainer.setCurrentSorting(SortingType.getSortingType(((RadioMenuItem)event.getSource()).getText()));
    }

    void taskDropped(DragEvent event) {
        System.out.println("dropped task inside container");
        //Dragboard db = event.getDragboard(); //TODO prob add again

        boolean success = false;

        if (taskToBeDragged != null) {
            ObservableList<Node> tasks = taskContainer.getChildren();
            int draggedIndex = tasks.indexOf(taskToBeDragged);
            int droppedIndex = tasks.indexOf(event.getSource());
            taskContainer.getChildren().remove(draggedIndex);
            taskContainer.getChildren().add(droppedIndex, taskToBeDragged);
            taskToBeDragged = null;

            listContainer.moveTaskToPosition(draggedTask, droppedIndex);

            success = true;
        }
        event.setDropCompleted(success); //TODO necessary
        event.consume();
    }

    @Override
    public void update(UpdateEvent updateEvent) {
        if(updateEvent == UpdateEvent.UPDATE_LIST) {
            refreshCbListSelector();
        }

        for (Toggle toggle : sortingGroup.getToggles()) {
            if (listContainer.getCurrentSorting() == SortingType.getSortingType(((RadioMenuItem)toggle).getText())) {
                toggle.setSelected(true);
            }
        }
        lblOrderBy.setText(listContainer.getCurrentSorting().getInUseLabel());

        taskContainer.getChildren().clear();
        for (Task task : listContainer.getAllTasks()) {
            generateTask(task);
        }
        taskContainer.getChildren().add(atHboxAdd);
    }

    public void setModel(ToDoDecorator listContainer) {
        this.listContainer = listContainer;
        listContainer.addListener(this);

        comboBoxListSelector.getItems().add(CREATE_NEW_LIST);
        comboBoxListSelector.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue && comboBoxListSelector.isEditable()) {
                refreshCbListSelector();
            }
        });
        update(UpdateEvent.UPDATE_LIST);
    }

    private void refreshCbListSelector() {
        ObservableList<String> allTaskLists = FXCollections.observableArrayList(listContainer.getNamesAllLists());

        comboBoxListSelector.getSelectionModel().select(CREATE_NEW_LIST); //TODO alt add dummy entry and remove after manipulation
        System.out.println(comboBoxListSelector.getSelectionModel().getSelectedItem());

        Platform.runLater(() -> {
            comboBoxListSelector.getItems().removeIf(x -> !CREATE_NEW_LIST.equals(x));
            System.out.println("removed tasklists");

            comboBoxListSelector.getItems().addAll(0, allTaskLists);
            System.out.println("added tasklists");

            comboBoxListSelector.getSelectionModel().select(listContainer.getNameSelectedList());
            System.out.println("reselected active list: "+comboBoxListSelector.getSelectionModel().getSelectedItem());
        });
    }

    private void generateTask(Task task) {
        HBox dtHboxBase = generateDtHbox(task);

        StackPane dtStackPanePriority = generateDtStackPanePriority(task);

        Label dtLblTask = new Label(task.getTitle());
        dtLblTask.getStyleClass().add("dt-lbl-task");

        Optional<LocalDate> sortingDate = listContainer.getNameForLblCurrentSorting(task, listContainer.getCurrentSorting());
        String sortingCaption = sortingDate.isPresent() ? sortingDate.get().format(dateFormatter) : "";
        Label dtLblSorting = new Label(sortingCaption);
        dtLblSorting.getStyleClass().add("dt-lbl-sorting");

        StackPane dtStackPaneDone = generateDtStackPaneDone(task);

        StackPane dtStackPaneAddDailyList = generateDtStackPaneAddDailyList(task);

        if (task.isDone()) {
            dtStackPaneDone.getStyleClass().add("dt-btn-active");
            dtLblTask.getStyleClass().add("dt-lbl-done");
            dtLblSorting.getStyleClass().add("dt-lbl-done");
        } else {
            dtStackPaneDone.getStyleClass().remove("dt-btn-active");
            dtLblTask.getStyleClass().remove("dt-lbl-done");
            dtLblSorting.getStyleClass().remove("dt-lbl-done");
        }

        dtHboxBase.getChildren().addAll(dtStackPanePriority,dtLblTask,dtLblSorting,dtStackPaneDone,dtStackPaneAddDailyList);

        taskContainer.getChildren().add(dtHboxBase);
    }

    private HBox generateDtHbox(Task task) {
        HBox dtHboxBase = new HBox();
        dtHboxBase.getStyleClass().add("dt-hbox-base");
        dtHboxBase.setOnDragDetected(e -> moveTask(e, task));
        dtHboxBase.setOnDragDone(e -> {
            if (!e.isAccepted() || taskToBeDragged != null) {
                System.out.println("dropped outside container, task reset");
                taskContainer.getChildren().remove(taskToBeDragged);
                taskContainer.getChildren().add(originalIndexOfDraggedTask, taskToBeDragged);
                taskToBeDragged = null;
            } else {
                System.out.println("task successfully moved");
            }
            e.consume();
        });
        dtHboxBase.setOnDragEntered(e -> {
            if (e.getSource() != taskToBeDragged) {
                ObservableList<Node> items = taskContainer.getChildren();
                int draggedIndex = items.indexOf(taskToBeDragged);
                System.out.println(draggedIndex);
                int droppedIndex = items.indexOf(e.getSource());
                System.out.println(droppedIndex);

                taskContainer.getChildren().remove(draggedIndex);
                taskContainer.getChildren().add(droppedIndex, taskToBeDragged);

                System.out.println(scrollContainer.getVvalue());
                scrollContainer.setVvalue(scrollContainer.getVvalue()+1.0*(droppedIndex-draggedIndex)/(taskContainer.getChildren().size()-4));
                System.out.println(scrollContainer.getVvalue());
                /*
                 * TODO experimental =D
                 *
                 *
                 */
            }
            e.consume();
        });
        dtHboxBase.setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.MOVE);
            if (e.getSource() == taskToBeDragged) {
                taskToBeDragged.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,CornerRadii.EMPTY,Insets.EMPTY)));
            }
            e.consume();
        });
        dtHboxBase.setOnDragDropped(e -> taskDropped(e));

        dtHboxBase.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                System.out.println("Show details");
                openTaskWindow(Optional.of(task));
            }
        });
        return dtHboxBase;
    }

    private StackPane generateDtStackPanePriority(Task task) {
        StackPane dtStackPanePriority = new StackPane();
        dtStackPanePriority.getStyleClass().addAll("dt-btn-default", "dt-stackpane-priority");
        ImageView dtImgPriority = new ImageView();
        dtImgPriority.setFitHeight(ICON_SIZE);
        dtImgPriority.setFitWidth(ICON_SIZE);
        //dtImgPriority.getStyleClass().add("dt-img-priority");
        dtStackPanePriority.getChildren().add(dtImgPriority);
        Tooltip.install(dtStackPanePriority, new Tooltip("Priorisieren"));
        if (task.isDone()) {
            dtStackPanePriority.setMouseTransparent(true);
        } else {
            dtStackPanePriority.setOnMouseClicked(e -> listContainer.togglePriority(task));
            if (task.isPriority()) {
                dtImgPriority.setImage(imgPriority);
            }
        }
        return dtStackPanePriority;
    }

    private StackPane generateDtStackPaneDone(Task task) {
        StackPane dtStackPaneDone = new StackPane();
        dtStackPaneDone.getStyleClass().add("dt-btn-default");
        ImageView dtImgDone = new ImageView(imgDone);
        dtImgDone.setFitHeight(ICON_SIZE);
        dtImgDone.setFitWidth(ICON_SIZE);
        //dtImgDone.getStyleClass().add("dt-img-done");
        dtStackPaneDone.getChildren().add(dtImgDone);
        Tooltip.install(dtStackPaneDone, new Tooltip("Aufgabe erledigt"));
        dtStackPaneDone.setOnMouseClicked(e-> {
            listContainer.toggleDone(task);
        });
        return dtStackPaneDone;
    }

    private StackPane generateDtStackPaneAddDailyList(Task task) {
        StackPane dtStackPaneAddDailyList = new StackPane();
        dtStackPaneAddDailyList.getStyleClass().add("dt-btn-default");
        if (task.isInDailyList()) {
            dtStackPaneAddDailyList.getStyleClass().add("dt-btn-active");
        } else {
            dtStackPaneAddDailyList.getStyleClass().remove("dt-btn-active");
        }
        ImageView dtImgAddDailyList = new ImageView(imgDailyList);
        dtImgAddDailyList.setFitHeight(ICON_SIZE);
        dtImgAddDailyList.setFitWidth(ICON_SIZE);
        //dtImgAddDailyList.getStyleClass().add("dt-img-add-daily-list");
        dtStackPaneAddDailyList.getChildren().add(dtImgAddDailyList);
        Tooltip.install(dtStackPaneAddDailyList, new Tooltip("zur Tages-Liste hinzufügen"));
        dtStackPaneAddDailyList.setOnMouseClicked(e-> listContainer.toggleTaskInDailyList(task));
        return dtStackPaneAddDailyList;
    }


}
