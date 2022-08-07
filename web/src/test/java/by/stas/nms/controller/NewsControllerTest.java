package by.stas.nms.controller;

import by.stas.nms.dto.NewsDto;
import by.stas.nms.service.NewsService;
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
@WebMvcTest(controllers = NewsController.class)
@ActiveProfiles("test")
class NewsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    NewsService newsService;

    @Test
    void createNewsNullNewsPassed() throws Exception {
        mockMvc.perform(post("/news")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void createNewsContentPassed() throws Exception {
        NewsDto NewsDto = new NewsDto();

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(NewsDto)))
                .andExpect(status().isOk());
    }

    @Test
    void readAllnewsNoParametersPassed() throws Exception {
        mockMvc.perform(get("/news")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllnewsValidParametersPassed() throws Exception {
        mockMvc.perform(get("/news?page=1&limit=10")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllnewsInvalidParametersPassed() throws Exception {
        mockMvc.perform(get("/news?page=-1&limit=-10")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void readAllnewsByTermNoParametersPassed() throws Exception {
        mockMvc.perform(get("/news")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllnewsByTermValidParametersPassed() throws Exception {
        mockMvc.perform(get("/news?term=term?page=1&limit=10")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void readAllnewsByTermInvalidParametersPassed() throws Exception {
        mockMvc.perform(get("/news?page=-1&limit=-10")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void readNewsById() throws Exception {
        mockMvc.perform(get("/news/{id}", "123")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void updateNewsNullContentPassed() throws Exception {
        mockMvc.perform(patch("/news/{id}", "123")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    @Test
    void updateNewsContentPassed() throws Exception {
        NewsDto NewsDto = new NewsDto();

        mockMvc.perform(patch("/news/{id}", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsBytes(NewsDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNews() throws Exception {
        mockMvc.perform(delete("/news/{id}", "123")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}