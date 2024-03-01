package hellojpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hellojpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

import static hellojpa.entity.QMember.member;

public class JpaMain10 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
    }

    // JPQL
    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<Member> result = em.createQuery(
                    "select m From MBR m where m.name like '%kim%'",
                    Member.class
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
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m);

            String name = "choi";
            if (name != null)
                cq = cq.where(cb.equal(m.get("name"), "kim"));

            List<Member> result = em.createQuery(cq).getResultList();
            result.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // QueryDsl
    public static void test3() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<Member> result = queryFactory
                    .select(member)
                    .from(member)
                    .where(member.name.like("kim"))
                    .orderBy(member.id.desc())
                    .fetch();
            result.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    // 네이티브 SQL
    public static void test4() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member(1L, "member1");
            em.persist(member);

            // flush -> commit, query
            List<Member> members = em.createNativeQuery("SELECT * FROM MBR", Member.class).getResultList();
            members.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
