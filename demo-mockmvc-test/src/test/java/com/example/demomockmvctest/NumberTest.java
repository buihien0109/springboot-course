package com.example.demomockmvctest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class NumberTest {
    static final Logger logger = Logger.getLogger(NumberTest.class.getName());

    @BeforeAll
    static void beforeAllTests() {
        logger.info("Before all tests");
    }

    @AfterAll
    static void afterAllTests() {
        logger.info("After all tests");
    }

    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {
        logger.info(() -> String.format("About to execute [%s]",
                testInfo.getDisplayName()));
    }

    @AfterEach
    void afterEachTest(TestInfo testInfo) {
        logger.info(() -> String.format("Finished executing [%s]",
                testInfo.getDisplayName()));
    }

    @DisplayName("Test if number is greater than zero") // tên của test case
    @ParameterizedTest(name = "{index} => number=''{0}''") // tên của test case
    @ValueSource(ints = {1, 2, 3}) // danh sách các giá trị đầu vào
    void palindromes(int number) {
        assertTrue(isGreaterThanZero(number));
    }

    private boolean isGreaterThanZero(int number) {
        return number > 0;
    }
}
