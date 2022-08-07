package by.stas.nms.controller;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Class {@code CommentController} is an endpoint of the API
 * which allows to perform CRUD operations with comments.
 * Annotated by {@link RestController} with no parameters to provide an answer in application/json.
 * Annotated by {@link RequestMapping} with parameter value = "/comments".
 * Annotated by {@link Validated} with no parameters.
 * So that {@code CommentController} is accessed by sending request to /comments endpoint.
 */
@RestController
@Validated
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Method for saving new comment.
     *
     * @param commentDto comment to save
     * @return saved comment
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.create(commentDto);
    }

    /**
     * Method for getting all comments. Supports pagination.
     *
     * @param page  result page (default=0)
     * @param limit result limit (default=10)
     * @return list with found comments.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> readAllComments(@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                            @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return commentService.readAll(page, limit);
    }

    /**
     * Method for getting all comments using Fulltext search. Supports pagination.
     *
     * @param term  term for fulltext search
     * @param page  result page (default=0)
     * @param limit result limit (default=10)
     * @return list with found comments.
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> readAllCommentsByTerm(@RequestParam(name = "term", defaultValue = "") String term,
                                                  @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                                  @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return commentService.readAll(term, page, limit);
    }

    /**
     * Method for getting single comment by id.
     *
     * @param id comment id.
     * @return found comment.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto readCommentById(@PathVariable String id) {
        return commentService.readById(id);
    }

    /**
     * Method for updating existing comment with new values.
     *
     * @param id         comment id.
     * @param commentDto object with new values.
     * @return comment after update.
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable String id,
                                    @RequestBody CommentDto commentDto) {
        return commentService.update(id, commentDto);
    }

    /**
     * Method for deleting existing comment by id.
     *
     * @param id comment id.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
