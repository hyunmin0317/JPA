package hellojpa;

import hellojpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain03 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
    }

    // 영속성 컨텍스트 개념
    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            // 비영속
            Member member = new Member(101L, "HelloJPA");

            // 영속
            System.out.println("=== BEFORE ===");
            em.persist(member);
            System.out.println("=== AFTER ===");

            // 영속성 컨텍스트에서 조회 (entityManager - 1차 캐시)
            Member findMember = em.find(Member.class, 101L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 영속성 엔티티의 동일성 보장
    public static void test2() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        Member findMember1 = em.find(Member.class, 101L);
        Member findMember2 = em.find(Member.class, 101L);
        System.out.println("result = " + (findMember1 == findMember2));

        em.close();
        emf.close();
    }

    // 엔티티 등록 - 트랜잭션을 지원하는 쓰기 지연
    public static void test3() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        // 엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.
        tx.begin();     // [트랜잭션] 시작
        try {
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            em.persist(member1);
            em.persist(member2);
            // 여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
            System.out.println("===============");

            // 커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
            tx.commit();    // [트랜잭션] 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 엔티티 수정 - 변경 감지
    public static void test4() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();     // [트랜잭션] 시작
        try {
            // 영속 엔티티 조회
            Member member = em.find(Member.class, 150L);

            // 영속 엔티티 데이터 수정
            member.setName("ZZZZZ");
//            em.update(member);    이런 코드가 있어야 하지 않을까? -> X
            tx.commit();    // [트랜잭션] 커밋
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 플러시 발생
    public static void test5() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member(200L, "member200");
            em.persist(member);

            // 플러시 직접 호출
            em.flush();

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
