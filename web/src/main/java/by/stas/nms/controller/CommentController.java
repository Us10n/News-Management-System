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

@RestController
@Validated
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.create(commentDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> readAllComments(@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                            @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return commentService.readAll(page, limit);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> readAllCommentsByTerm(@RequestParam(name = "term", defaultValue = "") String term,
                                                  @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                                  @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return commentService.readAll(term, page, limit);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto readCommentById(@PathVariable String id) {
        return commentService.readById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable String id,
                                    @RequestBody CommentDto commentDto) {
        commentDto.setId(id);
        return commentService.update(commentDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
