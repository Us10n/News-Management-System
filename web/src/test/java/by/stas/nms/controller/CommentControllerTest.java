package by.stas.nms.controller;

import by.stas.nms.dto.CommentDto;
import by.stas.nms.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommentController.class)
@ActiveProfiles("test")
class CommentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @Test
    void createCommentNullContent() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void createCommentContentPassed() throws Exception {
        CommentDto commentDto = new CommentDto();

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void readAllCommentsNoParametersPassed() throws Exception {
        mockMvc.perform(get("/comments")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllCommentsValidParametersPassed() throws Exception {
        mockMvc.perform(get("/comments?page=1&limit=10")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllCommentsInvalidParametersPassed() throws Exception {
        mockMvc.perform(get("/comments?page=-1&limit=-10")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void readAllCommentsByTermNoParametersPassed() throws Exception {
        mockMvc.perform(get("/comments")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllCommentsByTermValidParametersPassed() throws Exception {
        mockMvc.perform(get("/comments?term=term?page=1&limit=10")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllCommentsByTermInvalidParametersPassed() throws Exception {
        mockMvc.perform(get("/comments?page=-1&limit=-10")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void readCommentById() throws Exception {
        mockMvc.perform(get("/comments/{id}", "123")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCommentNullContentPassed() throws Exception {
        mockMvc.perform(patch("/comments/{id}", "123")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void updateCommentContentPassed() throws Exception {
        CommentDto commentDto = new CommentDto();

        mockMvc.perform(patch("/comments/{id}", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(commentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/comments/{id}", "123")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}