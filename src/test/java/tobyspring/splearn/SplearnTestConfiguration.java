package tobyspring.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.PasswordEncoder;

@TestConfiguration // 안에 애노테이션 빈이 붙은 팩토리 메소드를 만들어 준다. 이걸 등록해놓으면 이 메소드들을 실행해서 결과로 리턴된 오브젝트들을 스프링 빈으로 사용해준다
public class SplearnTestConfiguration {

    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> System.out.println("Sending email: " + email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
