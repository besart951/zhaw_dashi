package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class Expense extends FinancialItem {

    public Expense(String name, BigDecimal amount, Frequency frequency, LocalDate startDate, LocalDate endDate) {
        super(name, amount, frequency, startDate, endDate);
    }

    @Override
    public BigDecimal calculateAmountForMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        if (startOfMonth.isBefore(getEndDate()) && (endOfMonth.isAfter(getStartDate()) || endOfMonth.isEqual(getStartDate()))) {
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

    private BigDecimal calculateDailyExpense(LocalDate startOfMonth, LocalDate endOfMonth) {
        long daysInMonth = ChronoUnit.DAYS.between(startOfMonth, endOfMonth) + 1; // +1 to include the last day
        BigDecimal dailyExpense = getAmount();
        return dailyExpense.multiply(BigDecimal.valueOf(daysInMonth));
    }

    private BigDecimal calculatePeriodicExpense(LocalDate startOfMonth, LocalDate endOfMonth, Frequency frequency) {
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateComplexExpense(YearMonth yearMonth) {
        return BigDecimal.ZERO;
    }
}

