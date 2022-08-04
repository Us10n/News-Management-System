package by.stas.nms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class CommentRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllBy() {
    }

    @Test
    void findCommentById() {
    }

    @Test
    void findCommentsByNewsId() {
    }

    @Test
    void testFindCommentsByNewsId() {
    }

    @Test
    void deleteCommentById() {
    }

    @Test
    void deleteCommentsByNewsId() {
    }

    @Test
    void save() {
    }

    @Test
    void findAll() {
    }

    @Test
    void saveAll() {
    }
}