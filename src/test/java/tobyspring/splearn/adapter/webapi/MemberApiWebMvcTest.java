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
 *
 *  에플리케이션 레이어부터 시작해서 후반부 작업이 안됐는데 API 개발을 미리 해두고 싶다.
 *  혹은 뒤에 작업이 너무 복잡해서 쿼리도 되게 많고 연산도 많이 해야 되는 경우
 *  (ex memberAPI에서 로그인, 인증관련 코드들을 집중적으로 테스트 하고 싶은 경우도 포함) 단위테스트 진행
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