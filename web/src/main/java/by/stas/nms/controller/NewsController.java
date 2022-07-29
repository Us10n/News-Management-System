package by.stas.nms.controller;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.service.NewsService;
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
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsWithCommentsDto createNews(@RequestBody NewsWithCommentsDto newsDto) {
        return newsService.create(newsDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> readAllNews(@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                     @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return newsService.readAll(page, limit);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsWithCommentsDto readNewsById(@PathVariable String id,
                                            @RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer page,
                                            @RequestParam(name = "limit", defaultValue = "10") @Positive Integer limit) {
        return newsService.readById(id, page, limit);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsWithCommentsDto updateNewsById(@PathVariable String id,
                                              @RequestBody NewsWithCommentsDto newsDto) {
        newsDto.setId(id);
        return newsService.update(newsDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteNewsById(@PathVariable String id) {
        newsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
