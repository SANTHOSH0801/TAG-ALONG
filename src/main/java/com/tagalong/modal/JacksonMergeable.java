package com.tagalong.modal;

import java.util.ArrayList;
import java.util.List;

import com.tagalong.util.JsonUtil;

/**
 * Mergeable objects implement a merge method which allows basic JSON based
 * merges of two objects of
 * the same type. Type compatibility is enforced to make sure only directly
 * related classes are
 * merged.
 */
public class JacksonMergeable extends JacksonSerializable {

    // Verifies a direct inheritance chain between the objects
    public static boolean areMergeCompatible(Object base, Object other) {
        return base.getClass().isInstance(other) || other.getClass().isInstance(base);
    }

    @SuppressWarnings("unchecked")
    public <E, T> T merge(E delta, List<String> ignoreFields) throws Exception {
        // Verify type compatibility i.e. either delta is a subclass of 'this' or 'this'
        // is a
        // subclass of delta
        if (JacksonMergeable.areMergeCompatible(this, delta)) {
            return JsonUtil.deepMerge((T) this, delta, ignoreFields);
        } else {
            throw new IncompatibleMergeException(
                    "Incompatible types being merged: " + this.getClass() + " with " + delta.getClass());
        }
    }

    public <E, T> T merge(E delta) throws Exception {
        return merge(delta, new ArrayList<>());
    }

    /**
     * Merges without throwing any exception
     *
     * @param delta
     * @param <E>
     * @param <T>
     * @return
     */
    public <E, T> T safeMerge(E delta) {
        try {
            return this.merge(delta);
        } catch (Exception e) {
            return null;
        }
    }

    // Trivial exception wrapper for merge exceptions arising out of type
    // incompatibility.
    public static class IncompatibleMergeException extends Exception {

        public IncompatibleMergeException(String message) {
            super(message);
        }
    }
}
