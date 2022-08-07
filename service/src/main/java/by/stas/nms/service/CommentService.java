package by.stas.nms.service;

import by.stas.nms.dto.CommentDto;

import java.util.List;

/**
 * The interface {@code CommentService} adds new methods to CRUD.
 *
 * @see CRUDService
 */
public interface CommentService extends CRUDService<CommentDto, String> {
    /**
     * Read all objects with pageable.
     *
     * @param page  the page
     * @param limit the limit
     * @return the list of objects matching params
     */
    List<CommentDto> readAll(Integer page, Integer limit);

    /**
     * Read all objects using Fulltext search with pageable.
     *
     * @param term  the term form fulltext search
     * @param page  the page
     * @param limit the limit
     * @return the list of objects matching params.
     */
    List<CommentDto> readAll(String term,Integer page, Integer limit);
}
