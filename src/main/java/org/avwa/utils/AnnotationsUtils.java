package org.avwa.utils;

import jakarta.persistence.Entity;

public class AnnotationsUtils {

    public static String getEntityName(Class clazz) {

        Entity entityAnnotation = (Entity) clazz.getAnnotation(Entity.class);
        if (entityAnnotation != null) {
            String name = entityAnnotation.name();
            if (name != null && name.length() > 0)
                return name;
            else
                return clazz.getSimpleName();
        } else {
            return clazz.getSimpleName();
        }
    }
}
