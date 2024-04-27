package pl.postinvoice.Util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PeselValidatorTest {


    @ParameterizedTest
    @CsvSource({
            "0123456789, false",
            "01234567899, false",
            "abcdefghijk , false",
            "88010313518, true",
            "88010313511, false",
            "60101759382, true",
            "60101759381, false"


    })
    void givenPesel_whenIsPeselValid_thenExpectedResult(String pesel, boolean expectedIsValid) {

        // when
        boolean peselValid = PeselValidator.isPeselValid(pesel);

        // then
        Assertions.assertThat(peselValid).isEqualTo(expectedIsValid);


    }
}