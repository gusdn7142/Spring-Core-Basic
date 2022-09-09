package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  //트랜잭션 (테스트에서는 default로 커밋을 하고 테스트가 종료되면 롤백을 한다.)
//@Commit          //트랜잭션 커밋 (@Transactional의 롤백을 막기 위해 필요),  다른 테스트들과 꼬일수 있으므로 제거
class MemberTest {

    //@Autowired
    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {

        //Team 객체 생성 후 DB에 저장
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        //Member객체 생성 후 DB에 저장
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //한 사이클에 조회를 위해 영속성 컨텍스트 초기화
        em.flush();
        em.clear();

        //전체 Member 객체 조회
        List<Member> members = em.createQuery("select m from Member m",
                        Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member= " + member);
            System.out.println("-> member.team= " + member.getTeam());   //프록시로 team을 불러왔기 때문에, 이떄 team이 사용되므로 DB에서 team을 조회
        }

    }
}