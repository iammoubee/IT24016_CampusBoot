package campusboot;

import java.lang.reflect.*;
import java.util.*;

public class Container {
    private final Map<String, Object> beans = new LinkedHashMap<>();

    public void scan(Class<?>... classes) {
        for (Class<?> c : classes) {
            Component comp = c.getAnnotation(Component.class);
            if (comp == null) {
                System.out.println("[Container] Skipped: " + c.getSimpleName() + " (no @Component)");
                continue;
            }
            try {
                Object instance = c.getDeclaredConstructor().newInstance();
                String name = comp.value();
                if (name.isEmpty()) {
                    name = c.getSimpleName();
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                }
                beans.put(name, instance);
                System.out.println("[Container] Registered: " + name + " (" + c.getSimpleName() + ")");
            } catch (Exception e) {
                System.out.println("[Container] Error creating " + c.getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    public void injectValues(Map<String, String> config) {
        for (Object bean : beans.values()) {
            for (Field f : bean.getClass().getDeclaredFields()) {
                Value v = f.getAnnotation(Value.class);
                if (v == null) continue;
                String key = v.value();
                if (!config.containsKey(key)) continue;
                String raw = config.get(key);
                try {
                    f.setAccessible(true);
                    if (f.getType() == int.class) f.setInt(bean, Integer.parseInt(raw));
                    else if (f.getType() == String.class) f.set(bean, raw);
                    System.out.println("[Container] Injected: " + key + " -> " + bean.getClass().getSimpleName() + "." + f.getName());
                } catch (Exception e) {
                    System.out.println("[Container] Injection failed for " + key + ": " + e.getMessage());
                }
            }
        }
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    public Map<String, Object> getAllBeans() {
        return beans;
    }
}
