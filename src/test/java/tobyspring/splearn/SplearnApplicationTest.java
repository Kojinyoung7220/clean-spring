package tobyspring.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class SplearnApplicationTest {

    @Test
    void run() {
        try(MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

            SplearnApplication.main(new String[0]); //main 메소드 실행

            mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
        }
    }

}