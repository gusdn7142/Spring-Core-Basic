package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import study.querydsl.dto.MemberSearchParam;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hibernate.internal.util.StringHelper.isEmpty;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

public class MemberRepositoryImpl implements MemberRepositoryCustom {  //extends QuerydslRepositorySupportextends QuerydslRepositorySupport



    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);   //JPAQueryFactory() 객체 싱글톤 생성
    }


//    public MemberRepositoryImpl(EntityManager em) {  //Class<?> domainClass
//        super(Member.class);  //domainClass
//        this.queryFactory = new JPAQueryFactory(em);   //JPAQueryFactory() 객체 싱글톤 생성
//    }


    @Override
    //Where절에 메서드(파라미터)를 사용해 member 객체를 조회
    //회원명, 팀명, 나이(ageGoe, ageLoe)
    public List<MemberTeamDto> search(MemberSearchParam param) {

//        return from(member)
//                .leftJoin(member.team, team)
//                .where(usernameEq(param.getUsername()),  //동적쿼리 사용을 위해 메서드 사용
//                        teamNameEq(param.getTeamName()),  //where는 null이 파라미터로 올 경우 조건문에서 제외
//                        ageGoe(param.getAgeGoe()),
//                        ageLoe(param.getAgeLoe()))
//                .select(new QMemberTeamDto(
//                        member.id,
//                        member.username,
//                        member.age,
//                        team.id,
//                        team.name))
//                .fetch();

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




    /**
     * 단순한 페이징, fetchResults() 사용 : 페이징된 데이터 내용과 전체 카운트를 한번에 조회
     */
    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchParam param,
                                                Pageable pageable) {

        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)                //member의 team 과 team을 join
                .where(usernameEq(param.getUsername()),     //동적쿼리 사용을 위해 메서드 사용...  null이 파라미터로 올 경우 조건문에서 제외
                        teamNameEq(param.getTeamName()),
                        ageGoe(param.getAgeGoe()),
                        ageLoe(param.getAgeLoe()))
                .offset(pageable.getOffset())     //페이징 오프셋 설정
                .limit(pageable.getPageSize())    //페이징 사이즈 설정
                .fetchResults();                 //카운트 쿼리문과  데이터 조회쿼리를 날림.


        List<MemberTeamDto> content = results.getResults();   //member 객체 조회
        long total = results.getTotal();                       //count 수 조회

        return new PageImpl<>(content, pageable, total);   //member 객체, pageable, count 수 조회...   PageImpl은 Page의 구현체이다
    }



//    /**
//     * 단순한 페이징, fetchResults() 사용 : 페이징된 데이터 내용과 전체 카운트를 한번에 조회
//     */
//    //@Override
//    public Page<MemberTeamDto> searchPageSimple2(MemberSearchParam param,
//                                                Pageable pageable) {
//
//        JPQLQuery<MemberTeamDto> jpqlQuery = from(member)
//                .leftJoin(member.team, team)                //member의 team 과 team을 join
//                .where(usernameEq(param.getUsername()),     //동적쿼리 사용을 위해 메서드 사용...  null이 파라미터로 올 경우 조건문에서 제외
//                        teamNameEq(param.getTeamName()),
//                        ageGoe(param.getAgeGoe()),
//                        ageLoe(param.getAgeLoe()))
//                .select(new QMemberTeamDto(
//                        member.id,
//                        member.username,
//                        member.age,
//                        team.id,
//                        team.name));
//                //.offset(pageable.getOffset())     //페이징 오프셋 설정
//                //.limit(pageable.getPageSize())    //페이징 사이즈 설정
//
//        JPQLQuery<MemberTeamDto> query = getQuerydsl().applyPagination(pageable, jpqlQuery);
//
//        List<MemberTeamDto> content = query.fetchResults().getResults();   //member 객체 조회
//        long total = query.fetchResults().getTotal();                       //count 수 조회
//
//        return new PageImpl<>(content, pageable, total);   //member 객체, pageable, count 수 조회...   PageImpl은 Page의 구현체이다
//    }






    /**
     * 복잡한 페이징 - fetch()와 fetchCount() 사용 : 페이징된 데이터 내용과 전체 카운트를 별도로 조회
     */
    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchParam param,
                                                 Pageable pageable) {

        //데이터 조회
        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)                //member의 team 과 team을 join
                .where(usernameEq(param.getUsername()),     //동적쿼리 사용을 위해 메서드 사용...  null이 파라미터로 올 경우 조건문에서 제외
                        teamNameEq(param.getTeamName()),
                        ageGoe(param.getAgeGoe()),
                        ageLoe(param.getAgeLoe()))
                .offset(pageable.getOffset())     //페이징 오프셋 설정
                .limit(pageable.getPageSize())    //페이징 사이즈 설정
                .fetch();                         //데이터 다수 조회

        //카운트 조회
        JPAQuery<Member> countQuery = queryFactory    //long total
                .select(member)
                .from(member)
                .leftJoin(member.team, team)                //member의 team 과 team을 join
                .where(usernameEq(param.getUsername()),     //동적쿼리 사용을 위해 메서드 사용...  null이 파라미터로 올 경우 조건문에서 제외
                        teamNameEq(param.getTeamName()),
                        ageGoe(param.getAgeGoe()),
                        ageLoe(param.getAgeLoe()));
                //.fetchCount();                  //카운트 수 조회

        //return new PageImpl<>(content, pageable, total);   //member 객체, pageable, count 수 조회...   PageImpl은 Page의 구현체이다
        return PageableExecutionUtils.getPage(content, pageable,  countQuery::fetchCount);  //() ->countQuery.fetchCount()


    }





}