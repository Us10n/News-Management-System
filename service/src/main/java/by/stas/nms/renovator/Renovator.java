package by.stas.nms.renovator;

public interface Renovator<T> {
    void updateObject(T newObject, T oldObject);
}
