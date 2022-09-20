package study.querydsl.repository;



import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchParam;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
public class MemberJpaRepository {


    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public MemberJpaRepository(EntityManager em) {   //bean으로 등록되어 있다면...   JPAQueryFactory queryFactory도 추가해서 가능.. 이떄는 @RequiredArgsConstructor 사용 가능!!
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);   //JPAQueryFactory : jpa 쿼리를 만들어주는 팩토리 클래스
    }

    //DB에 member 객체 저장
    public void save(Member member) {
        em.persist(member);
    }

    //DB에서 id를 통해 member 객체 조회
    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    //DB에서 전체 member객체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //(querydsl 사용) DB에서 전체 memebe객체 조회
    public List<Member> findAll_Querydsl() {
        return queryFactory
                .selectFrom(member).fetch();
    }



    //DB에서 username을 통해  member 객체 조회
    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username= :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    //(querydsl 사용) DB에서 username을 통해  member 객체 조회
    public List<Member> findByUsername_Querydsl(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }



    //BooleanBuilder (동적쿼리를 위한 where 조건절 생성) 사용
    //회원명, 팀명, 나이(ageGoe, ageLoe)를 조건으로 줌
    public List<MemberTeamDto> searchByBuilder(MemberSearchParam param) {

        BooleanBuilder builder = new BooleanBuilder();   //BooleanBuilder 생성

        if (StringUtils.hasText(param.getUsername())) {    //StringUtils.hasText : 값이 있을때는 true 반환, 값이 없거나 null이면 false 반환
            builder.and(member.username.eq(param.getUsername()));
        }
        if (StringUtils.hasText(param.getTeamName())) {    //StringUtils.hasText : 값이 있을때는 true 반환, 값이 없거나 null이면 false 반환
            builder.and(team.name.eq(param.getTeamName()));
        }
        if (param.getAgeGoe() != null) {
            builder.and(member.age.goe(param.getAgeGoe()));
        }
        if (param.getAgeLoe() != null) {
            builder.and(member.age.loe(param.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(       //@QueryProjection 사용으로 QMemberTeamDto 객체 사용 가능!!!
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName"))
                )
                .from(member)
                .leftJoin(member.team, team)  //member의 team 과 team을 join
                .where(builder)               //동적쿼리(BooleanBuilder) 사용
                .fetch();
    }





}