package hellojpa;

import hellojpa.entity.Address;
import hellojpa.entity.Member4;
import hellojpa.entity.Period;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain09 {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member4 member = Member4.builder()
                    .name("hello")
                    .homeAddress(new Address("city", "street", "10000"))
                    .workPeriod(new Period())
                    .build();
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
