package hellojpa;

import hellojpa.entity.Member3;
import hellojpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain08 {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team = Team.builder()
                    .name("TeamA")
                    .build();
            em.persist(team);

            Member3 member = Member3.builder()
                    .name("member1")
                    .team(team)
                    .build();
            em.persist(member);

            em.flush();
            em.clear();

            Member3 findMember = em.find(Member3.class, 1L);
            printMember(findMember);
            printMemberAndTeam(findMember);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void test2() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member3 member = Member3.builder()
                    .name("hello")
                    .build();
            em.persist(member);

            em.flush();
            em.clear();

            Member3 findMember2 = em.getReference(Member3.class, 1L);
            System.out.println("findMember = " + findMember2.getClass());   // findMember = class hellojpa.entity.Member3$HibernateProxy$vHTFZnBj
            System.out.println("findMember.id = " + findMember2.getId());
            System.out.println("findMember.name = " + findMember2.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void printMember(Member3 member) {
        String username = member.getName();
        System.out.println("username = " + username);
    }

    private static void printMemberAndTeam(Member3 member) {
        String username = member.getName();
        System.out.println("username = " + username);
        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
}
