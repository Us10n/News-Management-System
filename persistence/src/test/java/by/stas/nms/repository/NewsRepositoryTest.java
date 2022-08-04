package by.stas.nms.repository;

import by.stas.nms.entity.News;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith({SpringExtension.class})
class NewsRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findNewsById() {
        mongoTemplate.findAll(News.class);
//        Optional<News> news = newsRepository.findNewsById("62e3fa045c47595671844145");
//        Assertions.assertTrue(news.isEmpty());
    }

    @Test
    void deleteById() {
    }

    @Test
    void findAllBy() {
    }

    @Test
    void save() {
    }

    @Test
    void findAll() {
    }
}