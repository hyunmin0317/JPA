package jpql;

import jakarta.persistence.*;
import jpql.entity.Member;

import java.util.List;

public class JpaMain10 {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from JPQL_MEMBER m", Member.class);
            Query query2 = em.createQuery("select m.username, m.age from JPQL_MEMBER m");

            List<Member> resultList = query1.getResultList();
            resultList.forEach(System.out::println);

            Member result;
            try {
                result = query1.getSingleResult();
            } catch (NoResultException e) {
                result = null;
            }
            System.out.println(result);

            Member query3 = em.createQuery("select m from JPQL_MEMBER m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println(query3.getUsername());

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
