package tobyspring.splearn.adapter.webapi;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

/**
 * 8-41
 * API 테스트는 어떻게?
 * 1. API만 유닛테스트를 한다.
 *  -> 의존하는게 있는데 어떻게? => MOCK으로 만들어서 테스트를 진행한다.
 * 2. 기능테스트를 진행한다
 *  -> API부터 service레벨, FACK DB등 모든 레벨까지 모두 테스트하는 전 기능 테스트를 진행한다.
 *
 *  API를 실행해서 테스트하는 2가지 방법
 *  1. API 빈을 가져와서 여기서 이 register 메서드를 직접 테스트 코드에서 호출하는 방법
 *      -> PostMapping은 테스트가 안되는 단점이 있다.
 *      그래서 컨트롤러 안에 있는 메소드를 바로 실행하는 것 보다는 전체 코드의 기능을 다 확인하고 싶다 그러면 mock MVC 테스트로 톰켓까지 띄어서 진짜 API를 직접 쏘고 이건 아니지만
 *      진짜 API를 호출하는 것과 거의 동일한 메커니즘으로 이게 다 돌아가는데, 톰켓이라는 웹서버만 없는 채로 스프링 컨테이너가 필요한 게 올라와서 그 안에서 테스트가 돌아가는 이 방식을 선택하면
 *      이런 류의 우리가 코드 추가한 것도 제대로 기능이 동작하는가 테스트가 되는 것이다.
 */
@RestController
@RequiredArgsConstructor
public class MemberApi {

    private final MemberRegister memberRegister;

    //register api -> /member/register
    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
