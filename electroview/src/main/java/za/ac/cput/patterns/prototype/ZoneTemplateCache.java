package za.ac.cput.patterns.prototype;

import java.util.HashMap;
import java.util.Map;

public class ZoneTemplateCache {

    private static final Map<String, ZonePrototype> cache = new HashMap<>();

    static {
        cache.put("RESIDENTIAL",
                new ZonePrototype("Residential Template", "",
                        5000.0, "RELATIVE", 120.0));
        cache.put("INDUSTRIAL",
                new ZonePrototype("Industrial Template", "",
                        20000.0, "ABSOLUTE", 18000.0));
        cache.put("MIXED_USE",
                new ZonePrototype("Mixed-Use Template", "",
                        10000.0, "RELATIVE", 115.0));
    }

    public static ZonePrototype getClone(String key) {
        ZonePrototype proto = cache.get(key);
        if (proto == null)
            throw new IllegalArgumentException(
                    "No template found for key: " + key);
        return proto.clone();
    }

    public static void addTemplate(String key, ZonePrototype proto) {
        cache.put(key, proto);
    }
}