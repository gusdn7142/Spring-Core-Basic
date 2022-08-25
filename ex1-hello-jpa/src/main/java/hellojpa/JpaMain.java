package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

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
            member.setUserName("user1");
            member.setCreatedBy("kim");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);

            em.flush();
            em.clear();



            tx.commit();  // 트랜잭션 커밋

        }
        catch (Exception ex){
            tx.rollback();  // 에러 발생 시 트랜잭션 롤백
        }
        finally {
            em.close();  // 엔티티 매니저 종료
        }
        emf.close();  // 엔티티 매니저 팩토리 종료

        //System.out.println(1);
    }


}
