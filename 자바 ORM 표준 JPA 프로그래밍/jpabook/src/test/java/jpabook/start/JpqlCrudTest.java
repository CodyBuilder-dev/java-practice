package jpabook.start;

import jpabook.start.shop.entity.Member;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;


public class JpqlCrudTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook-oracle");

    @Test
    public void jpqlInsertTest(){
        EntityManager em = emf.createEntityManager();
        Member m = new Member();
        m.setUsername("koo");
        m.setAge(29);
        String jpql = "Insert Into Member m";
        em.createQuery(jpql,Member.class);

        Member insertedMember = em.find(Member.class,m.getId());
    }

    @Test
    public void jpqlSelectTest(){
        EntityManager em = emf.createEntityManager();
        Member m = new Member();
        m.setUsername("koo");
        m.setAge(29);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(m);
        tx.commit();

        String jpql = "SELECT m FROM Member m";
        List<Member> insertedMemberList = em.createQuery(jpql,Member.class).getResultList();

        Assert.assertEquals(insertedMemberList.get(0).getUsername(),m.getUsername());
    }
}

