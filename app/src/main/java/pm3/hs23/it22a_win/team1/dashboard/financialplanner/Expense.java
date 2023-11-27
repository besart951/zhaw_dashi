package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

/**
 * The {@code Expense} class represents an expense in the financial planner.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
public class Expense extends FinancialItem {
    private static final int DAYS_INCLUSIVE_OFFSET = 1;

    public Expense(String name, BigDecimal amount, Frequency frequency, LocalDate startDate, LocalDate endDate) {
        super(name, amount, frequency, startDate, endDate);
    }

    /**
     * Calculates the total expense amount for a specified month.
     *
     * @param year  the year of the month
     * @param month the month
     * @return the total expense for the month
     */
    @Override
    public BigDecimal calculateAmountForMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        if (startOfMonth.isBefore(getEndDate())
            && (endOfMonth.isAfter(getStartDate()) || endOfMonth.isEqual(getStartDate()))) {
            switch (getFrequency()) {
                case DAILY:
                    return calculateDailyExpense(startOfMonth, endOfMonth);
                case WEEKLY:
                default:
                    return BigDecimal.ZERO;
            }
        }

        // If the expense period doesn't overlap with the specified month, return 0
        return BigDecimal.ZERO;
    }

    /**
     * Calculates the total expense amount for a day.
     *
     * @param startOfMonth the first day of the month
     * @param endOfMonth   the last day of the month
     * @return the total expense a day
     */
    private BigDecimal calculateDailyExpense(LocalDate startOfMonth, LocalDate endOfMonth) {
        long daysInMonth = ChronoUnit.DAYS.between(startOfMonth, endOfMonth) + DAYS_INCLUSIVE_OFFSET;
        BigDecimal dailyExpense = getAmount();
        return dailyExpense.multiply(BigDecimal.valueOf(daysInMonth));
    }
}
