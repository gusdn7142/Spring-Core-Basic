package Jpql;

import com.sun.org.apache.xpath.internal.operations.Or;

import javax.persistence.*;
import java.util.Collection;
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

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member2");
            member3.setTeam(teamB);
            em.persist(member3);


            em.flush();
            em.clear();

            int resultCount = em.createQuery("update Member m set m.age = 20" )
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);




//            //String query = "select m from Member m where m.team = :team";
//            List<Member> members = em.createNamedQuery("Member.findByUserName",Member.class)
//                    .setParameter("username","member1")
//                    .getResultList();
//
//            for (Member member : members) {
//                System.out.println("member = " + member);
//            }


//            String query = "select t from Team t join fetch t.members";
//            List<Team> result = em.createQuery(query,Team.class)
//                    .getResultList();
//
//            for (Team team : result) {
//                System.out.println("team = " + team.getName() + "|" + team.getMembers().size());
//                for(Member member : team.getMembers()){
//                    System.out.println("member = " + member);
//                }
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
    }
}
