package tobyspring.splearn.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * 레코드는 불변 오브젝트를 만드는데 굉장히 뛰어나다. 적극 활용하자
 * 게다가 그 이전에 일반적인 자바빈을 만들 때 프로퍼티를 멤버 필드로 정의하고 생성자에서 얘를 주입받아서 할당하고 이거를 레코드 헤더라는
 * 원래 이제 자바 클래스에는 뒤로 바로 붙지 않는데 레코드는 이런 문법이 지원이 된다.
 */
public record MemberRegisterRequest(
        @Email String email,
        @Size(min = 5, max = 20) String nickname,
        @Size(min = 8, max = 100) String password) {
    /**
     * 이렇게 record로 만들게 되면 이 안에 필드도 만들어진다.
     * ex
     * privte string email
     *
     *  그리고 동시에 생성자 역할도 해준다.
     *  그래서 이걸 받아서 같은 이름의 필드에다가 이걸 저장을 해둔다.
     *  사실상 final처럼 동작을 한다. 이유는 변경할 수 있는 메소드를 제공하지 않는다.
     *
     *  그리고 getter를 제공한다. 특이한방식으로
     *  get~~가 아닌 이 레코드 컴포넌트 이름 그 자체를 사용한다.
     *
     *
     *  만약 검증이 필요하면 Canonical constructor라고
     *  constructor를 만들어서 그 안에서 검증하거나 아니면 거기서 뭔가 주입된 값을 가공해서 집어넣는 등의 작업은 가능하다.
     *
     *  컴파일이되면 일반 클래스로 바뀐다.
     */


}
