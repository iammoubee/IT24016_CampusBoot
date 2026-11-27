package campusboot;

import java.lang.reflect.*;
import java.util.*;

public class Validator {
    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();
        for (Field f : obj.getClass().getDeclaredFields()) {
            Range r = f.getAnnotation(Range.class);
            if (r == null) continue;
            try {
                f.setAccessible(true);
                int val = f.getInt(obj);
                if (val < r.min() || val > r.max())
                    errors.add(f.getName() + " - " + r.message() + " (actual: " + val + ")");
            } catch (Exception e) {
                errors.add(f.getName() + " - could not read field");
            }
        }
        return errors;
    }
}
