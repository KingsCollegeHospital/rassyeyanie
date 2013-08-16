package uk.nhs.kch.rassyeyanie.framework.util;

import java.util.Collections;
import java.util.List;

/**
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // Hide constructor
    }

    /**
     * Like Collections.unmodifiableList(), but with checks for null values.
     */
    public static <T> List<T> unmodifiableList(List<T> list) {

        if (list == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(list);
    }

}
