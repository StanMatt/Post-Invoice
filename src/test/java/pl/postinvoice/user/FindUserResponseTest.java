package pl.postinvoice.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FindUserResponseTest {

    @Test
    void givenUser_thenfrom_thenFindUserResponse() {
        //given
        String expectedLogin = "Mati";
        long expectedId = 12L;
        User user = User.builder()
                .login(expectedLogin)
                .id(expectedId)
                .build();
        //when
        FindUserResponse response = FindUserResponse.from(user);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getLogin()).isEqualTo(user.getLogin());
        assertThat(response.getId()).isEqualTo(user.getId());

    }
}