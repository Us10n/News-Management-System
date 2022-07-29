package by.stas.nms.service;

/**
 * Interface of service that performs CRUD operations.
 *
 * @param <T> Dto class
 * @param <R> id type
 */
public interface CRUDService<T,R> {
    /**
     * Create T type object.
     *
     * @param object object to create instance of
     * @return the t
     */
    T create(T object);

    /**
     * Read T object by id.
     *
     * @param id object id
     * @return the T type object
     */
    T readById(R id);

    /**
     * Update T type object.
     *
     * @param object object with new values
     * @return instance of updated object
     */
    T update(T object);

    /**
     * Delete T object specified by id.
     *
     * @param id object id
     */
    void delete(R id);
}
