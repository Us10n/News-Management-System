package by.stas.nms.controller;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.logging.ServiceAspect;
import by.stas.nms.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Class {@code NewsController} is an endpoint of the API
 * which allows to perform CRUD operations with news.
 * Annotated by {@link RestController} with no parameters to provide an answer in application/json.
 * Annotated by {@link RequestMapping} with parameter value = "/news".
 * Annotated by {@link Validated} with no parameters.
 * So that {@code NewsController} is accessed by sending request to /news endpoint.
 */
@RestController
@Validated
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Method for saving new news.
     *
     * @param newsDto news to save
     * @return saved news
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsWithCommentsDto createNews(@RequestBody NewsWithCommentsDto newsDto) {
        return newsService.create(newsDto);
    }

    /**
     * Method for getting all news. Supports pagination.
     *
     * @param page  result page (default=0)
     * @param limit result limit (default=10)
     * @return list with found news.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> readAllNews(@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                     @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return newsService.readAll(page, limit);
    }

    /**
     * Method for getting all news using Fulltext search. Supports pagination.
     *
     * @param term  term for fulltext search
     * @param page  result page (default=0)
     * @param limit result limit (default=10)
     * @return list with found news.
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> readAllNewsByTerm(@RequestParam(name = "term",defaultValue = "") String term,
                                           @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                           @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return newsService.readAll(term, page, limit);
    }

    /**
     * Method for getting single news with comments by id. Support pagination.
     *
     * @param id news id.
     * @return found single news with comments.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsWithCommentsDto readNewsById(@PathVariable String id,
                                            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                            @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return newsService.readById(id, page, limit);
    }

    /**
     * Method for updating existing news with new values.
     *
     * @param id         news id.
     * @param newsDto object with new values.
     * @return news after update.
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsWithCommentsDto updateNewsById(@PathVariable String id,
                                              @RequestBody NewsWithCommentsDto newsDto) {
        return newsService.update(id,newsDto);
    }

    /**
     * Method for deleting existing news by id.
     *
     * @param id news id.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteNewsById(@PathVariable String id) {
        newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
