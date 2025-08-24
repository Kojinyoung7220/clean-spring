package tobyspring.splearn.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;
import tobyspring.splearn.domain.MemberStatus;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
/**
 * 생성자를 외부에서 못만들게 막는다. -> 정적 팩토리 메소드를 만들기 때문에.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

    @NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private MemberDetail detail;

    /**
     * 가장 기본적으로 내부의 상태를 설정하고 기본적인 준비 작업을 하는 건 이렇게 생성자를 만드는 게 좋다
     * 근데 외부에서 도메인 서비스 같은 걸 주입할 때는 생성자를 쓰는 것도 좋지만 스태틱 팩토리 메서드, 정적 팩토리 메소드를 사용하는 게 더 나아 보일 때가 있다.
     * -> 대표적인게 이름을 부여할 수 있다는 것 혹은 이 안에서 좀 더 뭔가 부가적인 작업을 통해서 필요한 경우에는 새로운 오브젝트를 만들지 않고 캐시에서 꺼내 온다거나,
     * 상속 구조를 통해 서브 클래스 중에 하나의 인스턴스를 리턴한다거나 이런 기능을 개발할때 주로 쓴다.
     * 이렇게 외부에서 주입받을땐 생성자 말고 정적 팩토리 메소드를 쓰는 방법도 좋아 보인다.
     * 이 생성자를 나두지만, 다른 사람들이 이걸 바로 쓰는 걸 원치 않는다. 그래서 private로 막는다.
     *
     * recode로 정적 팩토리 메서드를 만들고 나서도 순서를 보장해주지 않는다. 그래서 이 생성자를 그대로 정적 팩토리 메서드를 만들기로 한다. 색션4-17 15분40초
     */
//    private Member(String email, String nickname, String passwordHash) {
//        this.email = Objects.requireNonNull(email);
//        this.nickname = Objects.requireNonNull(nickname);
//        this.passwordHash = Objects.requireNonNull(passwordHash);
//        this.status = MemberStatus.PENDING;
//    }
//

    /**
     *  이렇게 정적 팩토리 메서드를 만들게 되면 파라미터가 엄청 길어지게 될 것이다.
     *  문제는 - 비슷한 타입이 이렇게 연이어서 나오는 경우에 나중에 필드가 추가가 되고 파라미터 순서가 바뀌고 이러다 보면 꼬이는 경우가 생긴다.
     *          근데 얘를 호출하는 쪽에서는 이름을 체크하면 살 수가 없다. 순서만 맞추기 때문에..
     *  빌더로 만들면 안될까? - 빌더의 단점은 생성자에다가 빌더를 붙여서 얘를 호출해서 오브젝트를 만들도록 하더라도 파라미터중 하나를 빼먹게 되면 실행이 된다. 즉 null이 들어가게 된다는 것이다.
     *                      필드가 더 생기면 위험할 수 도 있다
     *  => 대안 1. 파라미터 오브젝트를 사용하자
     *              파라미터 오브젝트란 파라미터들을 하나의 오브젝트에 담아서 넘기면 많은 장점이 있다.
     *              생성을 하기 위해서 전달되어지는 필수 회원 정보 항목 같은 것을 하나의 파라미터 오브젝트로 묶어서 던지는 방식으로 이걸 계산하면 어떨까 싶다.어떤 것이든 완벽한 방법은 없다.
     *              -> 장점은 1. 맴버에게 새로운 속성이 들어가도 파라미터 리스트가 길어지지 않는다.
     *                      2. 맴버 인스턴스를 만들어서 그 안에 필드 값을 세팅하는 코드도 "=" 양쪽 변수로 직관적으로 보이게 돼서 코드리뷰할때 편하다.
     */
    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(createRequest.password()));

        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();


        return member;
    }


    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
        this.detail.activate();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest){
        state(status == MemberStatus.ACTIVE, "등록 완료 상태가 아니면 정보를 수정할 수 없습니다");

        this.nickname = Objects.requireNonNull(updateRequest.nickname());

        this.detail.updateInfo(updateRequest);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}

