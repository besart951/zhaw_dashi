package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.util.StringConverter;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialItem.Frequency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code FinancialPlannerController} class serves as a controller for the
 * financial planner feature.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
public class FinancialPlannerController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<Frequency> intervalComboBox;
    @FXML
    private TextField initialBalanceField;
    @FXML
    private TableView<FinancialItem> expenseTableView;
    @FXML
    private TableView<FinancialItem> incomeTableView;
    @FXML
    private TableView<FinancialItem> transactionTableView;
    @FXML
    private PieChart expensesPieChart;
    @FXML
    private LineChart<String, Number> balanceLineChart;
    private FinancialPlannerData financialPlannerData;

    /**
     * Sets the model for the financial planner feature.
     *
     * @param financialPlannerData the model for the financial planner feature
     */
    public void setModel(FinancialPlannerData financialPlannerData) {
        this.financialPlannerData = financialPlannerData;
        initialBalanceField.setText(financialPlannerData.getInitialBalance().toString());

        setupIntervalComboBox();
        setupDatePicker();
        setupUpdateItemTables();

        setupFinancialItemTable(incomeTableView);
        setupFinancialItemTable(expenseTableView);
        setupFinancialItemTable(transactionTableView);
    }

    /**
     * Sets up the listeners for the financial item tables.
     * The tables are updated whenever a new financial item is added or removed.
     */
    private void setupUpdateItemTables() {
        updateFinancialItemTables();
        financialPlannerData.getFinancialItems().addListener((ListChangeListener<FinancialItem>) c -> {
            while (c.next()) {
                if (c.wasAdded() || c.wasRemoved()) {
                    updateFinancialItemTables();
                    updateCharts();
                }
            }
        });
    }

    /**
     * Configures the interval ComboBox to display all possible intervals.
     */
    private void setupIntervalComboBox() {
        intervalComboBox.getItems().addAll(Frequency.values());
        intervalComboBox.getItems().remove(Frequency.ONETIME);
        intervalComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Frequency frequency) {
                return frequency == null ? null : frequency.getDescription();
            }

            @Override
            public Frequency fromString(String string) {
                return intervalComboBox.getItems().stream()
                    .filter(item -> item.getDescription().equals(string))
                    .findFirst()
                    .orElse(null);
            }
        });
        intervalComboBox.setValue(Frequency.MONTHLY);
        intervalComboBox.valueProperty().addListener((obs, oldItem, newItem) -> updateCharts());
    }

    /**
     * Configures the start and end date pickers to be constrained by each other.
     * The start date picker is initialized to the current date, while the end date picker
     */
    private void setupDatePicker() {
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && endDatePicker.getValue() != null && newDate.isAfter(endDatePicker.getValue())) {
                endDatePicker.setValue(newDate.plusDays(1));
            } else if (newDate != null && endDatePicker.getValue() != null
                && newDate.isBefore(startDatePicker.getValue())) {
                startDatePicker.setValue(newDate.minusDays(1));
            }
        });
        startDatePicker.setValue(LocalDate.now());

        endDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && startDatePicker.getValue() != null && newDate.isBefore(startDatePicker.getValue())) {
                startDatePicker.setValue(newDate.minusDays(1));
            } else if (newDate != null && startDatePicker.getValue() != null
                && newDate.isAfter(endDatePicker.getValue())) {
                endDatePicker.setValue(newDate.plusDays(1));
            }
        });
        endDatePicker.setValue(LocalDate.now().plusDays(30));
    }

    /**
     * Updates the financial item tables by repopulating them with the current data.
     * This method also triggers an update of the charts to reflect the latest financial data.
     */
    public void updateFinancialItemTables() {
        incomeTableView.getItems().clear();
        expenseTableView.getItems().clear();
        transactionTableView.getItems().clear();
        incomeTableView.setItems(FXCollections.observableArrayList(financialPlannerData.getListOfIncome()));
        expenseTableView.setItems(FXCollections.observableArrayList(financialPlannerData.getListOfExpense()));
        transactionTableView.setItems(FXCollections.observableArrayList(financialPlannerData.getListOfTransaction()));

        updateCharts();
    }

    /**
     * Updates the charts to reflect the current financial data.
     */
    private void updateCharts() {
        updatePieChart();
        updateLineChart();
    }

    /**
     * Updates the pie chart to reflect the current financial data.
     * The pie chart is updated based on the selected interval, start date and end date.
     */
    private void updatePieChart() {
        Map<String, BigDecimal> expenseCategories = new HashMap<>();
        for (FinancialItem item : financialPlannerData.getListOfExpense()) {
            expenseCategories.merge(item.getCategory(), item.getAmount(), BigDecimal::add);
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        expenseCategories.forEach((category, amount) -> {
            pieChartData.add(new PieChart.Data(category, amount.doubleValue()));
        });

        expensesPieChart.setData(pieChartData);
    }

    /**
     * Updates the line chart to reflect the current financial data.
     * The line chart is updated based on the selected interval, start date and end date.
     */
    private void updateLineChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        //series.setName("Balance im Laufe der Zeit");

        List<BigDecimal> balances = financialPlannerData.calculateBalanceForTimeline(
            intervalComboBox.getValue(), startDatePicker.getValue(), endDatePicker.getValue());

        LocalDate date = startDatePicker.getValue();
        for (BigDecimal balance : balances) {
            series.getData().add(new XYChart.Data<>(date.toString(), balance));
            date = date.plusDays(intervalComboBox.getValue().getDays());
        }

        balanceLineChart.getData().clear();
        balanceLineChart.getData().add(series);
        balanceLineChart.getXAxis().setAutoRanging(true);
        balanceLineChart.getYAxis().setAutoRanging(true);
        balanceLineChart.setLegendVisible(false);
    }

    /**
     * Configures a given TableView to display financial items with appropriate
     * columns.
     * Also configures the table to use a constrained resize policy and to allow
     * multiple selections.
     * The table is initialized to be non-editable and sorted by the start date.
     *
     * @param tableView The TableView instance to be configured for displaying
     *                  FinancialItem objects.
     */
    public void setupFinancialItemTable(TableView<FinancialItem> tableView) {
        TableColumn<FinancialItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<FinancialItem, BigDecimal> amountColumn = new TableColumn<>("Menge");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<FinancialItem, String> frequencyColumn = new TableColumn<>("Frequenz");
        frequencyColumn.setCellValueFactory(
            cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFrequency().getDescription()));

        TableColumn<FinancialItem, LocalDate> startDateColumn = new TableColumn<>("Von");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<FinancialItem, LocalDate> endDateColumn = new TableColumn<>("Bis");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(amountColumn);
        tableView.getColumns().add(frequencyColumn);
        tableView.getColumns().add(startDateColumn);
        tableView.getColumns().add(endDateColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Handles the action of adding income through the UI.
     * Opens a dialog to input new income details and adds them to the financial data.
     */
    @FXML
    private void addIncomeButton() {
        ItemChooserDialogController financialItem = new ItemChooserDialogController();
        financialItem.showAddIncomeDialog(financialPlannerData);
    }


    /**
     * Removes the selected items from the financial data.
     */
    @FXML
    private void removeSelectedItem() {
        List<FinancialItem> selectedItems = new ArrayList<>();

        if (!incomeTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            selectedItems.addAll(incomeTableView.getSelectionModel().getSelectedItems());
        }
        if (!expenseTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            selectedItems.addAll(expenseTableView.getSelectionModel().getSelectedItems());
        }
        if (!transactionTableView.getSelectionModel().getSelectedItems().isEmpty()) {
            selectedItems.addAll(transactionTableView.getSelectionModel().getSelectedItems());
        }

        if (!selectedItems.isEmpty()) {
            financialPlannerData.removeAllFinancialItems(selectedItems);
        }
    }

    /**
     * Handles updating the balance based on user input.
     * Parses the new balance from the input field and updates the charts accordingly.
     */
    @FXML
    private void updateBalanceButton() {
        BigDecimal newBalance = new BigDecimal(initialBalanceField.getText());
        financialPlannerData.setInitialBalance(newBalance);
        updateCharts();
    }
}
