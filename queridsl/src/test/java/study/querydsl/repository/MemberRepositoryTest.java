package study.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchParam;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Commit
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void basicTest() {

        //DB에 member 객체 저장
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        //id를 통해 member 객체 조회
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        //전체 member 객체 조회
        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).containsExactly(member);

        //username을 통해 member 객체 조회
        List<Member> result2 = memberRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);

    }




    //where절에 메서드(파라미터)를 사용해 조건을 주어 member 객체를 조회하는 테스트
    @Test
    public void searchTest() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //where절 함수(파라미터)에 사용할 조건값 셋팅
        MemberSearchParam param = new MemberSearchParam();
        param.setAgeGoe(35);   //나이 >= 35
        param.setAgeLoe(40);   //나이 <= 40
        param.setTeamName("teamB");

        //param 조건을  where절 함수(파라미터)에 넣어 Member 객체를 select한 결과를 MemberTeamDto객체로 반환
        List<MemberTeamDto> result = memberRepository.search(param);  //함수(param);

        assertThat(result).extracting("username").containsExactly("member4");

    }




}