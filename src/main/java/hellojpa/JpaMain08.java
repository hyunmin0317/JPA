package hellojpa;

import hellojpa.entity.Child;
import hellojpa.entity.Member3;
import hellojpa.entity.Parent;
import hellojpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.hibernate.Hibernate;

import java.util.List;

public class JpaMain08 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team = Team.builder()
                    .name("TeamA")
                    .build();
            em.persist(team);

            Member3 member = Member3.builder()
                    .name("member1")
                    .team(team)
                    .build();
            em.persist(member);

            em.flush();
            em.clear();

            Member3 findMember = em.find(Member3.class, 1L);
            printMember(findMember);
            printMemberAndTeam(findMember);

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
            Member3 member = Member3.builder()
                    .name("hello")
                    .build();
            em.persist(member);

            em.flush();
            em.clear();

            Member3 findMember2 = em.getReference(Member3.class, 1L);
            System.out.println("before findMember = " + findMember2.getClass());    // class hellojpa.entity.Member3$HibernateProxy
            System.out.println("findMember.id = " + findMember2.getId());
            System.out.println("findMember.name = " + findMember2.getName());
            System.out.println("after findMember = " + findMember2.getClass());     // class hellojpa.entity.Member3$HibernateProxy

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void test3() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member3 member1 = Member3.builder()
                    .name("member1")
                    .build();
            em.persist(member1);

            Member3 member2 = Member3.builder()
                    .name("member2")
                    .build();
            em.persist(member2);

            em.flush();
            em.clear();

            Member3 m1 = em.find(Member3.class, member1.getId());
            Member3 m2 = em.getReference(Member3.class, member2.getId());

            System.out.println("m1 == m2: " + (m1.getClass() == m2.getClass()));
            System.out.println("m1 == m2: " + (m1 instanceof Member3));
            System.out.println("m1 == m2: " + (m2 instanceof Member3));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void test4() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member3 member1 = Member3.builder()
                    .name("member1")
                    .build();
            em.persist(member1);

            em.flush();
            em.clear();

            Member3 member = em.find(Member3.class, member1.getId());
            Member3 reference = em.getReference(Member3.class, member1.getId());
            System.out.println("member = " + member.getClass());
            System.out.println("reference = " + reference.getClass());
            System.out.println("a == a: " + (member == reference));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void test5() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member3 member1 = Member3.builder()
                    .name("member1")
                    .build();
            em.persist(member1);

            em.flush();
            em.clear();

            Member3 refMember = em.getReference(Member3.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass());      // Proxy
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));
            refMember.getName();
            Hibernate.initialize(refMember);    // 강제초기화
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

// org.hibernate.LazyInitializationException
//            em.detach(refMember);
//            em.clear();
//            em.close();
//            refMember.getName();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void test6() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team = Team.builder()
                    .name("TeamA")
                    .build();
            em.persist(team);

            Member3 member = Member3.builder()
                    .name("member1")
                    .team(team)
                    .build();
            em.persist(member);

            em.flush();
            em.clear();

            Member3 m = em.find(Member3.class, 1L);
            System.out.println("m = " + m.getTeam().getClass());

            System.out.println("============");
            m.getTeam().getName();
            System.out.println("============");

            // 즉시 로딩(EAGER) -> JPQL 에서 N+1 문제
            // 가급적 지연 로딩(LAZY) 만 사용
            List<Member3> members = em.createQuery("select m from MBR3 m", Member3.class)
                    .getResultList();
            members.forEach(mem -> System.out.println("mem = " + mem.getName()));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void test7() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            // CASCADE ALL 설정 시
            em.persist(parent);

            // CASCADE 설정 X
//            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void printMember(Member3 member) {
        String username = member.getName();
        System.out.println("username = " + username);
    }

    private static void printMemberAndTeam(Member3 member) {
        String username = member.getName();
        System.out.println("username = " + username);
        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
}
