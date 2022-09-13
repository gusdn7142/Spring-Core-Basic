package study.querydsl.entity;


import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;


@SpringBootTest
@Transactional
@Commit
public class QuerydslBasicTest {


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











}