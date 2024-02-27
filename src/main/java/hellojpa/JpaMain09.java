package hellojpa;

import hellojpa.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class JpaMain09 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
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

    public static void test3() {
        int a = 10;
        int b = 10;
        System.out.println("a == b: " + (a == b));      // true

        Address address1 = new Address("city", "street", "10000");
        Address address2 = new Address("city", "street", "10000");
        System.out.println("address1 == address2: " + (address1 == address2));      // false
        System.out.println("address1 == address2: " + address1.equals(address2));   // true
    }

    public static void test4() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member5 member = new Member5();
            member.setName("member1");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");
            member.getAddressHistory().add(AddressEntity.builder().address(new Address("old1", "street", "10000")).build());
            member.getAddressHistory().add(AddressEntity.builder().address(new Address("old2", "street", "10000")).build());

            em.persist(member);

            em.flush();
            em.clear();

            // 임베디드 타입 -> 즉시 로딩 (EAGER)
            System.out.println("============= START =============");
            Member5 findMember = em.find(Member5.class, member.getId());

            // homeCity -> newCity
//            findMember.getHomeAddress().setCity("newCity");     // -> X
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));

            // 치킨 -> 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            findMember.getAddressHistory().remove(AddressEntity.builder().address(new Address("old1", "street", "10000")).build());
            findMember.getAddressHistory().add(AddressEntity.builder().address(new Address("newCity1", "street", "10000")).build());

            // 값 타입 컬렉션 -> 지연 로딩 (LAZY)
            List<AddressEntity> addressHistory = findMember.getAddressHistory();
            addressHistory.forEach(h -> System.out.println("address = " + h.getAddress().getCity()));

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            favoriteFoods.forEach(f -> System.out.println("favoriteFood = " + f));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
