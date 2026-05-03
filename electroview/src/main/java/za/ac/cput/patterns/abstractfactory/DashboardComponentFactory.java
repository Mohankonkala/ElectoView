package za.ac.cput.patterns.abstractfactory;

public interface DashboardComponentFactory {
    ConsumptionChart createConsumptionChart();
    AlertPanel createAlertPanel();
}