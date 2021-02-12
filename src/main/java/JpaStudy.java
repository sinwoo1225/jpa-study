import entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaStudy {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try{
            Member member = new Member();
            member.setName("회원1");
            em.persist(member);
            em.flush();
            em.clear();

            Member memberA = em.find(Member.class, 1L);
            memberA.setName("회원B"); // 수정

            tx.commit(); // 수정된 객체를 바탕으로 UPDATE쿼리가 나간다.
        } catch(Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
