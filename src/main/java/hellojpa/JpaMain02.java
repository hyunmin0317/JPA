package hellojpa;

import hellojpa.entity.Member;
import hellojpa.repository.JpaRepository02;

public class JpaMain02 {

    public static void main(String[] args) {
        saveTest();
        findOneTest();
    }

    public static void saveTest() {
        Member member = Member.builder()
                .id(1L)
                .name("HelloA")
                .build();
        JpaRepository02.save(member);
    }

    public static void findOneTest() {
        Member findMember = JpaRepository02.findOne(1L);
        System.out.println("findMember.id = " + findMember.getId());
        System.out.println("findMember.name = " + findMember.getName());
    }
}
