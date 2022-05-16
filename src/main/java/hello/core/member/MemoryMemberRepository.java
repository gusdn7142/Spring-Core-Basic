package hello.core.member;


import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository {  //MemberRepository 인터페이스를 상속받음


    private static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {  //회원 저장
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {  //ID로 회원 조회
        return store.get(memberId);
    }



}
