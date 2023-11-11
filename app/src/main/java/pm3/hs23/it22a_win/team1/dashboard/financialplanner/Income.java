package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class Income extends FinancialItem {

    public Income(String name, BigDecimal amount, Frequency frequency, LocalDate startDate, LocalDate endDate) {
        super(name, amount, frequency, startDate, endDate);
    }

    @Override
    public BigDecimal calculateAmountForMonth(int year, int month) {
        // Create a YearMonth instance for the specified year and month
        YearMonth yearMonth = YearMonth.of(year, month);

        // Then, create LocalDate instances for the start and end of that month
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // Check if the income period overlaps with the specified month
        if (startOfMonth.isBefore(getEndDate()) && (endOfMonth.isAfter(getStartDate()) || endOfMonth.isEqual(getStartDate()))) {
            // If it does, calculate the amount
            switch (getFrequency()) {
                case DAILY:
                    return calculateDailyIncome(startOfMonth, endOfMonth);
                case WEEKLY:
            }
        }

        // If the income period doesn't overlap with the specified month, return 0
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateDailyIncome(LocalDate startOfMonth, LocalDate endOfMonth) {
        long daysInMonth = ChronoUnit.DAYS.between(startOfMonth, endOfMonth) + 1; // +1 to include the last day
        BigDecimal dailyIncome = getAmount();
        return dailyIncome.multiply(BigDecimal.valueOf(daysInMonth));
    }

    private BigDecimal calculatePeriodicIncome(LocalDate startOfMonth, LocalDate endOfMonth, Frequency frequency) {

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateComplexIncome(YearMonth yearMonth) {

        return BigDecimal.ZERO;
    }
}

