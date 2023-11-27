package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import static pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialItem.Frequency;

/**
 * The {@code ItemChooserDialogController} is responsible for handling the UI
 * logic of the item chooser dialog.
 * It facilitates the creation of new financial items, manages the input
 * validation,
 * and updates the financial planner data model accordingly.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
public class ItemChooserDialogController {

    @FXML
    private Button okButton;
    @FXML
    private Label validationMessageLabel;
    @FXML
    private TextField amountNameField;
    @FXML
    private TextField amountQuantityField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private CheckBox hasEndDateCheckBox;
    @FXML
    private ComboBox<Frequency> intervalComboBox;
    @FXML
    private ComboBox<Type> typeComboBox;

    private FinancialPlannerData financialPlannerData;
    // This Regex pattern matches a number with up to two decimal places
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("\\d+(\\.\\d{0,2})?");
    // This Regex pattern matches a string containing only letters, numbers, and
    // spaces
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\s]+");

    /**
     * Initializes the controller's main functionalities including setup of combo
     * boxes,
     * date pickers, button state bindings, and input validation listeners.
     */
    @FXML
    private void initialize() {
        setupComboBoxes();
        setupOkButton();
        setupDatePickers();
        setupValidationListeners();
        setupIntervalComboBoxListener();
    }

    /**
     * Configures the type and interval combo boxes with appropriate options and
     * custom cell factories
     * to display descriptive text for each choice.
     */
    private void setupComboBoxes() {
        typeComboBox.setCellFactory(param -> new ListCell<Type>() {
            @Override
            protected void updateItem(Type item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });

        typeComboBox.setButtonCell(new ListCell<Type>() {
            @Override
            protected void updateItem(Type item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });

        typeComboBox.getItems().addAll(Type.INCOME, Type.EXPENSE);
        intervalComboBox.getItems().addAll(Frequency.values());
        intervalComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Frequency frequency) {
                return frequency.getDescription();
            }

            @Override
            public Frequency fromString(String string) {
                return intervalComboBox.getItems().stream()
                    .filter(item -> item.getDescription().equals(string))
                    .findFirst()
                    .orElse(null);
            }
        });
    }

    /**
     * Binds the disable property of the 'OK' button to the validation state of the
     * input fields,
     * effectively disabling it when the input does not meet the validation
     * criteria.
     */
    private void setupOkButton() {
        okButton.disableProperty().bind(createBindings());
    }

    /**
     * Adds listeners to the start and end date pickers to enforce the rule that the
     * start date
     * cannot occur after the end date and vice versa. It also sets default values
     * for these pickers.
     */
    private void setupDatePickers() {
        startDatePicker.valueProperty().addListener(this::startDateChanged);
        endDatePicker.valueProperty().addListener(this::endDateChanged);
    }

    /**
     * Ensures that the start date cannot occur after the end date.
     * If the start date is changed to a date after the end date, the end date is
     * adjusted accordingly.
     *
     * @param obs    The observable value.
     * @param oldVal The old value.
     * @param newVal The new value.
     */
    private void startDateChanged(ObservableValue<? extends LocalDate> obs, LocalDate oldVal, LocalDate newVal) {
        if (endDatePicker.getValue() == null || newVal == null)
            return;
        if (newVal.isAfter(endDatePicker.getValue())) {
            endDatePicker.setValue(newVal.plusDays(1));
        }
    }

    /**
     * Ensures that the end date cannot occur before the start date.
     * If the end date is changed to a date before the start date, the start date is
     * adjusted accordingly.
     *
     * @param obs    The observable value.
     * @param oldVal The old value.
     * @param newVal The new value.
     */
    private void endDateChanged(ObservableValue<? extends LocalDate> obs, LocalDate oldVal, LocalDate newVal) {
        if (startDatePicker.getValue() == null || newVal == null)
            return;
        if (newVal.isBefore(startDatePicker.getValue())) {
            startDatePicker.setValue(newVal.minusDays(1));
        }
    }

    /**
     * Adds listeners to the amount name and quantity fields to validate the input
     * and display
     * appropriate error messages.
     */
    private void setupValidationListeners() {
        amountNameField.textProperty().addListener(this::validateAmountNameField);
        amountQuantityField.textProperty().addListener(this::validateAmountQuantityField);
    }

    /**
     * Validates the input of the amount name field and displays an error message if
     * the input is invalid.
     *
     * @param obs    The observable value.
     * @param oldVal The old value.
     * @param newVal The new value.
     */
    private void validateAmountNameField(ObservableValue<? extends String> obs, String oldVal, String newVal) {
        if (!NAME_PATTERN.matcher(newVal).matches()) {
            applyValidationStyle(amountNameField, "Nur Buchstaben, Zahlen und Leerzeichen sind erlaubt.");
        } else {
            clearValidationStyle(amountNameField);
        }
    }

    /**
     * Validates the input of the amount quantity field and displays an error
     * message if the input is invalid.
     *
     * @param obs    The observable value.
     * @param oldVal The old value.
     * @param newVal The new value.
     */
    private void validateAmountQuantityField(ObservableValue<? extends String> obs, String oldVal, String newVal) {
        if (!AMOUNT_PATTERN.matcher(newVal).matches()) {
            applyValidationStyle(amountQuantityField, "Geben Sie einen gültigen Betrag ein (z.B. 123 oder 123.45).");
        } else {
            clearValidationStyle(amountQuantityField);
        }
    }

    /**
     * Applies a validation style to the given control and displays the given
     * message in the validation message label.
     *
     * @param control The control to apply the validation style to.
     * @param message The message to display in the validation message label.
     */
    private void applyValidationStyle(Control control, String message) {
        control.setStyle("-fx-border-color: red; -fx-border-radius: 5;");
        validationMessageLabel.setText(message);
    }

    /**
     * Clears the validation style from the given control and removes the message
     *
     * @param control The control to clear the validation style from.
     */
    private void clearValidationStyle(Control control) {
        control.setStyle("");
        validationMessageLabel.setText("");
    }

    /**
     * Adds a listener to the interval combo box to disable the end date picker when
     * the interval is set to one-time.
     */
    private void setupIntervalComboBoxListener() {
        intervalComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isIntervalSet = newVal == Frequency.ONETIME;
            hasEndDateCheckBox.setDisable(isIntervalSet);
            endDatePicker.setDisable(isIntervalSet);
            if (isIntervalSet) {
                hasEndDateCheckBox.setSelected(false);
                endDatePicker.setValue(null);
            }
        });
    }

    /**
     * Creates a new financial item based on the input in the dialog and adds it to
     * the financial planner data model.
     */
    @FXML
    private void createFinancialItem() {
        Type selectedType = typeComboBox.getValue();
        if (selectedType == null) {
            new Alert(Alert.AlertType.ERROR, "Ungültiger Typ ausgewählt").show();
            return;
        }

        String name = amountNameField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = hasEndDateCheckBox.isSelected() ? null : endDatePicker.getValue();
        Frequency frequency = intervalComboBox.getValue();

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountQuantityField.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Ungültiges Betragsformat").show();
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            new Alert(Alert.AlertType.ERROR, "Der Betrag kann nicht negativ sein").show();
            return;
        }

        FinancialItem item;
        if (selectedType == Type.INCOME) {
            amount = amount.abs();
            if (endDate == null && frequency == Frequency.ONETIME) {
                item = new OneTimeTransaction(name, amount, startDate);
            } else {
                item = new Income(name, amount, frequency, startDate, endDate);
            }
        } else if (selectedType == Type.EXPENSE) {
            amount = amount.negate();
            if (endDate == null && frequency == Frequency.ONETIME) {
                item = new OneTimeTransaction(name, amount, startDate);
            } else {
                item = new Expense(name, amount, frequency, startDate, endDate);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Ungültiger Typ ausgewählt").show();
            return;
        }
        financialPlannerData.addFinancialItem(item);
    }

    /**
     * Displays a dialog for adding a new income item, loading its content from an
     * FXML file.
     * Sets the financial planner data for the controller and handles potential
     * IOExceptions.
     *
     * @param financialPlannerData The financial data model for the application.
     */
    public void showAddIncomeDialog(FinancialPlannerData financialPlannerData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource("/pm3/hs23/it22a_win/team1/dashboard/financialplanner/ItemChooserDialog.fxml"));
            Parent root = fxmlLoader.load();
            ItemChooserDialogController controller = fxmlLoader.getController();
            controller.setFinancialPlannerData(financialPlannerData);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane((DialogPane) root);
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/pm3/hs23/it22a_win/team1/dashboard/icons/money.png"));
            stage.setTitle("Erstellen Sie einen neuen Eintrag");

            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and returns a BooleanBinding that determines the enabled state of the
     * 'OK' button.
     *
     * @return A BooleanBinding object used to control the enabled state of the 'OK'
     * button.
     */
    private BooleanBinding createBindings() {
        return Bindings.createBooleanBinding(() -> {
                boolean isIntervalSet = intervalComboBox.getValue() == Frequency.ONETIME;
                return typeComboBox.getValue() == null ||
                    !NAME_PATTERN.matcher(amountNameField.getText()).matches() ||
                    !isValidAmount(amountQuantityField.getText()) ||
                    startDatePicker.getValue() == null ||
                    (!isIntervalSet && (!hasEndDateCheckBox.isSelected() && endDatePicker.getValue() == null)) ||
                    (startDatePicker.getValue() != null && endDatePicker.getValue() != null &&
                        startDatePicker.getValue().isAfter(endDatePicker.getValue()));
            }, typeComboBox.valueProperty(), amountNameField.textProperty(), amountQuantityField.textProperty(),
            startDatePicker.valueProperty(), endDatePicker.valueProperty(), hasEndDateCheckBox.selectedProperty(),
            intervalComboBox.valueProperty());
    }

    /**
     * Validates the given string to see if it represents a valid monetary amount.
     * The method checks if the string can be converted to a BigDecimal, is
     * non-negative, and has up to two decimal places.
     *
     * @param amountStr The string representation of the amount to be validated.
     * @return true if the amount is valid, false otherwise.
     */
    private boolean isValidAmount(String amountStr) {
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            return amount.compareTo(BigDecimal.ZERO) >= 0 && amount.scale() <= 2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sets the financial planner data for the controller.
     *
     * @param financialPlannerData The financial data model for the application.
     */
    private void setFinancialPlannerData(FinancialPlannerData financialPlannerData) {
        this.financialPlannerData = financialPlannerData;
    }

    /**
     * Handles the status of the 'Has End Date' check box.
     * If the check box is selected, the end date picker is disabled.
     */
    @FXML
    public void hasEndDateCheckBoxStatus() {
        endDatePicker.setDisable(hasEndDateCheckBox.isSelected());
    }

    /**
     * Closes the dialog window.
     *
     * @param actionEvent The event that triggered the action.
     */
    @FXML
    private void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * The Type enumeration defines the possible types of financial items that can
     * be created
     * using the item chooser dialog.
     */
    private enum Type {
        INCOME("Einnahmen"),
        EXPENSE("Ausgaben");

        private final String description;

        Type(String description) {
            this.description = description;
        }

        /**
         * Retrieves the descriptive name of the financial item type.
         *
         * @return A string representing the descriptive name of the type.
         */
        public String getDescription() {
            return description;
        }
    }
}
