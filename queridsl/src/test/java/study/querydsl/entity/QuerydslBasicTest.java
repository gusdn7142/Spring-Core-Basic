package study.querydsl.entity;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;


@SpringBootTest
@Transactional
@Commit
public class QuerydslBasicTest {

    @PersistenceUnit
    EntityManagerFactory emf;

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);    //엔티티 매니저로 JPAQueryFactory 객체 초기화

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

    }


    @Test
    public void startJPQL() {
        //JPQL로 member1 객체 조회 : 런타임 시점에 오류 확인 가능
        String qlString =
                "select m from Member m " +
                        "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");  //member.getUsername() 비교
    }


    @Test
    public void startQuerydsl() {

        //Querydsl로 member1 객체 조회 : 컴파일 시점에 오류 확인 가능, 파라미터 바인딩 필요 없음.
        //QMember m = new QMember("m1");  //m1 : 어떤 QMember인지를 구분하기 위한 별칭 부여,  같은 테이블을 조인해서 사용해야하는 경우 사용
        //QMember m = QMember.member;  => Static import를 통해 member로 변환 가능
                Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))   //JPQL과 달리 파라미터 바인딩이 필요 없음.
                .fetchOne();                            //한번 호출에 하나의 Row를 조회
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search(){
        //다중 조건으로 회원 객체 조회 (기본 검색 쿼리)
        Member findMember = queryFactory
                .selectFrom(member) //.select(member)과 .from(member)을 합침
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchAndParam(){
        //다중 조건으로 회원 객체 조회 (AND 조건을 파라미터로 처리)
        Member findMember = queryFactory
                .selectFrom(member) //.select(member)과 .from(member)을 합침
                .where(
                        member.username.eq("member1"),   //and와 똑같이 동작
                        member.age.eq(10)
                )
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch(){

//        //fetch() : Member 객체 다수 조회
//        List<Member> fetch = queryFactory
//                .selectFrom(member)
//                .fetch();

        //fetchOne() : Member 객체 단건 조회
//        Member fetchOne = queryFactory
//                .selectFrom(member)
//                .fetchOne();

//        //fetchFirst() : member 객체 처음 한건 조회
//        Member fetchFirst = queryFactory
//                .selectFrom(member)
//                .fetchFirst();      //limit(1).fetchOne()와 같다.

//        //fetchResults() : count(member)를 가져오는 쿼리와 모든 member 정보를 조회하는 쿼리 발생
//        QueryResults<Member> results = queryFactory
//                .selectFrom(member)
//                .fetchResults();  //count(member)를 가져오는 쿼리와 모든 member 정보를 조회하는 쿼리 발생  (select문 총 2개)
////
//        //results.getTotal();
//        //List<Member> content = results.getResults();
//
//
        //fetchCount() : 카운트 쿼리 발생
        long total = queryFactory
                .selectFrom(member)
                .fetchCount();

    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 : 내림차순(desc)
     * 2. 회원 이름 : 올림차순 (asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))   //age가 100인 레코드 조회
                .orderBy(member.age.desc(), member.username.asc().nullsLast())  //정렬 : 나이는 내림차순, 유저이름은 오름차순으로 정렬
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    /**
     * 페이징
     * 1. 조회 건수 제한
     */
    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)    //0부터 시작    (zero index)
                .limit(2)     //최대 2건 조회 (size)
                .fetch();
        assertThat(result.size()).isEqualTo(2);
    }


    /**
     * 페이징
     * 2. 조회 건수 제한 + 전체 조회 수도 확인
     */
    @Test
    public void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)    //0부터 시작    (zero index)
                .limit(2)     //최대 2건 조회 (size)
                .fetchResults();
        assertThat(queryResults.getTotal()).isEqualTo(4);   //전체 레코드 개수 (count)
        assertThat(queryResults.getLimit()).isEqualTo(2);   //offset은 1, size는 2이기 때문에  idx가 5번, 4번인 레코드가 출력됨
        assertThat(queryResults.getOffset()).isEqualTo(1);  //offset은 1
        assertThat(queryResults.getResults().size()).isEqualTo(2);   //idx가 5번, 4번 2개이기 때문에 size도 2이다.
    }


    /**
     * JPQL
     * select
     * COUNT(m), //회원수
     * SUM(m.age), //나이 합
     * AVG(m.age), //평균 나이
     * MAX(m.age), //최대 나이
     * MIN(m.age) //최소 나이
     * from Member m
     */
    @Test
    public void aggregation() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.count(),      //회원 레코드 수
                        member.age.sum(),   //나이 총합
                        member.age.avg(),   //평균 나이
                        member.age.max(),    //최대 나이
                        member.age.min())   //최소 나이
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }


    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     */
    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)   //join(target, alias)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }




    /**
     * 기본 조인 : 팀 A에 소속된 모든 회원
     */
    @Test
    public void join() throws Exception {
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)           //join(조인 대상, 별칭으로 사용할 Q타입)
                .where(team.name.eq("teamA")) //team의 이름이 "teamA"인 레코드만 조회
                .fetch();

        assertThat(result)
                .extracting("username")    //검증할 필드 지정
                .containsExactly("member1", "member2"); //containsExactly: 순서를 포함해서 원소가 정확히 일치하는지 확인
    }

    /**
     * 세타 조인 (연관관계가 없는 필드로 조인)
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));  //member 객체 생성시 이름을 임의로 "teamA"로 생성
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")   //검증할 필드 지정
                .containsExactly("teamA", "teamB");   //containsExactly: 순서를 포함해서 원소가 정확히 일치하는지 확인

    }



    /**
     * 1. 조인 대상 필터링
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'teamA'
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID=t.id and t.name='teamA'
     */
    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                //.where(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }


    /**
     * 2. 연관관계 없는 엔티티 외부 조인
     * 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
     * JPQL: SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name
     * SQL: SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
     */
    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))  //leftJoin의 대상이 member.team이 아닌 team이기 때문에  leftOuterJoin()으로 바뀌어서 이제 id로 join 되지 않고 on 절로만 join된다.
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("t=" + tuple);
        }
    }

    /**
     * 페치 조인 미적용
     */
    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();


        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }


    /**
     * 페치 조인 적용
     */
    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }


    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");  //서브쿼리에서 사용하기 위해 생성

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())   //서브쿼리 (나이가 가장 많은 사람의 나이 조회)
                                .from(memberSub)
                ))
                .fetch();


        assertThat(result).extracting("age").containsExactly(40);
    }



    /**
     * 나이가 평균 나이 이상인 회원
     */
    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");  //서브쿼리에서 사용하기 위해 생성

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(  //goe : >=
                        JPAExpressions
                                .select(memberSub.age.avg())   //서브쿼리 (평균 나이 조회)
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age").containsExactly(30,40);
    }



    /**
     * 서브쿼리 여러 건 처리, in 사용
     */
    @Test
    public void subQueryIn() throws Exception {
        QMember memberSub = new QMember("memberSub");  //서브쿼리에서 사용하기 위해 생성

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(    //== 여러개
                        JPAExpressions
                                .select(memberSub.age)     //서브쿼리 (나이가 10보다 큰 회원의 나이 조회)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))   // >
                ))
                .fetch();

        assertThat(result).extracting("age").containsExactly(20, 30, 40);
    }


    /**
     * select 절에 subquery
     */
    @Test
    public void selectSubQueryIn() throws Exception {
        QMember memberSub = new QMember("memberSub");  //서브쿼리에서 사용하기 위해 생성

        List<Tuple> fetch = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())   //서브 쿼리 (평균 나이 조회)
                                .from(memberSub)
                ).from(member)
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("username = " + tuple.get(member.username));   //이름 출력
            System.out.println("age = " +
                    tuple.get(JPAExpressions.select(memberSub.age.avg())   //평균 나이 출력
                            .from(memberSub)));
        }
    }



    /**
     * Case 문 - 단순한 조건
     */
    @Test
    public void basicCase(){

        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }

    }



    /**
     * Case 문 - 복잡한 조건
     */
    @Test
    public void Case2(){

        List<String> result = queryFactory
                .select(new CaseBuilder()      //case-when-then-else
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }


    }


    /**
     * 상수 사용
     */
    @Test
    public void constant(){

        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

    }


    /**
     * 문자 더하기
     */
    @Test
    public void concat(){

        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))  //age를 문자열 타입으로 변환
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }


    /**
     * 프로젝션과 결과 반환 - 프로젝션 대상이 하나
     */
    @Test
    public void simpleProjection(){

        List<String> result = queryFactory
                .select(member.username)   //조회 대상
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }



    /**
     * 프로젝션과 결과 반환 - 프로젝션 대상이 둘  (튜플로 조회)
     */
    @Test
    public void tupleProjection(){

        List<Tuple> result = queryFactory   //
                .select(member.username, member.age)
                .from(member)
                .fetch();


        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            System.out.println("username=" + username);
            System.out.println("age=" + age);
        }


    }


    /**
     * 순수 JPA에서 DTO 조회
     */
    @Test
    public void findDtoByJPQL() {

        List<MemberDto> result = em.createQuery(
                        "select new study.querydsl.dto.MemberDto(m.username, m.age) " +    //DTO 사용시에는 new키워드 사용 필요!!
                                "from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }



    /**
     * Querydsl에서 DTO 조회 - 프로퍼티 접근(Setter)
     */
    @Test
    public void findDtoBySetter() {

        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,    //Setter (타입, 꺼내올 값들)..   기본과 인자있는 생성자 필요
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }



    /**
     * Querydsl에서 DTO 조회 - 필드 직접 접근
     */
    @Test
    public void findDtoByField() {

        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,    //Dto 필드에 직접 대입
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }



    /**
     * Querydsl에서 DTO 조회 - 생성자 사용
     */
    @Test
    public void findDtoByConstructor() {

        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,    //생성자로 대입...  UserDto로도 가능... 타입을 보고 매핑되기 떄문!!!
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }





    /**
     * Querydsl에서 DTO 조회 - 별칭(Alias)이 다를 때
     */
    @Test
    public void findUserDto() {
        QMember memberSub = new QMember("memberSub");

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,    //DTO 필드에 직접 대입
                        //member.username.as("name"),
                        ExpressionUtils.as(member.username,"name"),
                        member.age))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("UserDto = " + userDto);
        }

    }




    /**
     * Querydsl에서 DTO 조회 - 생성자(+@QueryProjection) 활용
     */
    @Test
    public void findByQueryProjection(){

        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }




    /**
     * Querydsl에서 동적쿼리 사용 - BooleanBuilder
     */
    @Test
    public void 동적쿼리_BooleanBuilder() throws Exception {

        String usernameParam = "member1";  //uerename 파라미터
        Integer ageParam = 10;             //age 파라미터

        List<Member> result = searchMember1(usernameParam, ageParam);  //조건에 따른 member 조회 함수
        Assertions.assertThat(result.size()).isEqualTo(1);   //Member객체의 사이즈 (레코드 수)


    }

    //조건에 따른 member 조회 함수
    private List<Member> searchMember1(String usernameParam, Integer ageParam) {

        BooleanBuilder builder = new BooleanBuilder();   //where절 초기값 부여도 가능 : new BooleanBuilder(member.username.eq(usernameCond))

        if (usernameParam != null) {                         //usernameCond null이 아니면
            builder.and(member.username.eq(usernameParam));  //username 필드가 usernameCond와 같은지 비교
        }
        if (ageParam != null) {                             //ageCond null이 아니면
            builder.and(member.age.eq(ageParam));            //age 필드가 ageCond와 같은지 비교
        }
        return queryFactory
                .selectFrom(member)   //member 객체 조회
                .where(builder)       //builder에 포함된 where절이 들어감. (ex, username필드와 age 필드)... bulider 다음에 and로 조건 추가도 가능!!!
                .fetch();
    }




    /**
     * Querydsl에서 동적쿼리 사용 - Where 다중 파라미터 사용
     */
    @Test
    public void 동적쿼리_WhereParam() throws Exception {

        String usernameParam = "member1";
        Integer ageParam = 10;


        List<Member> result = searchMember2(usernameParam, ageParam);  //조건에 따른 member 조회 함수
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    //조건에 따른 member 조회 함수
    private List<Member> searchMember2(String usernameParam, Integer ageParam) {

        return queryFactory       //member 객체 조회
                .selectFrom(member)
                .where(usernameEq(usernameParam), ageEq(ageParam))
                //.where(allEq(usernameParam, ageParam))
                .fetch();
    }

    //where절에 사용할 username 필드 비교 메서드
    private BooleanExpression usernameEq(String usernameParam) {
        //if(usernameParam == null){
          //  return null;
        //}
        //return member.username.eq(usernameParam)
        return usernameParam != null ? member.username.eq(usernameParam) : null;   //null일 경우를 고려한 삼항연산
    }

    //where절에 사용할 age 필드 비교 메서드
    private BooleanExpression ageEq(Integer ageParam) {
        //if(ageParam == null){
        //  return null;
        //}
        //return member.age.eq(ageParam)
        return ageParam != null ? member.age.eq(ageParam) : null;   //null일 경우를 고려한 삼항연산
    }

    //where절에 사용할 username 필드와  age 필드 비교 조합 메서드....  조합 가능!!!
    //private BooleanExpression allEq(String usernameParam, Integer ageParam) {
    //    return usernameEq(usernameParam).and(ageEq(ageParam));
    //}





    /**
     * Querydsl에서 수정, 삭제 벌크 연산 - 쿼리 한번으로 대량 데이터 수정
     */
    @Test
    public void bulkUpdate(){

        //영속성 컨텍스트 값
        //member1.age = 10 -> DB member1
        //member2.age = 20 -> DB member2
        //member3.age = 30 -> DB member3
        //member4.age = 40 -> DB member4

        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")   //username을 "비회원"으로 변경
                .where(member.age.lt(28))   //age<28이면
                .execute();

        //영속성 컨텍스트 값       //Update 쿼리시의 변경사항을 영속성 컨텍스트에 저장하지 않는다!!!
        //member1.age = 10 -> DB member1
        //member2.age = 20 -> DB member2
        //member3.age = 30 -> DB member3
        //member4.age = 40 -> DB member4

        em.flush();
        em.clear();

        List<Member> result = queryFactory   //member 객체 조회시 이미 member객체가 영속성 컨텍스트에 있으면 DB에서 조회한 값을 버린다.
               .selectFrom(member)
                .fetch();

        for (Member member1 : result) {
            System.out.println("member1 = " + member1);  //member 객체 출력
        }

    }



    /**
     * Querydsl에서 수정, 삭제 벌크 연산 - 기존 숫자에 1 더하기
     */
    @Test
    public void bulkAdd() {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))   // member.age.multiply(x) : 곱하기
                .execute();
    }


    /**
     * Querydsl에서 수정, 삭제 벌크 연산 - 쿼리 한번으로 대량 데이터 삭제
     */
    @Test
    public void bulkDelete() {

        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))  //age>18
                .execute();

    }


    /**
     * Querydsl에서 SQL function 호출 - username 필드의 문자열을 다른 문자열로 변경하여 조회
     */
    @Test
    public void sqlFunction() {

        List<String> result = queryFactory
                .select(Expressions.stringTemplate(   //username에서 "member" 문자열을 M으로 변경
                        "function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetch();

        for(String s : result) {
            System.out.println("s = " + s);  //M1.. M2.. M3.. M4..
        }
    }


    /**
     * Querydsl에서 SQL function 호출 - 소문자인 username만 비교하여 username필드 조회
     */
    @Test
    public void sqlFunction2() {

        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(Expressions.stringTemplate(
                        "function('lower', {0})",
                        member.username)))
                //.where(member.username.eq(member.username.lower()))  //upper는 대문자!!
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }


    }







}