package by.stas.nms.service;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.dto.NewsWithCommentsDto;

import java.util.List;

public interface NewsService extends CRUDService<NewsWithCommentsDto, String> {
    NewsWithCommentsDto readById(String id, Integer page, Integer limit);
    List<NewsDto> readAll(Integer page, Integer limit);
    List<NewsDto> readAll(String term,Integer page, Integer limit);
}
