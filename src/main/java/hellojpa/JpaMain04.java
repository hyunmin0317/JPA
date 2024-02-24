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
                    .username("A")
                    .roleType(RoleType.USER)
                    .build();
            // IDENTITY 전략 -> persist 시점에 즉시 INSERT 시행
            System.out.println("===============");
            em.persist(user);
            System.out.println("user.id = " + user.getId());
            System.out.println("===============");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
