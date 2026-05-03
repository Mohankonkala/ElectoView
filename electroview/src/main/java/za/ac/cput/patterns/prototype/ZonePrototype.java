package za.ac.cput.patterns.prototype;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ZonePrototype implements Cloneable {

    private String zoneId;
    private String name;
    private String location;
    private double capacityKwh;
    private String thresholdType;
    private double thresholdValue;
    private String status;

    public ZonePrototype(String name, String location,
                         double capacityKwh,
                         String thresholdType,
                         double thresholdValue) {
        this.zoneId        = UUID.randomUUID().toString();
        this.name          = name;
        this.location      = location;
        this.capacityKwh   = capacityKwh;
        this.thresholdType = thresholdType;
        this.thresholdValue = thresholdValue;
        this.status        = "CONFIGURED";
    }

    @Override
    public ZonePrototype clone() {
        try {
            ZonePrototype cloned = (ZonePrototype) super.clone();
            cloned.zoneId = UUID.randomUUID().toString();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }
}