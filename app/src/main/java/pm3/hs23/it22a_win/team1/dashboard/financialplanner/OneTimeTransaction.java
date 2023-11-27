package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a one-time financial transaction.
 *
 * @author Besart Morina
 * @version 24.11.2023
 */
public class OneTimeTransaction extends FinancialItem {

    /**
     * Constructs a one-time transaction.
     *
     * @param name            The name of the transaction.
     * @param amount          The amount of the transaction.
     * @param transactionDate The date of the transaction.
     */
    public OneTimeTransaction(String name, BigDecimal amount, LocalDate transactionDate) {
        super(name, amount, Frequency.ONETIME, transactionDate, transactionDate);
    }

    /**
     * Calculates the amount for a specific month.
     *
     * @param year  The year of the month.
     * @param month The month.
     * @return The amount if the transaction occurred in the given month and year,
     * otherwise BigDecimal.ZERO.
     */
    @Override
    public BigDecimal calculateAmountForMonth(int year, int month) {
        LocalDate transactionDate = getStartDate();

        if (transactionDate.getYear() == year && transactionDate.getMonthValue() == month) {
            return getAmount();
        }

        return BigDecimal.ZERO;
    }
}
