package by.stas.nms.mapper;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto mapToDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    Comment mapToEntity(CommentDto orderDto);
}
