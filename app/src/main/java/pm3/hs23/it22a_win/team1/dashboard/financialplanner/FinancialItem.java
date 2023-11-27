package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * The {@code FinancialItem} class is an abstract class that represents a
 * financial item.
 * It contains the name, amount, frequency, start date and end date of the
 * financial item.
 *
 * @author Besart Morina
 * @version 24.11.2023
 */
public abstract class FinancialItem {

    private String name;
    private BigDecimal amount;
    private Frequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;

    private static final int YEARS_TO_ADD = 5;
    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 30;
    private static final int DAYS_IN_YEAR = 365;

    /**
     * Constructs a new {@code FinancialItem} object with the
     * specified name, amount, frequency, start date and end date.
     *
     * @param name      the name of the financial item
     * @param amount    the amount of the financial item
     * @param frequency the frequency of the financial item
     * @param startDate the start date of the financial item
     * @param endDate   the end date of the financial item
     */
    public FinancialItem(String name, BigDecimal amount, Frequency frequency, LocalDate startDate, LocalDate endDate) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        Objects.requireNonNull(amount);
        Objects.requireNonNull(frequency);
        Objects.requireNonNull(startDate);

        this.name = name;
        this.amount = amount;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public abstract BigDecimal calculateAmountForMonth(int year, int month);

    /**
     * Returns the category of the financial item.
     *
     * @return the category of the financial item
     */
    public String getCategory() {
        return "Studentenleben";
    }

    /**
     * Calculates the amount of the financial to be paid in the specified month.
     *
     * @param frequency the frequency
     * @return the amount of the financial item for the specified frequency
     */
    public BigDecimal getAmountInFreq(Frequency frequency) {
        Objects.requireNonNull(frequency);

        long totalEndDays;
        if (endDate != null) {
            totalEndDays = ChronoUnit.DAYS.between(startDate, endDate);
        } else {
            totalEndDays = ChronoUnit.DAYS.between(startDate, startDate.plusYears(YEARS_TO_ADD));
        }

        return switch (frequency) {
            case DAILY -> amount.multiply(BigDecimal.valueOf(totalEndDays));
            case WEEKLY -> amount.multiply(BigDecimal.valueOf(totalEndDays / DAYS_IN_WEEK));
            case MONTHLY -> amount.multiply(BigDecimal.valueOf(totalEndDays / DAYS_IN_MONTH));
            case ANNUALLY -> amount.multiply(BigDecimal.valueOf(totalEndDays / DAYS_IN_YEAR));
            case ONETIME -> amount;
            default -> throw new IllegalArgumentException("Unknown frequency: " + frequency);
        };
    }

    /**
     * Enumeration for frequency types with a description and the number of days.
     */
    public enum Frequency {
        ONETIME("Einmalig", 0),
        DAILY("Täglich", 1),
        WEEKLY("Wöchentlich", DAYS_IN_WEEK),
        MONTHLY("Monatlich", DAYS_IN_MONTH),
        ANNUALLY("Jährlich", DAYS_IN_YEAR);

        private final String description;
        private final int days;

        Frequency(String description, int days) {
            this.description = description;
            this.days = days;
        }

        public String getDescription() {
            return description;
        }

        public int getDays() {
            return days;
        }
    }

}
