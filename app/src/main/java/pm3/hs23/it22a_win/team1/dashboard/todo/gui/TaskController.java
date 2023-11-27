package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import pm3.hs23.it22a_win.team1.dashboard.todo.ToDoTestLoad;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.Task;

/**
 * This controller processes all input coming from the window used to
 * create, modify or show a task.
 * 
 * @author elmiglor
 * @version 2023-11-12
 */
public class TaskController implements IsObserver{

    @FXML
    private StackPane btnCancel;

    @FXML
    private HBox btnContainer;

    @FXML
    private StackPane btnCreate;

    @FXML
    private StackPane btnDelete;

    @FXML
    private StackPane btnEdit;

    @FXML
    private CheckBox checkDailyList;

    @FXML
    private CheckBox checkDueInCalendar;

    @FXML
    private CheckBox checkExecutionInCalendar;

    @FXML
    private CheckBox checkPriority;

    @FXML
    private ChoiceBox<String> choiceBoxRepetitionUnit;

    @FXML
    private DatePicker pickerDueDate;

    @FXML
    private DatePicker pickerExecutionDate;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtRepetition;

    @FXML
    private TextField txtTitle;

    private ToDoDecorator listContainer;
    private Task task = null;
    private boolean fullscreen = false;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    void initialize() {
        System.out.println(javafx.scene.text.Font.getDefault());
        //System.out.println(javafx.scene.text.Font.getFamilies());
        btnContainer.setStyle(ToDoTestLoad.getColorScheme());
        txtDescription.addEventFilter(KeyEvent.KEY_PRESSED, new TextAreaTabToFocusEventHandler());
        Tooltip.install(btnCreate, new Tooltip("Aufgabe erstellen"));
        Tooltip.install(btnEdit, new Tooltip("Änderungen speichern")); // TODO wird datum aktualisiert? user informieren
        Tooltip.install(btnDelete, new Tooltip("Aufgabe löschen"));
        Tooltip.install(btnCancel, new Tooltip("Abbrechen"));

        btnCreate.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                createTask(null);
            }
        });
        btnEdit.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                editTask(null);
            }
        });
        btnDelete.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                deleteTask(null);
            }
        });
        btnCancel.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                cancel(null);
            }
        });
        btnCancel.getStyleClass().add("btn-active");

        pickerDueDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                // return string != null && !string.isEmpty() ? LocalDate.parse(string,
                // dateFormatter) : null;
                if (string != null && !string.isEmpty()) {
                    try {
                        LocalDate locDate = LocalDate.parse(string, dateFormatter);
                        pickerDueDate.getEditor().setBorder(null);
                        return locDate;
                    } catch (DateTimeParseException e) {
                        System.out.println(pickerDueDate.getEditor().getBorder());
                        pickerDueDate.getEditor().setBorder(new Border(new BorderStroke(Color.rgb(255, 0, 0),
                                BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT)));
                        throw e;
                    }
                }
                pickerDueDate.getEditor().setBorder(null);
                return null;
            }
        });
        pickerExecutionDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                // return string != null && !string.isEmpty() ? LocalDate.parse(string,
                // dateFormatter) : null;
                if (string != null && !string.isEmpty()) {
                    try {
                        LocalDate locDate = LocalDate.parse(string, dateFormatter);
                        pickerExecutionDate.getEditor().setBorder(null);
                        return locDate;
                    } catch (DateTimeParseException e) {
                        System.out.println(pickerExecutionDate.getEditor().getBorder());
                        pickerExecutionDate.getEditor().setBorder(new Border(new BorderStroke(Color.rgb(255, 0, 0),
                                BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT)));
                        pickerExecutionDate.requestFocus();
                        throw e;
                    }
                }
                pickerExecutionDate.getEditor().setBorder(null);
                return null;
            }
        });
        pickerDueDate.getEditor().setTextFormatter(new TextFormatter<>(pickerDueDate.getConverter()));
        pickerDueDate.valueProperty()
                .bindBidirectional((Property<LocalDate>) pickerDueDate.getEditor().getTextFormatter().valueProperty());
        pickerExecutionDate.getEditor().setTextFormatter(new TextFormatter<>(pickerExecutionDate.getConverter()));
        pickerExecutionDate.valueProperty().bindBidirectional(
                (Property<LocalDate>) pickerExecutionDate.getEditor().getTextFormatter().valueProperty());

        pickerExecutionDate.focusedProperty().addListener((obj, oldVal, newVal) -> {
            if (!newVal) {
                // pickerExecutionDate.setValue(pickerExecutionDate.getConverter().fromString(pickerExecutionDate.getEditor().getText()));
                System.out.println("obs " + pickerExecutionDate.getValue());
            }
        });
    }

    @FXML
    void cancel(MouseEvent event) {
        // TODO warning changes not saved
        btnCancel.fireEvent(new WindowEvent(btnCancel.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void closeWindow() {
        ((Stage) txtTitle.getScene().getWindow()).close();
    }

    @FXML
    void createTask(MouseEvent event) {
        if (!txtTitle.getText().isBlank()) {
            Optional<LocalDate> optionalDueDate = pickerDueDate.getValue() != null
                    ? Optional.of(pickerDueDate.getValue())
                    : Optional.empty();
            Optional<LocalDate> optionalExecutionDate = pickerExecutionDate.getValue() != null
                    ? Optional.of(pickerExecutionDate.getValue())
                    : Optional.empty();
            Optional<Period> optionalRepetitionIntervall = determinePeriod();
            if (optionalRepetitionIntervall.isPresent() && optionalDueDate.isEmpty()) {
                optionalDueDate = Optional.of(LocalDate.now());
            }
            listContainer.addTask(txtTitle.getText(), txtDescription.getText(), optionalDueDate,
                    checkDueInCalendar.isSelected(), optionalExecutionDate, checkExecutionInCalendar.isSelected(),
                    optionalRepetitionIntervall, checkPriority.isSelected(), checkDailyList.isSelected());
            if (!fullscreen) {
                closeWindow();
            }
        } else {
            requestFocusTxtTitle();
        }
    }

    @FXML
    void deleteTask(MouseEvent event) {
        Alert alert = DefaultAlert.getStyledAlert("Aufgabe löschen", "Möchten Sie die Aufgabe '" + task.getTitle() + "' wirklich löschen?", txtTitle.getScene().getWindow());
        alert.showAndWait().ifPresent(response -> {
            if (response.getButtonData() == ButtonData.CANCEL_CLOSE) {

            } else {
                listContainer.removeTask(task);
                if (!fullscreen) {
                    closeWindow();
                }
            }
        });
    }

    @FXML
    void editTask(MouseEvent event) {
        if (pickerDueDate.isFocused()) {
            pickerDueDate.getEditor().commitValue();
        }
        if (pickerExecutionDate.isFocused()) {
            pickerExecutionDate.getEditor().commitValue();
        }
        if (pickerDueDate.getEditor().getBorder() == null && pickerExecutionDate.getEditor().getBorder() == null) {
            Optional<LocalDate> optionalDueDate = pickerDueDate.getValue() != null
                    ? Optional.of(pickerDueDate.getValue())
                    : Optional.empty();
            Optional<LocalDate> optionalExecutionDate = pickerExecutionDate.getValue() != null
                    ? Optional.of(pickerExecutionDate.getValue())
                    : Optional.empty();
            Optional<Period> optionalRepetitionIntervall = determinePeriod();
            if (optionalRepetitionIntervall.isPresent() && optionalDueDate.isEmpty()) {
                optionalDueDate = Optional.of(LocalDate.now());
                pickerDueDate.setValue(optionalDueDate.get());
            }
            System.out.println(optionalRepetitionIntervall + " " + optionalDueDate);
            listContainer.modifyTask(task, txtTitle.getText(), txtDescription.getText(), optionalDueDate,
                    checkDueInCalendar.isSelected(), optionalExecutionDate, checkExecutionInCalendar.isSelected(),
                    optionalRepetitionIntervall, checkPriority.isSelected(), checkDailyList.isSelected());
            if (!fullscreen) {
                closeWindow();
            }
        }
    }

    void insertData(ToDoDecorator listContainer) {
        this.listContainer = listContainer;
        listContainer.addListener(this);
        if (!fullscreen) {
            Platform.runLater(() -> {
                btnCancel.getScene().setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        e.consume();
                        Event.fireEvent(btnCancel, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, true, true, true, true, true, true, true, true, true, true, null));
                    }
                });
                txtTitle.getScene().getWindow().setOnCloseRequest(e -> {
                    if (isModified()) {
                        showChangeAlert(e);
                    }
                });
            });
        }
        update(null);       //TODO define UpdateEvent
    }

    void setFullscreenMode(boolean fullscreen) {
        this.fullscreen = fullscreen;
        if (fullscreen) {
            ((VBox)btnContainer.getParent()).getChildren().remove(btnContainer);
            rootPane.getStyleClass().remove("cont-default-border");
        }
    }

    @Override
    public void update(UpdateEvent event) {
        resetTaskData();
        if (listContainer.getSelectedTask().isPresent()) {
            btnContainer.getChildren().remove(btnCreate);
            task = listContainer.getSelectedTask().get();
            txtTitle.setText(task.getTitle());
            txtDescription.setText(task.getDescription());
            task.getDueDate().ifPresent(dueDate -> pickerDueDate.setValue(dueDate));
            System.out.println(pickerDueDate.getValue());
            checkDueInCalendar.setSelected(task.isDueInCalendar());
            task.getExecutionDate().ifPresent(executionDate -> pickerExecutionDate.setValue(executionDate));
            checkExecutionInCalendar.setSelected(task.isExecutionInCalendar());
            parsePeriod(task.getRepetitionInterval());
            checkPriority.setSelected(task.isPriority());
            checkDailyList.setSelected(task.isInDailyList());
        } else {
            btnContainer.getChildren().remove(btnEdit);
            btnContainer.getChildren().remove(btnDelete);
        }
    }

    private void resetTaskData() {
            task = null;
            txtTitle.clear();
            txtDescription.clear();
            pickerDueDate.setValue(null);
            System.out.println(pickerDueDate.getValue() + " due date");
            checkDueInCalendar.setSelected(false);
            pickerExecutionDate.setValue(null);
            checkExecutionInCalendar.setSelected(false);
            txtRepetition.clear();
            choiceBoxRepetitionUnit.setValue(choiceBoxRepetitionUnit.getItems().get(0));
            checkPriority.setSelected(false);
            checkDailyList.setSelected(false);
    }

    boolean isModified() {
        if (task != null) {
            if (!txtTitle.getText().equals(task.getTitle()))
                return true;
            if ((txtDescription.getText() == null ^ task.getDescription() == null) || (txtDescription.getText() != null
                    && task.getDescription() != null && !txtDescription.getText().equals(task.getDescription())))
                return true;
            if ((pickerDueDate.getValue() == null ^ task.getDueDate().isEmpty()) || (pickerDueDate.getValue() != null
                    && !pickerDueDate.getValue().isEqual(task.getDueDate().orElse(null))))
                return true;
            if (checkDueInCalendar.isSelected() != task.isDueInCalendar())
                return true;
            if ((pickerExecutionDate.getValue() == null ^ task.getExecutionDate().isEmpty())
                    || (pickerExecutionDate.getValue() != null
                            && !pickerExecutionDate.getValue().isEqual(task.getExecutionDate().orElse(null))))
                return true;
            if (checkExecutionInCalendar.isSelected() != task.isExecutionInCalendar())
                return true;
            if ((determinePeriod().isEmpty() ^ task.getRepetitionInterval().isEmpty())
                    || (determinePeriod().isPresent()
                            && !determinePeriod().get().equals(task.getRepetitionInterval().orElse(null))))
                return true;
            if (checkPriority.isSelected() != task.isPriority())
                return true;
            if (checkDailyList.isSelected() != task.isInDailyList())
                return true;
        } else if (!txtTitle.getText().isBlank() || !txtDescription.getText().isBlank() || pickerDueDate.getValue() != null || pickerExecutionDate.getValue() != null) {
            System.out.println(pickerDueDate.getValue() +"pick");
            return true;
        }
        return false;
    }

    private Optional<Period> determinePeriod() {
        if (txtRepetition.getText().isBlank())
            return Optional.empty();
        System.out.println(choiceBoxRepetitionUnit.getValue());
        switch (choiceBoxRepetitionUnit.getValue()) {
            case "Tag(en)":
                return Optional.of(Period.ofDays(Integer.parseInt(txtRepetition.getText())));
            case "Woche(n)":
                return Optional.of(Period.ofWeeks(Integer.parseInt(txtRepetition.getText())));
            case "Monat(en)":
                return Optional.of(Period.ofMonths(Integer.parseInt(txtRepetition.getText())));
            case "Jahr(en)":
                return Optional.of(Period.ofYears(Integer.parseInt(txtRepetition.getText())));
        }
        return Optional.empty();
    }

    private void parsePeriod(Optional<Period> period) {
        if (period.isPresent()) {
            if (period.get().getDays() > 0 && period.get().getDays() % 7 != 0) {
                txtRepetition.setText(String.valueOf(period.get().getDays()));
            }
            if (period.get().getDays() % 7 == 0) {
                txtRepetition.setText(String.valueOf(period.get().getDays() / 7));
                choiceBoxRepetitionUnit.setValue(choiceBoxRepetitionUnit.getItems().get(1));
            }
            if (period.get().getMonths() > 0) {
                txtRepetition.setText(String.valueOf(period.get().getMonths()));
                choiceBoxRepetitionUnit.setValue(choiceBoxRepetitionUnit.getItems().get(2));
            }
            if (period.get().getYears() > 0) {
                txtRepetition.setText(String.valueOf(period.get().getYears()));
                choiceBoxRepetitionUnit.setValue(choiceBoxRepetitionUnit.getItems().get(3));
            }
        }
    }

    /**
     * 
     * @param event
     * @return true if modification should be dropped, false if cancel drop of modification
     */
    boolean showChangeAlert(WindowEvent event) {
        Alert alert = DefaultAlert.getStyledAlert("Es wurden Änderungen vorgenommen.", "Möchten Sie die Änderungen verwerfen?", txtTitle.getScene().getWindow());
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent()) {
            if (response.get().getButtonData() == ButtonData.CANCEL_CLOSE) {
                if (fullscreen) {
                    return false;
                } else {
                    event.consume();
                }
            }
            return true;
        };
        return true;
    }

    void requestFocusTxtTitle() {
        txtTitle.requestFocus();
        txtTitle.positionCaret(txtTitle.getLength());
    }

}

