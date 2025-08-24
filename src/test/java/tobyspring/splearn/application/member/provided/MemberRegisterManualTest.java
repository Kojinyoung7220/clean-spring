//package tobyspring.splearn.application.member.provided;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.test.util.ReflectionTestUtils;
//import tobyspring.splearn.application.MemberService;
//import tobyspring.splearn.application.member.required.EmailSender;
//import tobyspring.splearn.application.member.required.MemberRepository;
//import tobyspring.splearn.domain.shared.Email;
//import tobyspring.splearn.domain.member.Member;
//import tobyspring.splearn.domain.member.MemberFixture;
//import tobyspring.splearn.domain.MemberStatus;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//
///**
// * 스프링 프레임워크나 그런 지원 없이 필요한 스텁들, 목을 만들어서 테스트를 진행
// * 하지만 이런 방법이 매우 번거로움.
// * 그리고 sql을 검증해야되는 일도 있는데 이 방식은 쉽지 않음
// */
//class MemberRegisterManualTest {
//
//    @Test
//    void registerTestStub() {
//        MemberService register = new MemberService(
//                new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder()
//        );
//
//        Member member = register.register(MemberFixture.createMemberRegisterRequest());
//
//        assertThat(member.getId()).isNotNull();
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//    }
//
//    @Test
//    void registerTestMock() {
//        EmailSenderMock emailSenderMock = new EmailSenderMock();
//
//        MemberService register = new MemberService(
//                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
//        );
//
//        Member member = register.register(MemberFixture.createMemberRegisterRequest());
//
//        assertThat(member.getId()).isNotNull();
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//
//        assertThat(emailSenderMock.tos).hasSize(1);
//        assertThat(emailSenderMock.tos.getFirst()).isEqualTo(member.getEmail());
//    }
//
//    @Test
//    void registerTestMockito() {
//        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
//        MemberService register = new MemberService(
//                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
//        );
//
//        Member member = register.register(MemberFixture.createMemberRegisterRequest());
//
//        assertThat(member.getId()).isNotNull();
//        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//
//        Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
//    }
//
//
//
//
//
//    /**
//     * memberRepository는 data jpa라는 기술을 가지고 있기 때문에 인터페이스만 정의하면 인베디드 db를 사용한 리포지토리 구현체(어뎁터)를 만들어준다.
//     */
//    static class MemberRepositoryStub implements MemberRepository {
//        @Override
//        public Member save(Member member) {
//            ReflectionTestUtils.setField(member, "id", 1L);
//            return member;
//        }
//
//        @Override
//        public Optional<Member> findByEmail(Email email) {
//            return Optional.empty();
//        }
//
//        @Override
//        public Optional<Member> findById(Long memberId) {
//            return Optional.empty();
//        }
//    }
//
//    static class EmailSenderStub implements EmailSender {
//        @Override
//        public void send(Email email, String subject, String body) {
//            return;
//        }
//    }
//
//    static class EmailSenderMock implements EmailSender {
//        List<Email> tos = new ArrayList<>();
//
//        @Override
//        public void send(Email email, String subject, String body) {
//            tos.add(email);
//        }
//    }
//
//}