package pm3.hs23.it22a_win.team1.dashboard.financialplanner;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class FinancialItem {

    private String name;
    private BigDecimal amount;
    private Frequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;

    public FinancialItem(String name, BigDecimal amount, Frequency frequency, LocalDate startDate, LocalDate endDate) {
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

    public enum Frequency {
        ONETIME("Einmalig"),
        DAILY("Täglich"),
        WEEKLY("Wöchentlich"),
        MONTHLY("Monatlich"),
        ANNUALLY("Jährlich");

        private final String description;

        Frequency(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}
