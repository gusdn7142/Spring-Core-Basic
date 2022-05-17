package hello.core.member;


import hello.core.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;



public class MemberServiceTest {

    MemberService memberService;

    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }


//    MemberService memberService = new MemberServiceImpl();




    @Test
    void join() {

        //멤버 객체 생성
        Member member = new Member(1L, "member A", Grade.VIP);

        //회원 가입
        memberService.join(member);

        //회원 조회
        Member findMember = memberService.findMember(1L);

        //결과 확인 (가입한 유저 확인)
        assertThat(member).isEqualTo(findMember);  //

    }







}
