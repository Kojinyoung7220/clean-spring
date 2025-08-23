package tobyspring.splearn.domain.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 여기서 검증했다고 해서 도메인쪽에서 검증을 안하면 안된다.
 * 도메인 생성하는 로직을 다른곳에서도 만들 수 있다 ex) 배치 job
 */
public record MemberInfoUpdateRequest(
        @Size(min = 5, max = 20)String nickname,
        @NotNull @Size(max = 15)String profileAddress,
        @NotNull String introduction
) {
}
