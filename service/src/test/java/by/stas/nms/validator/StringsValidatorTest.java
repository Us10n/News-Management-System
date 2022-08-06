package by.stas.nms.validator;

import by.stas.nms.exception.ExceptionHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

@ActiveProfiles("test")
class StringsValidatorTest {

    @ParameterizedTest
    @MethodSource("provideValidIdStrings")
    void isIdStringValidPositive(String id) {
        ExceptionHolder exceptionHolder=new ExceptionHolder();
        StringsValidator.isIdStringValid(id,exceptionHolder);
        Assertions.assertTrue(exceptionHolder.getExceptionMessages().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidIdStrings")
    void isIdStringValidNegative(String id) {
        ExceptionHolder exceptionHolder=new ExceptionHolder();
        StringsValidator.isIdStringValid(id,exceptionHolder);
        Assertions.assertFalse(exceptionHolder.getExceptionMessages().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("provideValidTermStrings")
    void isTermStringValidPositive(String term) {
        ExceptionHolder exceptionHolder=new ExceptionHolder();
        StringsValidator.isTermStringValid(term,exceptionHolder);
        Assertions.assertTrue(exceptionHolder.getExceptionMessages().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTermStrings")
    void isTermStringValidNegative(String term) {
        ExceptionHolder exceptionHolder=new ExceptionHolder();
        StringsValidator.isTermStringValid(term,exceptionHolder);
        Assertions.assertFalse(exceptionHolder.getExceptionMessages().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("provideValidDates")
    void isDateValidPositive(String date) {
        Assertions.assertTrue(StringsValidator.isDateValid(date));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDates")
    void isDateValidNegative(String date) {
        Assertions.assertFalse(StringsValidator.isDateValid(date));
    }

    @ParameterizedTest
    @MethodSource("provideValidTitles")
    void isTitleValidPositive(String title) {
        Assertions.assertTrue(StringsValidator.isTitleValid(title));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTitles")
    void isTitleValidNegative(String title) {
        Assertions.assertFalse(StringsValidator.isTitleValid(title));
    }

    @ParameterizedTest
    @MethodSource("provideValidTexts")
    void isTextValidPositive(String text) {
        Assertions.assertTrue(StringsValidator.isTextValid(text));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTexts")
    void isTextValidNegative(String text) {
        Assertions.assertFalse(StringsValidator.isTextValid(text));
    }

    @ParameterizedTest
    @MethodSource("provideValidUsernames")
    void isUsernameValidPositive(String username) {
        Assertions.assertTrue(StringsValidator.isUsernameValid(username));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUsernames")
    void isUsernameValidNegative(String username) {
        Assertions.assertFalse(StringsValidator.isUsernameValid(username));
    }

    public static Stream<String> provideValidIdStrings() {
        return Stream.of(
                "ada1291e811219729de860eb",
                "ada1291e811219729de860ea",
                "12a1291e811219729de860e1"
        );
    }

    public static Stream<String> provideInvalidIdStrings() {
        return Stream.of(
                "",
                "awd",
                null,
                "107f191e81wd1972914124wa123"
        );
    }

    public static Stream<String> provideValidTermStrings() {
        return Stream.of(
                "term",
                "Also term",
                "Also Also term"
        );
    }

    public static Stream<String> provideInvalidTermStrings() {
        return Stream.of(
                null,
                "",
                "        "
        );
    }

    private static Stream<String> provideValidDates() {
        return Stream.of(
                "2013-03-10T02:00:00",
                "2013-03-10T02:00:00",
                "2022-09-20T02:00:00",
                "2013-09-11T12:00:00"
        );
    }

    private static Stream<String> provideInvalidDates() {
        return Stream.of(
                "2013-03-10 02:00:00",
                "2013-03-10T02:00:00Z",
                "2022:09:20T02:00:00",
                "2013-09-11-12:00:00",
                null
        );
    }

    private static Stream<String> provideValidTexts() {
        return Stream.of(
                "There once was a ship",
                "that put to sea",
                "And the name of that ship was the Billy o' Tea",
                "The winds blew hard, her bow dipped down Blow, me bully boys, blow (Hah!)",
                "Alt + 0151 is a –"
        );
    }

    private static Stream<String> provideInvalidTexts() {
        return Stream.of(
                "",
                "1",
                "a",
                null
        );
    }

    private static Stream<String> provideValidTitles() {
        return Stream.of(
                "There once was a ship",
                "that put to sea",
                "And the name of that ship ",
                "was the Billy o' Tea ",
                "Alt + 0151 is a –"
        );
    }

    private static Stream<String> provideInvalidTitles() {
        return Stream.of(
                "",
                "1",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa (more than 30 symbols)",
                null
        );
    }

    private static Stream<String> provideValidUsernames() {
        return Stream.of(
                "Camper",
                "Camper_228",
                "Camper.228",
                "Ca_mper.228",
                "valid.username_12"
        );
    }

    private static Stream<String> provideInvalidUsernames() {
        return Stream.of(
                "C",
                "Ca",
                "Cam",
                "Camp",
                "Camper_.228",
                "Camper_228_",
                "Camper.228.",
                "Camper.228_",
                "Camper_228.",
                "Line_26_characters_lenghts",
                "Line with spaces"
        );
    }
}