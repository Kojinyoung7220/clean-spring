package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest // jpa 테스트와는 달리 리포지토리에 주목해서 어떤 테스트를 수행하는 게 아니기 때문에 명시적으로 어떤 설정을 넣지 않으면 SQL이 보이지 않는다. -> yml파일에 설정
@Transactional
@Import(SplearnTestConfiguration.class) // 중첩 클래스에서 -> 상위 클래스로 뺐기 때문에 사용한다고 임포트를 해줘야 한다. 또한 애플리케이션보다 테스트에 만든 것이 우선이다!
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager entityManager){


    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(member.getId()).isEqualTo(found.getId());
    }

    @Test
    void findFail() {
        assertThatThrownBy(() -> memberFinder.find(999L)).isInstanceOf(IllegalArgumentException.class);
    }
}