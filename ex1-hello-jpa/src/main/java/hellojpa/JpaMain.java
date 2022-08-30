package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
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
            Address address = new Address("city", "street", "10000");

            Member member = new Member();
            member.setUserName("member1");
            member.setHomeAddress(address);
            em.persist(member);

            Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode());
            //member.setHomeAddress(newAddress);



//            Address copyAdress =  new Address(address.getCity(), address.getStreet(), address.getZipcode());
//
//            Member member2 = new Member();
//            member2.setUserName("member2");
//            member2.setHomeAddress(copyAdress);
//            em.persist(member2);
//
//
//            member.getHomeAddress().setCity("new City");

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
