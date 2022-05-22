package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class MemberServiceImpl implements MemberService{


    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public void join(Member member) {  //회원가입
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {  //회원조회
        return memberRepository.findById(memberId);
    }


    //테스트 코드
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }

}
