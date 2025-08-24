package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProfileTest {

    @Test
    void profile(){
        new Profile("12345");
        new Profile("kkk");
        new Profile("agg");
        new Profile("");
    }

    @Test
    void profileFail(){
        assertThatThrownBy(() -> new Profile("1234567891234568")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("asdasfasfasfasfasdsadsa")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("A")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url(){
        var profile = new Profile("tobyilee");

        assertThat(profile.url()).isEqualTo("@tobyilee");
    }


}