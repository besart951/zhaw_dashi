package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialItem.Frequency;

import java.util.Objects;

/**
 * The {@code FinancialPlannerData} class represents the data of the financial
 * planner.
 * It contains a list of financial items and the initial balance.
 *
 * @author Besart Morina
 * @version 24.11.2023
 */
public class FinancialPlannerData {
    private final ListProperty<FinancialItem> financialItems;
    private BigDecimal initialBalance;

    public FinancialPlannerData() {
        this.financialItems = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        this.initialBalance = BigDecimal.ZERO;
        Expense expense = new Expense("Ausgabe 1", new BigDecimal("50"), Frequency.DAILY, LocalDate.now(),
            LocalDate.now().plusDays(30));
        Expense expense1 = new Expense("Ausgabe 1", new BigDecimal("45.67"), Frequency.DAILY, LocalDate.now(),
            LocalDate.now().plusDays(30));
        financialItems.add(expense1);
        financialItems.add(expense);
    }

    /**
     * Calculates the balance for a given timeline.
     *
     * @param frequency the frequency of the balance
     * @param startDate the start date of the timeline
     * @param endDate   the end date of the timeline
     * @return a list of balances for the given timeline
     */
    public List<BigDecimal> calculateBalanceForTimeline(Frequency frequency, LocalDate startDate, LocalDate endDate) {
        List<BigDecimal> balances = new ArrayList<>();
        BigDecimal balance = initialBalance;

        if (frequency == null) {
            frequency = Frequency.MONTHLY;
        }

        LocalDate date;
        if (startDate == null) {
            date = LocalDate.now();
        } else {
            date = startDate;
        }

        LocalDate dateEnd;
        if (endDate == null) {
            dateEnd = LocalDate.now().plusDays(30);
        } else {
            dateEnd = endDate;
        }

        while (!date.isAfter(dateEnd)) {
            for (FinancialItem item : financialItems) {
                if (item instanceof Expense) {
                    balance = balance.subtract(item.getAmountInFreq(frequency));
                } else if (item instanceof Income) {
                    balance = balance.add(item.getAmountInFreq(frequency));
                }
            }
            balances.add(balance);
            date = date.plusDays(1);
        }
        return balances;
    }

    /**
     * Adds a financial item to the list of financial items.
     *
     * @param item
     */
    public void addFinancialItem(FinancialItem item) {
        Objects.requireNonNull(item);
        financialItems.add(item);
    }

    public ListProperty<FinancialItem> getFinancialItems() {
        return financialItems;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    /**
     * Sets the initial balance.
     *
     * @param initialBalance the initial balance
     */
    public void setInitialBalance(BigDecimal initialBalance) {
        Objects.requireNonNull(initialBalance);

        this.initialBalance = initialBalance;
    }

    /**
     * Removes all financial item from the list of financial items.
     *
     * @param selectedItems the financial item to remove
     */
    public void removeAllFinancialItems(List<FinancialItem> selectedItems) {
        Objects.requireNonNull(selectedItems);
        financialItems.removeAll(selectedItems);
    }

    /**
     * Returns a list of all the incomes.
     *
     * @return a list of all the incomes
     */
    public List<FinancialItem> getListOfIncome() {
        ArrayList<FinancialItem> incomes = new ArrayList<>();
        for (FinancialItem item : financialItems) {
            if (item instanceof Income) {
                incomes.add(item);
            }
        }
        return incomes;
    }

    /**
     * Returns a list of all the expenses.
     *
     * @return a list of all the expenses
     */
    public List<FinancialItem> getListOfExpense() {
        ArrayList<FinancialItem> expenses = new ArrayList<>();
        for (FinancialItem item : financialItems) {
            if (item instanceof Expense) {
                expenses.add(item);
            }
        }
        return expenses;
    }

    /**
     * Returns a list of all the one time transactions.
     *
     * @return a list of all the one time transactions
     */
    public List<FinancialItem> getListOfTransaction() {
        ArrayList<FinancialItem> transactions = new ArrayList<>();
        for (FinancialItem item : financialItems) {
            if (item instanceof OneTimeTransaction) {
                transactions.add(item);
            }
        }
        return transactions;
    }
}
