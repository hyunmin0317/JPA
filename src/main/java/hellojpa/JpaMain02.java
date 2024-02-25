package hellojpa;

import hellojpa.entity.Member;
import hellojpa.repository.MemberRepository;

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
        MemberRepository.save(member);
    }

    public static void findOneTest() {
        Member member = MemberRepository.findOne(1L);
        System.out.println("findMember.id = " + member.getId());
        System.out.println("findMember.name = " + member.getName());
    }

    public static void findAllTest() {
        List<Member> result = MemberRepository.findAll();
        result.forEach(m -> System.out.println(m.getName()));
    }

    public static void updateTest() {
        MemberRepository.update(1L, "HelloJPA");
        Member member = MemberRepository.findOne(1L);
        System.out.println("updatedMember.id = " + member.getId());
        System.out.println("updatedMember.name = " + member.getName());
    }

    public static void deleteTest() {
        MemberRepository.delete(101L);
    }
}
