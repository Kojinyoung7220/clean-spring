package tobyspring.splearn.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.Email;

@Component
@Fallback
/**
 * @Fallback spring 6.2에 나온 기술 -> 다른 빈을 다 찾다가 이 타입에 빈을 찾아봤는데 찾을 수 없다? 그렇게 됐을 때 대체로 이걸 사용해줘이다 원래는 primary, Qualifier를 사용하기도 한다.
 * 반대로 다른 빈이 아직 구현되지 않았다면 그냥 이걸 사용하고 이 코드를 삭제하지 않은 채로 내가 사용하고 싶은 빈을 가져와서 포함을 시켰다 그때는 얘를 무시해 달라 그런 용도로 사용 가능하다.
 */
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send email: " + email); // 콘솔에 잘 찍히는지 테스트?
    }
}
