package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 이건 멤버 서비스를 가져올 필요가 없다..?!
 * 아주 간단한 최소한의 컨테이너를 올려서 테스트가 가능하다. -> 통합테스트
 */
@SpringBootTest // jpa 테스트와는 달리 리포지토리에 주목해서 어떤 테스트를 수행하는 게 아니기 때문에 명시적으로 어떤 설정을 넣지 않으면 SQL이 보이지 않는다. -> yml파일에 설정
@Transactional
@Import(SplearnTestConfiguration.class) // 중첩 클래스에서 -> 상위 클래스로 뺐기 때문에 사용한다고 임포트를 해줘야 한다. 또한 애플리케이션보다 테스트에 만든 것이 우선이다!
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) // 테스트 코드 record로 변한 후 에러(junit)가 생겨 추가한 어노테이션 => 레코드의 파라미터를 스프링 컨테이너를 통해서 빈으로 가져온다. => junit 프로퍼티에 설정하는걸로 변경
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        System.out.println(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest())).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void activate(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void memberRegisterRequestFail() {

        checkValidation(new MemberRegisterRequest("tobyspring@gmail.com", "Toby", "longsecret"));
        checkValidation(new MemberRegisterRequest("tobyspring@gmail.com", "Charlieeeeeeeeeeeeeeeeeeeeee", "longsecret"));
        checkValidation(new MemberRegisterRequest("tobyspringgmail.com", "Toby", "longsecret"));

    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() ->  memberRegister.register(invalid)).isInstanceOf(ConstraintViolationException.class);
    }
}
