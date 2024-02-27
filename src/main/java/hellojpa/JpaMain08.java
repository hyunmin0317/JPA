package hellojpa;

import hellojpa.entity.Member3;
import hellojpa.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain08 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
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

            em.detach(refMember);
            em.clear();
            em.close();

            refMember.getName();        // org.hibernate.LazyInitializationException
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
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
