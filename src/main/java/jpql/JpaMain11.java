package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpql.entity.Member;
import jpql.entity.Team;

import java.util.Collection;
import java.util.List;

public class JpaMain11 {

    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
        test6();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            String query1 = "select m.username from JPQL_MEMBER m";
            List<String> result1 = em.createQuery(query1, String.class).getResultList();
            result1.forEach(System.out::println);

            String query2 = "select m.team from JPQL_MEMBER m";
            List<Team> result2 = em.createQuery(query2, Team.class).getResultList();
            result2.forEach(System.out::println);

            String query3 = "select t.members from JPQL_TEAM t";
            List<Collection> result3 = em.createQuery(query3, Collection.class).getResultList();
            System.out.println(result3);

            String query4 = "select m.username from JPQL_TEAM t join t.members m";
            List<String> result4 = em.createQuery(query4, String.class).getResultList();
            result4.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
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
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query1 = "select m from JPQL_MEMBER m";
            List<Member> result1 = em.createQuery(query1, Member.class).getResultList();
            result1.forEach(m -> {
                // 지연 로딩
                System.out.println("member = " + member3.getUsername() + ", " + member3.getTeam().getName());
                // 회원1, 팀A (SQL)
                // 회원2, 팀A (1차 캐시)
                // 회원3, 팀B (SQL)
                // 회원 100명 -> N + 1
            });

            String query2 = "select m from JPQL_MEMBER m join fetch m.team";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();
            result2.forEach(m -> {
                // 즉시 로딩 (fetch)
                System.out.println("member = " + member3.getUsername() + ", " + member3.getTeam().getName());
            });

            String query3 = "select t from JPQL_TEAM t join fetch t.members";
            List<Team> result3 = em.createQuery(query3, Team.class).getResultList();
            result3.forEach(team -> {
                System.out.println("team = " + team.getName() + " | memebers = " + team.getMembers().size());
                team.getMembers().forEach(member -> System.out.println("-> member = " + member));
            });

            String query4 = "select distinct t from JPQL_TEAM t join fetch t.members";
            List<Team> result4 = em.createQuery(query4, Team.class).getResultList();
            result4.forEach(team -> {
                System.out.println("team = " + team.getName() + " | memebers = " + team.getMembers().size());
                team.getMembers().forEach(member -> System.out.println("-> member = " + member));
            });

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
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
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // 페치 조인에서 페이지네이션 -> 메모리에서 페이징 (매우 위험)
            String query1 = "select t from JPQL_TEAM t join fetch t.members";
            List<Team> result1 = em.createQuery(query1, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();
            result1.forEach(team -> System.out.println("team = " + team.getName() + " | memebers = " + team.getMembers().size()));

            String query2 = "select t from JPQL_TEAM t";
            List<Team> result2 = em.createQuery(query2, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            result2.forEach(team -> {
                System.out.println("team = " + team.getName() + " | memebers = " + team.getMembers().size());
                team.getMembers().forEach(member -> System.out.println("-> member = " + member));
            });

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
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
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query1 = "select m from JPQL_MEMBER m where m = :member";
            List<Member> result1 = em.createQuery(query1, Member.class)
                    .setParameter("member", member1)
                    .getResultList();
            System.out.println(result1);

            String query2 = "select m from JPQL_MEMBER m where m.id = :memberId";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getResultList();
            System.out.println(result2);

            String query3 = "select m from JPQL_MEMBER m where m.team = :team";
            List<Member> result3 = em.createQuery(query3, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();
            System.out.println(result3);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
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
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            List<Member> result1 = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();
            System.out.println(result1);

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
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            // flush 자동 호출 -> commit, query, flush
            int resultCount = em.createQuery("update JPQL_MEMBER m set m.age = 20").executeUpdate();
            System.out.println("resultCount = " + resultCount);

            Member findMember = em.find(Member.class, member1.getId());
            // member.getAge() = 0 (20 X)
            System.out.println("member.getAge() = " + findMember.getAge());

            em.clear();     // 영속성 컨텍스트 초기화 필요
            Member findMember2 = em.find(Member.class, member1.getId());
            // member.getAge() = 20
            System.out.println("member.getAge() = " + findMember2.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
