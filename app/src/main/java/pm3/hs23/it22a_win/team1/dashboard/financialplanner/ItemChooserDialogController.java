package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialItem.Frequency;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import javafx.util.StringConverter;





public class ItemChooserDialogController {

    @FXML private ButtonType pressButton;
    @FXML private ButtonType pressCancleButton;
    @FXML private DialogPane dialogPane;
    @FXML private TextField amountNameField;
    @FXML private TextField amountQuantityField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox hasEndDateCheckBox;
    @FXML private ComboBox<Frequency> intervalComboBox;
    @FXML private ComboBox<String> typeComboBox;
    private FinancialPlannerData financialPlannerData;
    private final String INCOME = "Einnahmen";
    private final String EXPENSES = "Ausgaben";

    private BooleanBinding inputsFull ;

    @FXML
    public void initialize() {
        setupTypeComboBox();
        listenerValidInput();
        setupDialogButtons();
    }

    private void listenerValidInput() {
        amountQuantityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                ObservableValue<? extends String> observable,
                String oldValue, String newValue) {
                if (!newValue.matches("\\d+(\\.\\d{2})?|\\d+(,\\d{2})?")) {
                    String sanitized = newValue.replaceAll("[^\\d.,]", "");
                    amountQuantityField.setText(sanitized);
                }
            }
        });
    }


    public ItemChooserDialogController () {
    }

    private void setupTypeComboBox() {
        typeComboBox.getItems().addAll(INCOME, EXPENSES);
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
        intervalComboBox.valueProperty().addListener((obs, oldItem, newItem) -> updateForm(newItem));

        hasEndDateCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            endDatePicker.setDisable(!newValue);
        });
    }

    private FinancialItem setupDialogButtons() {
        /*
        dialogPane.lookupButton(ButtonType.OK).addEventHandler(e ->
        );
        */
        return null;
    }

    private Dialog<FinancialItem> getItemDialog() {
        Dialog<FinancialItem> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Erstellen Sie ein neues FinancialItem-Objekt aus den eingegebenen Werten
                System.out.println("ok");
                return null;
            } else if (buttonType == ButtonType.CANCEL) {
                System.out.println("zuemache");
                return null;
            } else {
                // Behandeln Sie andere Button-Typen
                return null;
            }
        });
        return dialog;
    }


    private void updateForm(Frequency frequency) {
        if (frequency == Frequency.ONETIME) {
            hasEndDateCheckBox.setDisable(true);
            endDatePicker.setDisable(true);
        } else {
            hasEndDateCheckBox.setDisable(false);
            endDatePicker.setDisable(false);
        }
    }

    private void handleSaveAction() { // Call this method when the save button is clicked
        String name = amountNameField.getText();
        BigDecimal amount = new BigDecimal(amountQuantityField.getText());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Frequency frequency = intervalComboBox.getValue();

        FinancialItem item = switch (typeComboBox.getValue()) {
            case INCOME -> new Expense(name, amount, frequency, startDate, endDate);
            case EXPENSES -> new Income(name, amount, frequency, startDate, endDate);
            default -> null;
        };

        if (item != null) {
            financialPlannerData.addFinancialItem(item);
        } else {
            new Alert(Alert.AlertType.ERROR, "Konnte nicht hergestellt werden");
        }
    }


    public void showAddIncomeDialog(FinancialPlannerData financialPlannerData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/financialplanner/ItemChooserDialog.fxml"));
            Parent root = fxmlLoader.load();

            Dialog dialog = new Dialog();
            dialog.setDialogPane((DialogPane) root);

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(
                new Image(this.getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/icons/money.png").toString()));
            stage.setTitle("Erstellen Sie einen neuen Eintrag");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
                    handleSaveAction();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setFinancialPlannerData(FinancialPlannerData financialPlannerData) {
        this.financialPlannerData = financialPlannerData;
    }

    public void hasEndDateCheckBoxStatus(ActionEvent actionEvent) {
        endDatePicker.setDisable(hasEndDateCheckBox.isSelected());
    }
}
