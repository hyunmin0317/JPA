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
        test2();
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

    public static void test2() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Address address = new Address("city", "street", "10000");
            Member4 member1 = Member4.builder()
                    .name("member1")
                    .homeAddress(address)
                    .build();
            em.persist(member1);

            Member4 member2 = Member4.builder()
                    .name("member2")
                    .homeAddress(address)
                    .build();
            em.persist(member2);

            // 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함 -> 부작용 발생
//            member1.getHomeAddress().setCity("newCity");

            // 대신 값(인스턴스)를 복사해서 사용
            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
            Member4 member3 = Member4.builder()
                    .name("member3")
                    .homeAddress(copyAddress)
                    .build();
            em.persist(member3);

//            member3.getHomeAddress().setCity("oldCity");

            // 불변 객체 -> 부작용 원천 차단
            // 생성 시점 이후 절대 값을 변경할 수 없는 객체 -> 생성자로만 값을 설정, Setter X 또는 private 으로
            Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
            member1.setHomeAddress(newAddress);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
