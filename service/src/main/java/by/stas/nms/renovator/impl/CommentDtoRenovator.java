package by.stas.nms.renovator.impl;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.renovator.Renovator;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoRenovator implements Renovator<CommentDto> {
    @Override
    public void updateObject(CommentDto newObject, CommentDto oldObject) {
        if (newObject.getDate() == null) {
            newObject.setDate(oldObject.getDate());
        }
        if (newObject.getText() == null) {
            newObject.setText(oldObject.getText());
        }
        if (newObject.getUsername() == null) {
            newObject.setUsername(oldObject.getUsername());
        }
        newObject.setNewsId(oldObject.getNewsId());
    }
}
