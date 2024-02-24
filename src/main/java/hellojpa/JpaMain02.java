package hellojpa;

import hellojpa.entity.Member;
import hellojpa.repository.JpaRepository02;

public class JpaMain02 {

    public static void main(String[] args) {
        saveTest();
    }

    public static void saveTest() {
        Member member = Member.builder()
                .id(1L)
                .name("HelloA")
                .build();
        JpaRepository02.save(member);
    }
}
