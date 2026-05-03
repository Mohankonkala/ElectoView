package za.ac.cput.patterns.abstractfactory;

public class BudgetAlertPanel implements AlertPanel {

    @Override
    public String render() {
        return "Rendering budget alert panel — 80% and 100% warnings";
    }

    @Override
    public int getMaxAlerts() {
        return 5;
    }
}