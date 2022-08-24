package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

            //member 엔티티 생성 후 DB에 등록
            Member member = new Member();
            member.setUserName("member1");
            //member.setTeam(team);
            em.persist(member);

            //team 엔티티 생성 후 DB에 등록
            Team team = new Team();
            team.setName("TeamA");
            team.getMembers().add(member);

            em.persist(team);

//            em.flush();
//            em.clear();

//            Team findTeam = em.find(Team.class, team.getId());
//            List<Member> members = findTeam.getMembers();
//
//            for (Member member1 : members) {
//                System.out.println("m = " + member.getUserName());
//            }

//            Member findMember = em.find(Member.class, member.getId());
//            List<Member> members = findMember.getTeam().getMembers();
//
//            for (Member member1 : members) {
//                System.out.println("m = " + member.getUserName());
//            }


//            System.out.println(findMember.getTeam().getName());

            //Team findTeam = em.find(Team.class,findMember.getTeamId());


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
