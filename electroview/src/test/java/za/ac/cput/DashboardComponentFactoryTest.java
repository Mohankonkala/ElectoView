package za.ac.cput;

import org.junit.jupiter.api.Test;
import za.ac.cput.patterns.abstractfactory.AdminDashboardFactory;
import za.ac.cput.patterns.abstractfactory.AlertPanel;
import za.ac.cput.patterns.abstractfactory.ConsumerDashboardFactory;
import za.ac.cput.patterns.abstractfactory.ConsumptionChart;
import za.ac.cput.patterns.abstractfactory.DashboardComponentFactory;

import static org.junit.jupiter.api.Assertions.*;

class DashboardComponentFactoryTest {

    @Test
    void adminFactory_CreatesZoneOverviewChart() {
        DashboardComponentFactory factory = new AdminDashboardFactory();
        ConsumptionChart chart = factory.createConsumptionChart();

        assertEquals("ZONE_OVERVIEW_LINE_CHART", chart.getChartType());
    }

    @Test
    void adminFactory_ChartRendersSuccessfully() {
        DashboardComponentFactory factory = new AdminDashboardFactory();
        ConsumptionChart chart = factory.createConsumptionChart();

        assertNotNull(chart.render());
        assertFalse(chart.render().isBlank());
    }

    @Test
    void adminFactory_AlertPanelHasHighCapacity() {
        DashboardComponentFactory factory = new AdminDashboardFactory();
        AlertPanel panel = factory.createAlertPanel();

        assertEquals(50, panel.getMaxAlerts());
    }

    @Test
    void consumerFactory_CreatesPersonalUsageChart() {
        DashboardComponentFactory factory = new ConsumerDashboardFactory();
        ConsumptionChart chart = factory.createConsumptionChart();

        assertEquals("PERSONAL_USAGE_BAR_CHART", chart.getChartType());
    }

    @Test
    void consumerFactory_AlertPanelHasLowCapacity() {
        DashboardComponentFactory factory = new ConsumerDashboardFactory();
        AlertPanel panel = factory.createAlertPanel();

        assertEquals(5, panel.getMaxAlerts());
    }

    @Test
    void consumerFactory_AlertPanelMentionsBudget() {
        DashboardComponentFactory factory = new ConsumerDashboardFactory();
        AlertPanel panel = factory.createAlertPanel();

        assertTrue(panel.render().toLowerCase().contains("budget"));
    }

    @Test
    void adminAndConsumer_ProduceDifferentChartTypes() {
        DashboardComponentFactory admin    = new AdminDashboardFactory();
        DashboardComponentFactory consumer = new ConsumerDashboardFactory();

        assertNotEquals(
                admin.createConsumptionChart().getChartType(),
                consumer.createConsumptionChart().getChartType()
        );
    }
}