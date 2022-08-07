package by.stas.nms.renovator.impl;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.dto.NewsWithCommentsDto;
import by.stas.nms.entity.Comment;
import by.stas.nms.mapper.CommentMapper;
import by.stas.nms.renovator.Renovator;
import by.stas.nms.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The class {@code NewsWithCommentsDtoRenovator} provides method to update old object with new values.
 *
 * @see Renovator
 */
@Component
public class NewsWithCommentsDtoRenovator implements Renovator<NewsWithCommentsDto> {

    private final CommentRepository commentRepository;

    @Autowired
    public NewsWithCommentsDtoRenovator(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void updateObject(NewsWithCommentsDto newObject, NewsWithCommentsDto oldObject) {
        if (newObject.getDate() == null) {
            newObject.setDate(oldObject.getDate());
        }
        if (newObject.getText() == null) {
            newObject.setText(oldObject.getText());
        }
        if (newObject.getTitle() == null) {
            newObject.setTitle(oldObject.getTitle());
        }
        if (newObject.getComments() == null) {
            List<CommentDto> existingCommentDtos = commentRepository.findCommentsByNewsId(oldObject.getId())
                    .stream()
                    .map(CommentMapper.INSTANCE::mapToDto)
                    .collect(Collectors.toList());
            newObject.setComments(existingCommentDtos);
        } else {
            List<CommentDto> newCommentDtos = replaceNewsCommentWithNewOnes(oldObject.getId(), newObject.getComments());
            newObject.setComments(newCommentDtos);
        }
    }

    private List<CommentDto> replaceNewsCommentWithNewOnes(String newsId, List<CommentDto> newCommentDtos) {
        commentRepository.deleteCommentsByNewsId(newsId);
        List<Comment> newComments = newCommentDtos.stream()
                .map(commentDto -> {
                    commentDto.setId(null);
                    commentDto.setNewsId(newsId);
                    return CommentMapper.INSTANCE.mapToEntity(commentDto);
                })
                .collect(Collectors.toList());
        List<Comment> createdComments = commentRepository.saveAll(newComments);

        return createdComments.stream()
                .map(CommentMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }
}
