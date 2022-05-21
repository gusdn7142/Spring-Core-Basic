package hello.core.xml;


import hello.core.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class xmlAppContext {

    @Test
    @DisplayName("XML파일을 설정정보로 넘겨준 뒤 빈을 조회한다.")
    void xmlAppContext() {
        ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
        MemberService memberService = ac.getBean("memberService", MemberService.class);  //(빈 이름, 빈 타입) 으로 빈 객체 조회
        assertThat(memberService).isInstanceOf(MemberService.class);
    }

}
