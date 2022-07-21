package by.stas.nms.mapper;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto mapToDto(Comment comment);

    Comment mapToEntity(CommentDto orderDto);
}
