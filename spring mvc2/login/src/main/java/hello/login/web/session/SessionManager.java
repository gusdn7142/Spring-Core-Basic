package hello.login.web.session;


import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;




/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";   //쿠키에 담을 세션 이름

    //세션 저장소
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();   //동시성 이슈 문제를 해결하기 위해 ConcurrentHashMap을 사용



    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response) {

        //세션 id를 생성하고, 값을 세션 저장소에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키 생성 (세션 이름, 세션 id)
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }



    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {

        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);   //쿠키 조회 (세션명으로)

        if (sessionCookie == null) {  //쿠키가 null이면
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());  //세션id로 세션 값(객체) 조회
    }



    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);   //쿠키 조회 (세션명으로)

        if (sessionCookie != null) {  //쿠키가 null이 아니면
            sessionStore.remove(sessionCookie.getValue());   //세션 id로 세션 제거
        }

    }





    //쿠키 찾는 함수
    public Cookie findCookie(HttpServletRequest request, String cookieName) {

        //코드1
        if (request.getCookies() == null) {  //쿠키가 없으면
            return null;
        }

        return Arrays.stream(request.getCookies())  //배열의 값을 하나씩 넘김
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);

        //코드2
/*        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }

        for(Cookie cookie : cookies){
            if(cookie.getName().equals(SESSION_COOKIE_NAME)){
                return sessionStore.get(cookie.getValue());
            }
        }*/

    }




}
