package hellojpa;

import hellojpa.entity.RoleType;
import hellojpa.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain04 {

    public static void main(String[] args) {
        test1();
    }

    // Enumerated 설정 테스트
    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            User user = User.builder()
                    .id(1L)
                    .username("A")
                    .roleType(RoleType.ADMIN)
                    .build();
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
