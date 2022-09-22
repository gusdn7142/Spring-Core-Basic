package study.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import study.querydsl.dto.MemberSearchParam;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hibernate.internal.util.StringHelper.isEmpty;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);   //JPAQueryFactory() 객체 싱글톤 생성
    }

    @Override
    //Where절에 메서드(파라미터)를 사용해 member 객체를 조회하는 테스트
    //회원명, 팀명, 나이(ageGoe, ageLoe)
    public List<MemberTeamDto> search(MemberSearchParam param) {

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)           //member의 team 과 team을 join
                .where(usernameEq(param.getUsername()),  //동적쿼리 사용을 위해 메서드 사용
                        teamNameEq(param.getTeamName()),  //where는 null이 파라미터로 올 경우 조건문에서 제외
                        ageGoe(param.getAgeGoe()),
                        ageLoe(param.getAgeLoe()))
                .fetch();
    }

    //uesrname 필드 '==' 조건 함수
    private BooleanExpression usernameEq(String username) {
        return isEmpty(username) ? null : member.username.eq(username);
    }

    //teamName 필드 '==' 조건 함수
    private BooleanExpression teamNameEq(String teamName) {
        return isEmpty(teamName) ? null : team.name.eq(teamName);
    }

    //age 필드 '>'조건 함수
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }

    //age 필드 '<'조건 함수
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }



}