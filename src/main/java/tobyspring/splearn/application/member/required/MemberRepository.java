package tobyspring.splearn.application.member.required;

import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.shared.Email;
import tobyspring.splearn.domain.member.Member;

import java.util.Optional;

/**
 * 회원 정보를 저장하거나 조회한다.
 * spring-data는 큰틀의 추상화 다른 종류로 변경할때 이 추상화된 리포지토리를 사용할 코드들은 건들일일이 없다.
 * 더 큰 이유는 업데이트할때 스프링 데이터가 제공하는 기능중 Domain Event Publication 도메인 이벤트를 발행하는 그런 기능이 일어난다.
 * 만약 save를 하지 않으면 이게 안된다. 업데이트 시 꼭 save를 해야 된다.
 * 또한 Auditing을 위해서도 필요하다.
 */
public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member); //spring data 에서는 수정할때도 save로 간다. 스프링 데이터를 사용하니깐 어뎁터를 안만들어도 된다.


    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memberId); //옵셔널은 리턴타입에만.
}
