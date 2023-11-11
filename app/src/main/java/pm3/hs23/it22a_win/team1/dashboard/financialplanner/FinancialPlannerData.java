package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialItem.Frequency;

public class FinancialPlannerData {
    private List<FinancialItem> financialItems;
    private BigDecimal initialBalance;
    private final String EXPENSE = "Expense";
    private final String INCOME = "Income";
    private final String ONETIMETRANSACTION = "OneTimeTransaction";

    public FinancialPlannerData() {
        this.financialItems = new ArrayList<>();
        this.initialBalance = BigDecimal.ZERO;
    }

    public BigDecimal calculateBalanceForMonth(int year, int month) {
        BigDecimal balance = initialBalance;

        for (FinancialItem item : financialItems) {
            BigDecimal itemAmountForMonth = item.calculateAmountForMonth(year, month);
            if (item instanceof Expense) {
                balance = balance.subtract(itemAmountForMonth);
            } else if (item instanceof Income ) {
                balance = balance.add(itemAmountForMonth);
            }
        }
        return balance;
    }

    public void addFinancialItem(FinancialItem item) {
        financialItems.add(item);
    }

    public List<FinancialItem> getFinancialItems() {
        return financialItems;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public List<FinancialItem> getListOfType(Class<? extends FinancialItem> type) {
        return financialItems.stream()
            .filter(type::isInstance)
            .map(type::cast)
            .collect(Collectors.toList());
    }

    public void removeFinancialItem(FinancialItem selectedItem) {
    }

    public FinancialItem getListOfIncome() {

        return null;
    }

    public FinancialItem getListOfExpense() {
        return null;
    }

    public FinancialItem getListOfTransaction() {
        return null;
    }
}
