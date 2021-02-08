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
            // 생성
            Member member = new Member();
            member.setName("시루떡");
            em.persist(member);
            em.flush(); // ✅ 명시적으로 DB에 쿼리를 보냄

            // 수정
            member.setName("시루");
            em.flush();

            // 삭제
            em.remove(member);
            em.flush();

            // 단건 조회
            member = em.find(Member.class, 1L );
            System.out.println(member);

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
