package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialItem.Frequency;

import java.math.BigDecimal;


public class FinancialPlannerController {
    private FinancialPlannerData financialPlannerData;

    @FXML private TextField initialBalanceField;
    @FXML private Button updateBalanceButton;

    @FXML private ListView<FinancialItem> expenseListView;

    @FXML private ListView<FinancialItem> incomeListView;
    @FXML public Button addIncomeButton;
    @FXML public Button removeIncomeButton;

    @FXML private ListView<FinancialItem> transactionListView;

    @FXML private PieChart expensesPieChart;
    @FXML private LineChart<?, ?> balanceLineChart;


    public void setModels(FinancialPlannerData financialPlannerData) {
        this.financialPlannerData = financialPlannerData;
        // Initialize ListView with items
        updateFinancialItemList();

        // Set initial balance in the TextField
        initialBalanceField.setText(financialPlannerData.getInitialBalance().toString());

        // Setup event handlers for buttons
        setupButtonHandlers();
    }

    private void setupButtonHandlers() {
        updateBalanceButton.setOnAction(event -> updateBalance());
        addIncomeButton.setOnAction(event -> addIncome());
        removeIncomeButton.setOnAction(event -> removeIncome());
    }

    private void updateBalance() {
        BigDecimal newBalance = new BigDecimal(initialBalanceField.getText());
        financialPlannerData.setInitialBalance(newBalance);
    }

    private void addExpense() {

    }

    private void removeExpense() {
        FinancialItem selectedItem = expenseListView.getSelectionModel().getSelectedItem();
        financialPlannerData.removeFinancialItem(selectedItem);

        updateFinancialItemList();
    }

    private void addIncome() {
        ItemChooserDialogController financialItem = new ItemChooserDialogController();
        financialItem.showAddIncomeDialog(financialPlannerData);

    }

    private void removeIncome() {
        FinancialItem selectedItem = incomeListView.getSelectionModel().getSelectedItem();
        financialPlannerData.removeFinancialItem(selectedItem);
        updateFinancialItemList();
    }

    private void addTransaction() {

    }

    private void removeTransaction() {
        FinancialItem selectedItem = transactionListView.getSelectionModel().getSelectedItem();
        financialPlannerData.removeFinancialItem(selectedItem);
        updateFinancialItemList();
    }

    public void updateFinancialItemList() {
        incomeListView.getItems().setAll(financialPlannerData.getListOfIncome());
        expenseListView.getItems().setAll(financialPlannerData.getListOfExpense());
        transactionListView.getItems().setAll(financialPlannerData.getListOfTransaction());

        updateCharts();
    }

    private void updateCharts() {
        // Implement the logic to update PieChart and LineChart with the new data
        // This could be complex as it involves transforming your data into a format suitable for the charts
    }

}
