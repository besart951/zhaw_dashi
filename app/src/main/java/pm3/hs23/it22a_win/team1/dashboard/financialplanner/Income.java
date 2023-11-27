package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * The {@code Income} class represents an income.
 * It contains a name, an amount, a frequency, a start date and an end date.
 *
 * @author Besart Morina
 * @version 24.11.2023
 */
public class Income extends FinancialItem {

    public Income(String name, BigDecimal amount, Frequency frequency, LocalDate startDate, LocalDate endDate) {
        super(name, amount, frequency, startDate, endDate);
    }

    /**
     * Calculates the total income amount for a specified month.
     *
     * @param year  the year of the month
     * @param month the month
     * @return the total income for the month
     */
    @Override
    public BigDecimal calculateAmountForMonth(int year, int month) {
        if (year < 0) {
            throw new IllegalArgumentException("Year cannot be negative");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        if (startOfMonth.isBefore(getEndDate())
            && (endOfMonth.isAfter(getStartDate()) || endOfMonth.isEqual(getStartDate()))) {
            switch (getFrequency()) {
                case DAILY:
                    return calculateDailyIncome(startOfMonth, endOfMonth);
                case WEEKLY:
                    // Add your code here for WEEKLY frequency
                    break;
                case MONTHLY:
                    // Add your code here for MONTHLY frequency
                    break;
                case ANNUALLY:
                    // Add your code here for ANNUALLY frequency
                    break;
                case ONETIME:
                    // Add your code here for ONETIME frequency
                    break;
            }
        }

        return BigDecimal.ZERO;
    }

    /**
     * Calculates the total income amount for a day.
     *
     * @param startOfMonth the first day of the month
     * @param endOfMonth   the last day of the month
     * @return the total income a day
     */
    private BigDecimal calculateDailyIncome(LocalDate startOfMonth, LocalDate endOfMonth) {
        Objects.requireNonNull(startOfMonth);
        Objects.requireNonNull(endOfMonth);

        long daysInMonth = ChronoUnit.DAYS.between(startOfMonth, endOfMonth) + 1;
        BigDecimal dailyIncome = getAmount();
        return dailyIncome.multiply(BigDecimal.valueOf(daysInMonth));
    }
}
