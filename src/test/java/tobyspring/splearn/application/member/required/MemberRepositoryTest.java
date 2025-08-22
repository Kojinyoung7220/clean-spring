package tobyspring.splearn.application.member.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tobyspring.splearn.domain.member.Member;

import static org.assertj.core.api.Assertions.*;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;


/**
 *  메모리 db를 사용하고 jpa 내부적으로도 동작을 해야 된다.
 *  이런경우 사용하려고 DataJpaTest가 있다.
 *  이건 앞으로 만들어지는 스프링의 모든 빈을 다 띄우지 않고, 데이터 jpa 리포지토리가 동작하는데 필요로 하는 최소한의 빈들로만 스프링 컨테이너를 구성해서 테스트에서 사용할 수 있게 해준다..
 */
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;


    @Test
    void createMember() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();

    }

    @Test
    void duplicateEmail() {
        Member member1 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        memberRepository.save(member1);

        Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        assertThatThrownBy(() -> memberRepository.save(member2)).isInstanceOf(DataIntegrityViolationException.class);

    }


}