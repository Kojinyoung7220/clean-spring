package tobyspring.splearn.application.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.member.provided.MemberFinder;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.member.*;
import tobyspring.splearn.domain.shared.Email;

/**
 * 런타임에 어떤 오브젝트가 사용될지, 어떤 어뎁터가 사용될지를 결정하고 이걸 연결해주는 작업을 해줘야 한다 (DI를 통해)
 * 이 클래스는 required 인터페이스를 구현한 어뎁터들을 애플리케이션이 시작될 때 오브젝트를 만들어서 에플리케이션에 연결해주는 구성 작업을 담당하게 된다.
 *
 * 애플리케이션 서비스의 가장 큰 특징은 주요한 도메인과 관련된 기능은 저 도메인 레이어 안에, 우리가 도메인 모델을 코드로 그대로 표현해서 담아놓은 그 오브젝트를 이용해서 처리한다.
 * 그 외에 외부 시스템과 커뮤니케이션을 한다거나, 어떤 기능을 사용하는 것들 그 다음에 어떤 절차적인 작업들이 있다
 * ex) 회원이 하나 등록되게 하기 위해서는 우리가 먼저 사전 조건을 검사해야한다. 넘어온 파라미터의 정보가 이상이 없는지, 이메일같은 경우는 중복이 없는지 등..
 *
 * 서비스를 만들 때 중요하게 생각하는점
 * 1. 도메인은 우리가 설계하고 문서화, 다이어그램을 만들어서 그려 놓은 걸 잘 반영해서 코드로 만들어 내는것이고
 * 서비스는 실제로 우리 서비스에서 사용하는 주요한 어떤 작업들 이런 것들을 어떤 절차를 거쳐서 이 기능을 수행하는가 누구와 협력을 하는가 이런 것들이 코드 안에서 잘 읽힐 수 있게 작성을 해야한다.
 *
 * 서비스카 커지는 경우
 * ex) provided interface가 점점 더 늘어나면(의도, 요구사항 증가로 인한) 그럼 인터페이스가 추가된다.
 * 그럼 컨트롤러(프레젠테이션 오브젝트)도 용도에 따라서 분리될 수 있다.
 */
@Service
@Transactional
@Validated // 파라미터에 설정된 Validation을 먼저 수행해주는 코드를 자동으로 삽입해준다.
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
    private final MemberFinder memberFinder; //같은 포트를 주입받는다..?!  데이터 변경과 관련된 것도 아니고 단순히 이제 조회하는 로직을 모아 놓는 포트이고, 자기 자신의 포트를 이용해서 필요한 기능을 사용할 수도 있다.
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        //check -> 이건 도메인 모델 안에서 검증한다
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        return memberRepository.save(member); //jpa가 아닌 스프링 data를 사용하기 때문에 이렇게 함.. 강의 30번 -> spring data에서 업데이트 상황에서도 save를 호출해야된다고 공식적으로 이야기를 함.
        //spring data는 jpa를 위해서 만들어진게 아니다. 그 외에도 거의 한 10개정도 되는 굉장히 다양한 종류의 데이터 저장 기술들
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.deactivate();

        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberFinder.find(memberId);

        member.updateInfo(memberInfoUpdateRequest);

        return memberRepository.save(member);

    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if(memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다." + registerRequest.email());
        }
    }
}