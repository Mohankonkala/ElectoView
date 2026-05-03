package za.ac.cput;

import org.junit.jupiter.api.Test;
import za.ac.cput.patterns.prototype.ZonePrototype;
import za.ac.cput.patterns.prototype.ZoneTemplateCache;

import static org.junit.jupiter.api.Assertions.*;

class ZonePrototypeTest {

    @Test
    void clone_HasSameConfigAsOriginal() {
        ZonePrototype proto = new ZonePrototype(
                "Bellville", "Western Cape",
                5000.0, "RELATIVE", 120.0);
        ZonePrototype clone = proto.clone();

        assertEquals(proto.getCapacityKwh(),    clone.getCapacityKwh());
        assertEquals(proto.getThresholdType(),  clone.getThresholdType());
        assertEquals(proto.getThresholdValue(), clone.getThresholdValue());
    }

    @Test
    void clone_HasDifferentId() {
        ZonePrototype proto = new ZonePrototype(
                "Bellville", "Western Cape",
                5000.0, "RELATIVE", 120.0);
        ZonePrototype clone = proto.clone();

        assertNotEquals(proto.getZoneId(), clone.getZoneId());
    }

    @Test
    void modifyingClone_DoesNotAffectOriginal() {
        ZonePrototype proto = new ZonePrototype(
                "Original", "Cape Town",
                8000.0, "ABSOLUTE", 7500.0);
        ZonePrototype clone = proto.clone();

        clone.setName("Modified Clone");
        clone.setCapacityKwh(9000.0);

        assertEquals("Original", proto.getName());
        assertEquals(8000.0, proto.getCapacityKwh());
    }

    @Test
    void cache_ResidentialTemplate_ReturnsClone() {
        ZonePrototype zone = ZoneTemplateCache.getClone("RESIDENTIAL");

        assertNotNull(zone);
        assertEquals(5000.0, zone.getCapacityKwh());
        assertEquals("RELATIVE", zone.getThresholdType());
    }

    @Test
    void cache_IndustrialTemplate_ReturnsClone() {
        ZonePrototype zone = ZoneTemplateCache.getClone("INDUSTRIAL");

        assertNotNull(zone);
        assertEquals(20000.0, zone.getCapacityKwh());
        assertEquals("ABSOLUTE", zone.getThresholdType());
    }

    @Test
    void cache_TwoClonesFromSameTemplate_HaveDifferentIds() {
        ZonePrototype a = ZoneTemplateCache.getClone("RESIDENTIAL");
        ZonePrototype b = ZoneTemplateCache.getClone("RESIDENTIAL");

        assertNotEquals(a.getZoneId(), b.getZoneId());
    }

    @Test
    void cache_UnknownKey_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                ZoneTemplateCache.getClone("UNKNOWN_TEMPLATE")
        );
    }
}