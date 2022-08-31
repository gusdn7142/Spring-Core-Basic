package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
            member.setHomeAddress(new Address("homeCity","street","10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

//            member.getAddressHistory().add(new Address("old1","street","10000"));
//            member.getAddressHistory().add(new Address("old2","street","10000"));

            member.getAddressHistory().add(new AddressEntity("old1","street","10000"));
            member.getAddressHistory().add(new AddressEntity("old2","street","10000"));



            em.persist(member);


//            em.flush();
//            em.clear();
//
//
//            System.out.println("========== START =============");
            Member findMember = em.find(Member.class,member.getId());


            //findMember.getHomeAddress().setCity("newCity");  //(객체)값 타입은 생성자로 교체해야 한다.
//            Address address = findMember.getHomeAddress();
//            findMember.setHomeAddress(new Address("newCity",address.getStreet(),address.getZipcode()));  //


            //치킨 -> 한식
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");

//
//            findMember.getAddressHistory().remove(new Address("old1","street","10000"));   //equals 해시코드가 이떄 사용됨!!!
//            findMember.getAddressHistory().add(new Address("newCity", "street", " 100000"));



//            List<Address> addressHistory = findMember.getAddressHistory();
//
//            for (Address address : addressHistory) {
//                System.out.println("address = " + address.getCity());
//            }
//
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for (String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFood = " + favoriteFood);
//            }



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
