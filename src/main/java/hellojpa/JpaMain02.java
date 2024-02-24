package hellojpa;

import hellojpa.entity.Member;
import hellojpa.repository.JpaRepository02;

public class JpaMain02 {

    public static void main(String[] args) {
        saveTest();
        findOneTest();
        updateTest();
    }

    public static void saveTest() {
        Member member = Member.builder()
                .id(1L)
                .name("HelloA")
                .build();
        JpaRepository02.save(member);
    }

    public static void findOneTest() {
        Member member = JpaRepository02.findOne(1L);
        System.out.println("findMember.id = " + member.getId());
        System.out.println("findMember.name = " + member.getName());
    }

    public static void updateTest() {
        JpaRepository02.update(1L, "HelloJPA");
        Member member = JpaRepository02.findOne(1L);
        System.out.println("updatedMember.id = " + member.getId());
        System.out.println("updatedMember.name = " + member.getName());
    }
}
