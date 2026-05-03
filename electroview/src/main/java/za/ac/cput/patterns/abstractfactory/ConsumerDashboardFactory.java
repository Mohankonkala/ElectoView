package za.ac.cput.patterns.abstractfactory;

public class ConsumerDashboardFactory implements DashboardComponentFactory {

    @Override
    public ConsumptionChart createConsumptionChart() {
        return new PersonalUsageChart();
    }

    @Override
    public AlertPanel createAlertPanel() {
        return new BudgetAlertPanel();
    }
}