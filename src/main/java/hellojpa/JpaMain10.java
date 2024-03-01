package hellojpa;

import hellojpa.entity.Member5;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class JpaMain10 {

    public static void main(String[] args) {
        test1();
        test2();
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

    // Criteria 사용 준비
    public static void test2() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member5> query = cb.createQuery(Member5.class);

            Root<Member5> m = query.from(Member5.class);

            CriteriaQuery<Member5> cq = query.select(m);

            String name = "choi";
            if (name != null)
                cq = cq.where(cb.equal(m.get("name"), "kim"));

            List<Member5> result = em.createQuery(cq).getResultList();
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
