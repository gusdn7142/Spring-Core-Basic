package hellojpa;

import org.hibernate.Hibernate;

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

//            Member member = new Member();
//            member.setUserName("user1");
//            member.setCreatedBy("kim");
//            member.setCreatedDate(LocalDateTime.now());
//
//            em.persist(member);
//
//            em.flush();
//            em.clear();



//            Member member = em.find(Member.class,1L);
//            //printMemberAndTeam(member);
//            printMember(member);


            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUserName("member1");
            member1.setTeam(team);
            em.persist(member1);


            Member member2 = new Member();
            member2.setUserName("member2");
            member2.setTeam(teamB);
            em.persist(member2);

            em.flush();  //영속성 컨텍스트 비우기
            em.clear();

//            Member m = em.find(Member.class, member1.getId());
            List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();


//            System.out.println("m = " + m.getTeam().getClass());
//            System.out.println(m.getTeam().getName());


//            Member refMember = em.getReference(Member.class, member1.getId());  //프록시 객체
//            System.out.println("refMember = " + (refMember.getClass()));   //Proxy
//            Hibernate.initialize(refMember);  //프록시 강제 초기화
            //em.detach(refMember); //준영속상태로 전환
            //em.close()          //영속성 컨텍스트 닫기
            //em.clear

//            System.out.println("refMember.username = " + refMember.getUserName());
//            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));




//            Member refMember = em.getReference(Member.class, member1.getId());
//            System.out.println("refMember = " + (refMember.getClass()));   //Proxy
//
//            Member findMember = em.find(Member.class, member1.getId());
//            System.out.println("findMember = " + (findMember.getClass()));  //Member
//
//            System.out.println("refMember == findMember: " + (refMember == findMember));

            //
//            Member findMember = em.find(Member.class, member.getId());
//            Member findMember = em.getReference(Member.class, member.getId());
//            System.out.println("findMember = " + findMember.getClass());
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.username = " + findMember.getUserName());

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
