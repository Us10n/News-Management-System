package by.stas.nms.renovator;

/**
 * The interface {@code Renovator} provides method to update old object with new values.
 *
 * @param <T> the class type
 */
public interface Renovator<T> {
    /**
     * Update object.
     *
     * @param newObject the new object
     * @param oldObject the old object
     */
    void updateObject(T newObject, T oldObject);
}
