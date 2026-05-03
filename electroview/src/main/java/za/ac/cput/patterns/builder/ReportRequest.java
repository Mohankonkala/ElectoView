package za.ac.cput.patterns.builder;

import lombok.Getter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@ToString
public class ReportRequest {

    private final String requestedBy;   
    private final LocalDate startDate; 
    private final LocalDate endDate;    
    private final String zoneId;        
    private final String meterId;       
    private final String format;        
    private final boolean background;   

    private ReportRequest(Builder builder) {
        this.requestedBy = builder.requestedBy;
        this.startDate   = builder.startDate;
        this.endDate     = builder.endDate;
        this.zoneId      = builder.zoneId;
        this.meterId     = builder.meterId;
        this.format      = builder.format;
        this.background  = builder.background;
    }

    public static class Builder {

        private final String requestedBy;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private String zoneId    = null;
        private String meterId   = null;
        private String format    = "CSV";
        private boolean background = false;

        public Builder(String requestedBy,
                       LocalDate startDate,
                       LocalDate endDate) {
            if (requestedBy == null || requestedBy.isBlank())
                throw new IllegalArgumentException("requestedBy is required.");
            if (startDate == null || endDate == null)
                throw new IllegalArgumentException("Date range is required.");
            if (startDate.isAfter(endDate))
                throw new IllegalArgumentException(
                        "startDate cannot be after endDate.");
            this.requestedBy = requestedBy;
            this.startDate   = startDate;
            this.endDate     = endDate;
        }

        public Builder zoneId(String zoneId) {
            this.zoneId = zoneId;
            return this;
        }

        public Builder meterId(String meterId) {
            this.meterId = meterId;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder background(boolean background) {
            this.background = background;
            return this;
        }

        public ReportRequest build() {
            return new ReportRequest(this);
        }
    }
}