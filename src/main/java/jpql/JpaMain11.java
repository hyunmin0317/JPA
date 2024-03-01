package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpql.entity.Member;
import jpql.entity.Team;

import java.util.Collection;
import java.util.List;

public class JpaMain11 {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            String query1 = "select m.username from JPQL_MEMBER m";
            List<String> result1 = em.createQuery(query1, String.class).getResultList();
            result1.forEach(System.out::println);

            String query2 = "select m.team from JPQL_MEMBER m";
            List<Team> result2 = em.createQuery(query2, Team.class).getResultList();
            result2.forEach(System.out::println);

            String query3 = "select t.members from JPQL_TEAM t";
            List<Collection> result3 = em.createQuery(query3, Collection.class).getResultList();
            System.out.println(result3);

            String query4 = "select m.username from JPQL_TEAM t join t.members m";
            List<String> result4 = em.createQuery(query4, String.class).getResultList();
            result4.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
