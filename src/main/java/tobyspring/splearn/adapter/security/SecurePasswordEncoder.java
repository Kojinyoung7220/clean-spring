package tobyspring.splearn.adapter.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tobyspring.splearn.domain.member.PasswordEncoder;

/**
 * https://www.inflearn.com/community/questions/1620414/%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4-%EC%9C%84%EC%B9%98%EB%A5%BC-%EA%B2%B0%EC%A0%95%ED%95%98%EB%8A%94-%EA%B8%B0%EC%A4%80%EC%97%90-%EA%B4%80%ED%95%B4
 * Member라는 엔티티가 이 PasswordEncoder에 의존(사용)하기 때문이기도 하죠.
 * 이런 경우 PasswordEncoder를 애플리케에션 계층에 두면 도메인이 그 밖에 있는 애플리케이션 계층에 의존하는,
 * 계층 아키텍처의 의존 규칙을 위반하게 됩니다. 그걸 피하려면 비밀번호를 인코딩하는 걸 서비스 계층에서 하고,
 * 그렇게 얻은 해시값을 다시 Member에 넘겨야 하는데,
 * 이렇게 코드가 만들어지면 도메인 로직이 두 계층에 흩어지는 문제가 있기도 하죠.
 *
 */
@Component
public class SecurePasswordEncoder implements PasswordEncoder {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @Override
    public String encode(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String passwordHash) {
        return bCryptPasswordEncoder.matches(password, passwordHash);
    }
}
