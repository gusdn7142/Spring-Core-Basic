package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {

        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 획득
        EntityTransaction tx = em.getTransaction();
        tx.begin();  // 트랜잭션 시작

        try {

            Member member = new Member();
            member.setUserName("member1");
            em.persist(member);



//            //Criteria 사용 준비
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Member> query = cb.createQuery(Member.class);
//
//            //루트 클래스 (조회를 시작할 클래스)
//            Root<Member> m = query.from(Member.class);
//
//            //쿼리 생성 CriteriaQuery<Member> cq =
//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("userName"), "kim"));
//            List<Member> resultList = em.createQuery(cq).getResultList();


            //플러시가 호출될떄 : commit, query 발생시 동작
            List<String> usernameList = em.createNativeQuery("SELECT USERNAME FROM MEMBER").getResultList();
            for (String username : usernameList) {
                System.out.println("username = " + username);
            }



            tx.commit();  // 트랜잭션 커밋

        }
        catch (Exception ex){
            tx.rollback();  // 에러 발생 시 트랜잭션 롤백
            ex.printStackTrace();
        }
        finally {
            em.close();  // 엔티티 매니저 종료
        }
        emf.close();  // 엔티티 매니저 팩토리 종료

        //System.out.println(1);
    }

//    private static void printMember(Member member){
//        System.out.println("member = " + member.getUserName());
//    }
//
//    private static void printMemberAndTeam(Member member){
//        String username = member.getUserName();
//        System.out.println("username = " + username);
//
//        Team team = member.getTeam();
//        System.out.println(team.getName());
//    }


}
