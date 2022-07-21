package by.stas.nms.service;

import java.util.List;

/**
 * Interface of service that performs CRUD operations.
 *
 * @param <T> Dto class
 */
public interface CRUDService<T> {
    /**
     * Create T type object.
     *
     * @param object object to create instance of
     * @return the t
     */
    T create(T object);

    /**
     * Read all T type objects.
     *
     * @return list of existing objects
     */
    List<T> readAll(Integer page, Integer limit);

    /**
     * Read T object by id.
     *
     * @param id object id
     * @return the T type object
     */
    T readById(long id);

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
    void delete(long id);
}
