package pl.postinvoice.invoice.accountant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class FindAccountantResponseTest {

    @Test
    void givenAccountant_whenFrom_thenCorrectResponse() {
        //gievn
        long expectedId = 4L;
        String expectedName = "Matt";
        Accountant accountant = Accountant.builder()
                .id(expectedId)
                .name(expectedName)
                .build();

        //when
        FindAccountantResponse result = FindAccountantResponse.from(accountant);

        //then

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(accountant.getId());
        assertThat(result.getName()).isEqualTo(accountant.getName());



    }
}