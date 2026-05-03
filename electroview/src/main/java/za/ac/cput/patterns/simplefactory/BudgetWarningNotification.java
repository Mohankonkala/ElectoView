package za.ac.cput.patterns.simplefactory;

public class BudgetWarningNotification extends Notification {

    public BudgetWarningNotification(String recipientId, String referenceId) {
        super(recipientId, referenceId);
    }

    @Override
    public String getMessage() {
        return "You have reached 80% of your monthly kWh budget.";
    }

    @Override
    public String getType() {
        return "BUDGET_WARNING";
    }
}