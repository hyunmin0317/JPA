package jpql;

import jakarta.persistence.*;
import jpql.dto.MemberDto;
import jpql.entity.Address;
import jpql.entity.Member;
import jpql.entity.MemberType;
import jpql.entity.Team;

import java.util.List;
import java.util.stream.IntStream;

public class JpaMain10 {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    public static void test1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from JPQL_MEMBER m", Member.class);
            Query query2 = em.createQuery("select m.username, m.age from JPQL_MEMBER m");

            List<Member> resultList = query1.getResultList();
            resultList.forEach(System.out::println);

            Member result;
            try {
                result = query1.getSingleResult();
            } catch (NoResultException e) {
                result = null;
            }
            System.out.println(result);

            Member query3 = em.createQuery("select m from JPQL_MEMBER m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println(query3.getUsername());

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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            // 엔티티 프로젝션
            List<Member> result1 = em.createQuery("select m from JPQL_MEMBER m", Member.class).getResultList();
            List<Team> result2 = em.createQuery("select m.team from JPQL_MEMBER m", Team.class).getResultList();
            List<Team> result3 = em.createQuery("select t from JPQL_MEMBER m join m.team t", Team.class).getResultList();

            // 임베디드 타입 프로젝션
            List<Address> result4 = em.createQuery("select o.address from JPQL_ORDERS o", Address.class).getResultList();

            // 스칼라 타입 프로젝션
            List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from JPQL_MEMBER m").getResultList();
            resultList.forEach(o -> {
                System.out.println("username = " + o[0]);
                System.out.println("age = " + o[1]);
            });

            List<MemberDto> resultList2 = em.createQuery("select new jpql.dto.MemberDto(m.username, m.age) from JPQL_MEMBER m", MemberDto.class).getResultList();
            resultList2.forEach(dto -> {
                System.out.println("memberDto.username = " + dto.username());
                System.out.println("memberDto.age = " + dto.age());
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
            IntStream.range(0, 100).forEach(i -> {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            });

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from JPQL_MEMBER m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size = " + result.size());
            result.forEach(System.out::println);

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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            // 내부 조인: [inner] join
            String query = "select m from JPQL_MEMBER m join m.team t";
            em.createQuery(query, Member.class).getResultList();

            // 외부 조인: left [outer] join
            String query2 = "select m from JPQL_MEMBER m left join m.team t";
            em.createQuery(query2, Member.class).getResultList();

            // 세타 조인
            String query3 = "select m from JPQL_MEMBER m, JPQL_TEAM t where m.username = team.name";
            em.createQuery(query3, Member.class).getResultList();

            // 1. 조인 대상 필터링
            String query4 = "select m from JPQL_MEMBER m left join m.team t on t.name = 'A'";
            em.createQuery(query4, Member.class).getResultList();

            // 2. 연관관계 없는 엔티티 외부 조인
            String query5 = "select m from JPQL_MEMBER m left join JPQL_TEAM t on m.username = t.name";
            em.createQuery(query5, Member.class).getResultList();

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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m.username, 'Hello', true from JPQL_MEMBER m";
            List<Object[]> resultList = em.createQuery(query).getResultList();
            resultList.forEach(o -> {
                System.out.println("objects = " + o[0]);
                System.out.println("objects = " + o[1]);
                System.out.println("objects = " + o[2]);
            });

            String query2 = "select m.username from JPQL_MEMBER m where m.type = :userType";
            List<String> resultList2 = em.createQuery(query2)
                    .setParameter("userType", jpql.entity.MemberType.ADMIN)
                    .getResultList();
            resultList2.forEach(o -> System.out.println("objects = " + o));

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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "select " +
                    "case when m.age <= 10 then '학생요금' " +
                    "     when m.age >= 60 then '경로요금' " +
                    "     else '일반요금' " +
                    "end " +
                    "from JPQL_MEMBER m";
            List<String> result = em.createQuery(query, String.class).getResultList();
            result.forEach(System.out::println);

            String query2 = "select coalesce(m.username, '이름 없는 회원') from JPQL_MEMBER m";
            List<String> result2 = em.createQuery(query2, String.class).getResultList();
            result2.forEach(System.out::println);

            String query3 = "select nullif(m.username, 'teamA') from JPQL_MEMBER m";
            List<String> result3 = em.createQuery(query3, String.class).getResultList();
            result3.forEach(System.out::println);

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
