package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;



class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();



    @Test
    void sessionTest() {

        //서버의 응답 : 세션 생성 후 반환
        MockHttpServletResponse response = new MockHttpServletResponse();  //가짜 response를 만듦

        Member member = new Member();                                        //회원 객체 생성
        sessionManager.createSession(member, response);                    //회원객체와 response를 인자로 넘겨주어 세션저장소에 (세션 id, 회원객체) 저장 후 (세션 명, 세션 id)로 쿠키 생성


        //웹브라우저의 요청 : 서버에서 받아온 세션 쿠키를 요청에 담아 요청
        MockHttpServletRequest request = new MockHttpServletRequest();  //가짜 reqyest 만듦
        request.setCookies(response.getCookies());



        //서버의 응답 : 요청 정보로 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);   //세션에 담긴 value가 회원객체와 일치하는지 확인



        //서버의 응답 : 요청 정보로 세션 만료 시키기
        sessionManager.expire(request);

        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();    //세션에 담긴 value가 null인지 확인

    }



}