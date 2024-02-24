package hellojpa;

import hellojpa.entity.Member;
import hellojpa.repository.JpaRepository02;

import java.util.List;

public class JpaMain02 {

    public static void main(String[] args) {
        saveTest();
        findOneTest();
        updateTest();
        findAllTest();
        deleteTest();
    }

    public static void saveTest() {
        Member member = new Member(1L, "HelloA");
        JpaRepository02.save(member);
    }

    public static void findOneTest() {
        Member member = JpaRepository02.findOne(1L);
        System.out.println("findMember.id = " + member.getId());
        System.out.println("findMember.name = " + member.getName());
    }

    public static void findAllTest() {
        List<Member> result = JpaRepository02.findAll();
        result.forEach(m -> System.out.println(m.getName()));
    }

    public static void updateTest() {
        JpaRepository02.update(1L, "HelloJPA");
        Member member = JpaRepository02.findOne(1L);
        System.out.println("updatedMember.id = " + member.getId());
        System.out.println("updatedMember.name = " + member.getName());
    }

    public static void deleteTest() {
        JpaRepository02.delete(101L);
    }
}
