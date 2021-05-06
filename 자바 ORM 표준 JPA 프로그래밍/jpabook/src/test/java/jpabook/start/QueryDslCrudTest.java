package jpabook.start;

import jpabook.start.shop.entity.Member;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class QueryDslCrudTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook-oracle");

    @Test
    public void criteriaSelectTest(){
        EntityManager em = emf.createEntityManager();
        Member m = new Member();
        m.setUsername("koo");
        m.setAge(29);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(m);
        tx.commit();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);
        // 루트 클래스 지정
        Root<Member> rootMember = query.from(Member.class);

        CriteriaQuery<Member> cq = query.select(rootMember).where(cb.equal(rootMember.get("username"), "koo"));
        List<Member> insertedMemberList = em.createQuery(cq).getResultList();
        Assert.assertEquals(insertedMemberList.get(0).getUsername(), m.getUsername());
    }
}
