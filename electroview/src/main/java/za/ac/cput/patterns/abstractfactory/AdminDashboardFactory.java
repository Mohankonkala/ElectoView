package za.ac.cput.patterns.abstractfactory;

public class AdminDashboardFactory implements DashboardComponentFactory {

    @Override
    public ConsumptionChart createConsumptionChart() {
        return new ZoneOverviewChart();
    }

    @Override
    public AlertPanel createAlertPanel() {
        return new AnomalyAlertPanel();
    }
}