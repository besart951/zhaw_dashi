package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import pm3.hs23.it22a_win.team1.dashboard.todo.ToDoTestLoad;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.SortingType;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.Task;

/**
 * 
 * @author elmiglor
 * @version 2023-11-12
 */
public class ToDoFullScreenController implements IsObserver {

    @FXML
    private StackPane blurryPane;

    @FXML
    private Button btnEmptyTask;

    @FXML
    private Button btnEdit;

    @FXML
    private ChoiceBox<String> choiceBoxSorting;

    @FXML
    private StackPane containerTask;

    @FXML
    private ListView<String> containerListOfTaskLists;

    @FXML
    private StackPane containerAllTasks;

    @FXML
    private TextField inputField;

    @FXML
    private AnchorPane root;

    private ToDoDecorator toDoData;
    private ToDoBigController toDoBigController;
    private TaskController taskController;

    @FXML
    void initialize() {
        root.setStyle(ToDoTestLoad.getColorScheme());
        Platform.runLater(() -> {
            for (SortingType sortingType : SortingType.values()) {
                choiceBoxSorting.getItems().add(sortingType.getCaption());
            }
            choiceBoxSorting.setOnAction(e -> {
                toDoData.setCurrentSorting(SortingType.getSortingType(choiceBoxSorting.getValue()));
            });

            blurryPane.setOnMousePressed(e -> {
                blurryPane.setVisible(false);
            });

            containerListOfTaskLists.setOnDragOver(e -> {
                if (e.getTarget() instanceof Cell && ((Cell)e.getTarget()).getText() != null) {
                    e.acceptTransferModes(TransferMode.MOVE);
                    e.consume();
                }
            });

            containerListOfTaskLists.setOnDragDropped(e -> {            //TODO on hover set background, also drop on name, handle exceptions for dailylist
                if (e.getTarget() instanceof Cell && ((Cell)e.getTarget()).getText() != null) {

                    System.out.println("dropped task inside tasklist");
                    //Dragboard db = event.getDragboard(); //TODO prob add again

                    boolean success = false;

                    if (toDoBigController.getDraggedTask() != null) {
                        Task task = toDoBigController.getDraggedTask();
                        
                        success = toDoData.moveTaskToList(task, ((Cell)e.getTarget()).getText());
                    }
                    e.setDropCompleted(success); //TODO necessary
                    e.consume();

                }
            });
        });

    }

    private void loadAllTasks() {
        String allTasksView = "ToDoBig.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(allTasksView));
        Parent loadScreen;
        try {
            loadScreen = (Parent) loader.load();
            toDoBigController = loader.getController();
            toDoBigController.setFullscreenMode(true);
            toDoBigController.setModel(toDoData);
            ((Pane)loadScreen).setPrefHeight(470); //TODO real value
            containerAllTasks.getChildren().add(loadScreen);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void loadTask() {
        String taskView = "Task.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(taskView));
        Parent loadScreen;
        try {
            loadScreen = (Parent) loader.load();
            taskController = loader.getController();
            taskController.setFullscreenMode(true);
            taskController.insertData(toDoData);
            containerTask.getChildren().add(loadScreen);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @FXML
    void addListPopUp(ActionEvent event) {
        //workaround to have the caret centered
        inputField.setText(" ");
        inputField.clear();
        blurryPane.setVisible(true);
        inputField.requestFocus();
    }

    @FXML
    void inputFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            blurryPane.setVisible(false);
        }
    }

    @FXML
    void createList(ActionEvent event) {
        toDoData.addNewList(inputField.getText());
        blurryPane.setVisible(false);
    }

    @FXML
    void deleteList(ActionEvent event) {
        toDoBigController.deleteList(null);
    }

    @FXML
    void provideEmptyTask(ActionEvent event) {
        toDoData.setSelectedTask(Optional.empty());
    }

    @FXML
    void deleteTask(ActionEvent event) {
        System.out.println("deletedtask");
        if (toDoData.getSelectedTask().isPresent()) {
            taskController.deleteTask(null);
        } else {
            update(null); //TODO add update event
        }
    }

    @FXML
    void modifyTask(ActionEvent event) {
        System.out.println("modify task");
        if (toDoData.getSelectedTask().isPresent()) {
            taskController.editTask(null);
        } else {
            taskController.createTask(null);
        }
    }

    public void setModel(ToDoDecorator toDoDecorator) {
        toDoData = toDoDecorator;
        toDoData.addListener(this);
        containerListOfTaskLists.setItems(FXCollections.observableArrayList(toDoData.getNamesAllLists()));
        containerListOfTaskLists.getSelectionModel().select(toDoData.getNameSelectedList());
        containerListOfTaskLists.getSelectionModel().selectedItemProperty().addListener((obs, oldName, newName) -> {
            System.out.println("old "+oldName+"new "+newName);
            if (oldName != null && newName != null && !newName.equals(toDoData.getNameSelectedList())) {
                System.out.println("new set selected list");
                toDoData.setNameSelectedList(newName);
            }
        });
        containerAllTasks.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            System.out.println("pressedtriggered "+e.getSource()+" tar: "+e.getTarget());
            //if (!((Node)containerTask).isFocusWithin()) {
            if (!getAllNodes((Parent)containerTask).contains(e.getTarget()) && !getAllNodes((Parent)btnEdit).contains(e.getTarget()) && btnEdit != e.getTarget()) {
                System.out.println("focusnotwithin");
                if (taskController.isModified()) {
                    if (taskController.showChangeAlert(null)) {
                        toDoData.setSelectedTask(toDoData.getSelectedTask());
                    } else {
                        e.consume();
                    }
                }
            }
        });
        loadAllTasks();
        loadTask();
        update(null); //TODO define updateevent
    }

    @Override
    public void update(UpdateEvent updateEvent) {
        System.out.println("update loop triggered");
        updateTaskLists();
        choiceBoxSorting.setValue(toDoData.getCurrentSorting().getCaption());
        if (toDoData.getSelectedTask().isEmpty()) {
            taskController.requestFocusTxtTitle();
        }
        btnEmptyTask.setVisible(toDoData.getSelectedTask().isPresent());
    }

    private void updateTaskLists() {
        if (toDoData.getNamesAllLists().size() == containerListOfTaskLists.getItems().size()) {
            containerListOfTaskLists.getSelectionModel().select(toDoData.getNameSelectedList());
        } else if (toDoData.getNamesAllLists().size() < containerListOfTaskLists.getItems().size()) {
            containerListOfTaskLists.getSelectionModel().select(toDoData.getNameSelectedList());
            containerListOfTaskLists.requestFocus();
            containerListOfTaskLists.getItems().removeIf(x -> !toDoData.getNamesAllLists().contains(x));
        } else {
            int i = 0;
            for (i = 0; i < toDoData.getNamesAllLists().size()-1; i++) {
                if (!toDoData.getNamesAllLists().get(i).equals(containerListOfTaskLists.getItems().get(i))) {
                    break;
                }
            }
            containerListOfTaskLists.getItems().add(i, toDoData.getNamesAllLists().get(i));
            containerListOfTaskLists.getSelectionModel().select(toDoData.getNameSelectedList());
        }
    }




    private ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }


}

