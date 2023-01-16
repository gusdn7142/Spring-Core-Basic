package hello.springtx.propagation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext                //생략 가능
    private final EntityManager em;    //EntityManager에 의존성 주입

    //회원 등록
    @Transactional  //추가
    public void save(Member member) {
        log.info("member 저장");
        em.persist(member);   //영속성 컨텍스트에의 1차 캐시에 member 객체를 저장하고 쓰기지연 SQL 저장소에 Insert 쿼리를 저장 후 후 커밋될떄 DB에 SQL 전달
    }

    //(username을 통해) 회원 조회
    public Optional<Member> find(String username) {
        return em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", username)
                .getResultList().stream().findAny();  //결과가 2개이면 먼저 조회된 것을 반환
    }

}