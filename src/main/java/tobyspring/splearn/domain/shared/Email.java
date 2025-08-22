package tobyspring.splearn.domain.shared;

import java.util.regex.Pattern;

public record Email( String address) {
    /**
     * 레코드를 만들고 검증하는 코드를 넣어야 되는데 이건 레코드 생성자를 만들면 된다.
     * 근데 생성자 중에서 굉장히 컴팩트한 방식으로 사용할 수 있는 생성자 문법이 있다.
     */

    //매번 반복적으로 사용되고 한 번 컴파일 하는 작업이 들어가니깐 매 생성자에서 다시 이걸 처리하지말고 스태틱으로 집어넣자 또한 외부에 집어넣을일이 없다.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}");

    public Email{
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("이메일 형식이 바르지 않습니다: " + address);
        }

    }
}
