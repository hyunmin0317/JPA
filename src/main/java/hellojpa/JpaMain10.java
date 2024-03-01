package hellojpa;

import hellojpa.entity.Member5;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMain10 {

    public static void main(String[] args) {
        test1();
    }

    // JPQL
    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<Member5> result = em.createQuery(
                    "select m From MBR5 m where m.name like '%kim%'",
                    Member5.class
            ).getResultList();
            result.forEach(System.out::println);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
