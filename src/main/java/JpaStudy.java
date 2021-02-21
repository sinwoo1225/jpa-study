import entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

public class JpaStudy {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try{
            Member member1 = new Member();
            Member member2 = new Member();

            member1.setUsername("회원1");
            member1.setRoleType(Member.RoleType.USER);
            member1.setCreatedDate(new Date());

            member2.setUsername("관리자");
            member2.setCreatedDate(new Date());
            member2.setRoleType(Member.RoleType.ADMIN);

            em.persist(member1);
            em.persist(member2);

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