//code is copied from https://stackoverflow.com/questions/12860478/tab-key-navigation-in-javafx-textarea by Johan De Schutter
//goal is to swap the behaviour: TAB: swap to previous/next control, TAB+CTRL: insert tab in textarea
class TextAreaTabToFocusEventHandler implements EventHandler<KeyEvent>
{
    private static final String FOCUS_EVENT_TEXT = "TAB_TO_FOCUS_EVENT";

    @Override
    public void handle(final KeyEvent event)
    {
        if (!KeyCode.TAB.equals(event.getCode()))
        {
            return;
        }

        // handle events where the TAB key or TAB + CTRL key is pressed
        // so don't handle the event if the ALT, SHIFT or any other modifier key is pressed
        if (event.isAltDown() || event.isMetaDown() || event.isShiftDown())
        {
            return;
        }

        if (!(event.getSource() instanceof TextArea))
        {
            return;
        }

        final TextArea textArea = (TextArea) event.getSource();
        if (event.isControlDown())
        {
            // if the event text contains the special focus event text
            // => do not consume the event, and let the default behaviour (= move focus to the next control) happen.
            //
            // if the focus event text is not present, then the user has pressed CTRL + TAB key,
            // then consume the event and insert or replace selection with tab character
            if (!FOCUS_EVENT_TEXT.equalsIgnoreCase(event.getText()))
            {
                event.consume();
                textArea.replaceSelection("\t");
            }
        }
        else
        {
            // The default behaviour of the TextArea for the CTRL+TAB key is a move of focus to the next control.
            // So we consume the TAB key event, and fire a new event with the CTRL + TAB key.
            event.consume();
            final KeyEvent tabControlEvent = new KeyEvent(event.getSource(), event.getTarget(), event.getEventType(), event.getCharacter(), FOCUS_EVENT_TEXT, event.getCode(), event.isShiftDown(), true, event.isAltDown(), event.isMetaDown());
            textArea.fireEvent(tabControlEvent);
        }
    }
}
