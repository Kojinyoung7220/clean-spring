package tobyspring.splearn.adapter.integration;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import tobyspring.splearn.domain.Email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DummyEmailSenderTest {

    /**
     *  실제로 print.out이 찍히는지 확인하는 test   
     */
    @Test
    @StdIo
    void dummyEmailSender(StdOut out) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();
        dummyEmailSender.send(new Email("toby@splearn.app"), "subject", "body");
        
        assertThat(out.capturedLines()[0])
                .isEqualTo("DummyEmailSender send email: Email[address=toby@splearn.app]");
    }

}