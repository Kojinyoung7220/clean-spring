package tobyspring.splearn.adapter.webapi;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 8-41
 * webMvcTest 어노테이션은 JPA 테스트랑 비슷하게 지금 이 안에 존재하는 모든 스프링 빈을 다 올리지 않고,
 * 웹 파트를 담당하는 컨트롤러를 테스트하는데 필요로 하는 이것과 관련된 빈들만 딱 실행을 해준다.
 *
 * MockMvc를 사용하는게 스프링 6.1까지는 필수였지만
 * 6.2부터 MockMvcTester가 나왔다
 * MockMvcTester는 AssertJ가 접목이 돼 있다
 */
@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiWebMvcTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;

    @MockitoBean
    MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException {
        Member member = MemberFixture.createMember(1L);
        when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").asNumber().isEqualTo(1);

        verify(memberRegister).register(request);
    }

    @Test
    void registerFail() throws JsonProcessingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email");
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }
}