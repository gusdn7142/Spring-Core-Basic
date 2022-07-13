package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;



@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L;   //static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);

        store.put(member.getId(), member);
        return member;
    }

    //id(기본키)로 회원 조회
    public Member findById(Long id) {
        return store.get(id);
    }

    //로그인 id로 회원 조회
    public Optional<Member> findByLoginId(String loginId) {

        //코드1
/*        List<Member> all = findAll();

        for (Member member : all) {
            if(member.getLoginId().equals(loginId)){   //같은 로그인 id가 존재하면
                return Optional.of(member);
            }
        }
        return Optional.empty();  //null*/


        //코드2 : 코드1을 람다와 스트림으로 변경
        return findAll().stream()   //리스트를 스트림으로 바꾸어서
                .filter(member -> member.getLoginId().equals(loginId))
                .findFirst();

    }

    //전체 회원 조회
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }



}
