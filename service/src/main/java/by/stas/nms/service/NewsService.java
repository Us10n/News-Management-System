package by.stas.nms.service;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.dto.NewsWithCommentsDto;

import java.util.List;

/**
 * The interface {@code NewsService} adds new methods to CRUD.
 *
 * @see CRUDService
 */
public interface NewsService extends CRUDService<NewsWithCommentsDto, String> {
    /**
     * Read by id news with pageable.
     *
     * @param id    the id
     * @param page  the page
     * @param limit the limit
     * @return the news with comments dto
     */
    NewsWithCommentsDto readById(String id, Integer page, Integer limit);

    /**
     * Read by id without comments.
     *
     * @param id the id
     * @return the news dto
     */
    NewsDto readByIdWithoutComments(String id);

    /**
     * Read all objects with pageable.
     *
     * @param page  the page
     * @param limit the limit
     * @return the list of objects matching params
     */
    List<NewsDto> readAll(Integer page, Integer limit);

    /**
     * Read all objects using Fulltext search with pageable.
     *
     * @param term  the term form fulltext search
     * @param page  the page
     * @param limit the limit
     * @return the list of objects matching params.
     */
    List<NewsDto> readAll(String term,Integer page, Integer limit);
}
