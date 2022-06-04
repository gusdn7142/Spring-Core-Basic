package hello.servlet.domain.member;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */

public class MemberRepository {  //테슽트 코드 작성 단축키 (ctrl + shift +t)

    //필드 선언
    private Map<Long, Member> store = new HashMap<>();   //store은 DB역할이다.
    private static long sequence = 0L;   //id값으로 사용 예정

    //싱글톤으로 MemberRepository 객체 생성
    private static final MemberRepository instance = new MemberRepository();


    //getter()로만 MemberRepository 객체 반환
    public static MemberRepository getInstance() {
        return instance;
    }

    //기본 생성자
    private MemberRepository() {
    }

    //회원 저장
    public Member save(Member member){
        member.setId(++sequence);  //id값을 1씩 증가시키면서 저장 (autoincrement 역할)
        store.put(member.getId(), member);   //store DB에 id와 회원 객체 저장
        return member;  //member 객체 반환
    }

    //회원 조회 (by ID)
    public Member findById(Long id) {
        return store.get(id);  //id를 통해 store에서 해당 객체를 꺼냄
    }

    //모든 회원 조회
    public List<Member> findAll() {
        return new ArrayList<>(store.values());  //리스트 객체에 모든 회원 정보를 넘겨줌
    }

    //모든 회원 정보 삭제
    public void clearStore() {
        store.clear();
    }




}
