package jpql;

import jakarta.persistence.*;
import jpql.dto.MemberDto;
import jpql.entity.Address;
import jpql.entity.Member;
import jpql.entity.Team;

import java.util.List;

public class JpaMain10 {

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
}
