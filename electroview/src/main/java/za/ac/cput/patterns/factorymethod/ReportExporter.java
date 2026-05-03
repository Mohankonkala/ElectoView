package za.ac.cput.patterns.factorymethod;

public abstract class ReportExporter {

    // Template method — defines the workflow
    public final ExportedFile export(String reportId, String data) {
        validateData(data);
        String processed = processData(data);
        return createFile(reportId, processed);
    }

    // Factory Method — subclasses override this
    protected abstract ExportedFile createFile(String reportId, String data);

    private void validateData(String data) {
        if (data == null || data.isBlank())
            throw new IllegalArgumentException("Report data cannot be empty.");
    }

    protected String processData(String data) {
        return data.trim();
    }
}