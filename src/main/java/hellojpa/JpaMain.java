package hellojpa;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    }
}
