package za.ac.cput;

import org.junit.jupiter.api.Test;
import za.ac.cput.patterns.factorymethod.CsvReportExporter;
import za.ac.cput.patterns.factorymethod.ExportedFile;
import za.ac.cput.patterns.factorymethod.PdfReportExporter;
import za.ac.cput.patterns.factorymethod.ReportExporter;

import static org.junit.jupiter.api.Assertions.*;

class ReportExporterTest {

    @Test
    void pdfExporter_ProducesPdfMimeType() {
        ReportExporter exporter = new PdfReportExporter();
        ExportedFile file = exporter.export("rpt-001", "meter,zone\nM1,ZoneA");

        assertEquals("application/pdf", file.getMimeType());
    }

    @Test
    void pdfExporter_FileNameEndsWith_pdf() {
        ReportExporter exporter = new PdfReportExporter();
        ExportedFile file = exporter.export("rpt-001", "some data");

        assertTrue(file.getFileName().endsWith(".pdf"));
    }

    @Test
    void pdfExporter_ContentContainsReportId() {
        ReportExporter exporter = new PdfReportExporter();
        ExportedFile file = exporter.export("rpt-001", "some data");

        assertTrue(file.getContent().contains("rpt-001"));
    }

    @Test
    void csvExporter_ProducesCsvMimeType() {
        ReportExporter exporter = new CsvReportExporter();
        ExportedFile file = exporter.export("rpt-002", "M1,ZoneA,2026-01-01,120.5,false");

        assertEquals("text/csv", file.getMimeType());
    }

    @Test
    void csvExporter_FileNameEndsWith_csv() {
        ReportExporter exporter = new CsvReportExporter();
        ExportedFile file = exporter.export("rpt-002", "some data");

        assertTrue(file.getFileName().endsWith(".csv"));
    }

    @Test
    void csvExporter_ContentContainsBillingHeader() {
        ReportExporter exporter = new CsvReportExporter();
        ExportedFile file = exporter.export("rpt-002", "row1");

        assertTrue(file.getContent().contains(
                "meter_id,zone,date,kwh_consumed,anomaly_flag"));
    }

    @Test
    void export_EmptyData_ThrowsException() {
        ReportExporter exporter = new PdfReportExporter();

        assertThrows(IllegalArgumentException.class, () ->
                exporter.export("rpt-x", ""));
    }

    @Test
    void export_NullData_ThrowsException() {
        ReportExporter exporter = new CsvReportExporter();

        assertThrows(IllegalArgumentException.class, () ->
                exporter.export("rpt-x", null));
    }
}