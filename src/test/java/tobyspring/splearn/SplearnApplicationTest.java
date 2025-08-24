package tobyspring.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 테스트 커버리지를 맞추기 위한 run 메서드 테스트코드 작성
 */
class SplearnApplicationTest {

    @Test
    void run() {
        try(MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

            SplearnApplication.main(new String[0]); //main 메소드 실행

            mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
        }
    }

}