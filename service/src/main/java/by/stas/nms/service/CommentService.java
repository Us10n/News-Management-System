package by.stas.nms.service;

import by.stas.nms.dto.CommentDto;

import java.util.List;

public interface CommentService extends CRUDService<CommentDto, String> {
    List<CommentDto> readAll(Integer page, Integer limit);
}
