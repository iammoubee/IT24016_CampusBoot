package campusboot;

import java.lang.reflect.*;

public class Router {
    public static String dispatch(Container c, String path) {
        for (Object bean : c.getAllBeans().values()) {
            for (Method m : bean.getClass().getDeclaredMethods()) {
                Route r = m.getAnnotation(Route.class);
                if (r != null && r.value().equals(path)) {
                    try {
                        m.setAccessible(true);
                        return (String) m.invoke(bean);
                    } catch (Exception e) {
                        return "Error: " + e.getMessage();
                    }
                }
            }
        }
        return "404 Not Found: " + path;
    }
}
