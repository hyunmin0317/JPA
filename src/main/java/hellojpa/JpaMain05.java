package hellojpa;

import hellojpa.entity.Member2;
import hellojpa.entity.Member3;
import hellojpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMain05 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
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

            Member2 member = Member2.builder()
                    .name("member1")
                    .teamId(team.getId())
                    .build();
            em.persist(member);

            Member2 findMember = em.find(Member2.class, member.getId());
            Long findTeamId = findMember.getTeamId();
            Team findTeam = em.find(Team.class, findTeamId);
            System.out.println("findTeam.id = " + findTeam.getId());
            System.out.println("findTeam.name = " + findTeam.getName());

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

            Member3 findMember = em.find(Member3.class, member.getId());
            List<Member3> members = findMember.getTeam().getMembers();
            members.forEach(m -> System.out.println("m = " + m.getName()));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void test3() {
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
                    .build();
            member.setTeam(team);
            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
