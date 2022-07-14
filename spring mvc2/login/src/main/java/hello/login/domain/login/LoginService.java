package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null 로그인 실패
     */
    public Member login(String loginId, String password) {


        //코드1
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);

        if (findMemberOptional.isPresent()) {   //객체가 값을 가지고 있고
            Member member = findMemberOptional.get();

            if (member.getPassword().equals(password)) {   //패스워드가 일치하면
                return member;
            }
        }
        return null;



        //코드2 (람다식 적용)
//        return memberRepository.findByLoginId(loginId)
//                .filter(member -> member.getPassword().equals(password))
//                .orElse(null);
    }





}