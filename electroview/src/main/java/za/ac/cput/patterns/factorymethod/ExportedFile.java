package za.ac.cput.patterns.factorymethod;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExportedFile {

    private final String fileName;
    private final String content;
    private final String mimeType;

    public ExportedFile(String fileName, String content, String mimeType) {
        this.fileName = fileName;
        this.content = content;
        this.mimeType = mimeType;
    }
}