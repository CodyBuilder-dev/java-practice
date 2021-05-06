package jpabook.start;

import jpabook.start.league.entity.Player;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class NativeQueryCrudTest {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook-oracle");

    @Test
    public void insertQueryTest(){
        EntityManager em = emf.createEntityManager();

        String sql = "INSERT INTO PLAYER (ID, NAME) VALUES ('1','koo')";
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createNativeQuery(sql, Player.class);
        tx.commit();

        Player insertedPlayers = em.find(Player.class,"1");

        Assert.assertEquals(insertedPlayers.getId(),"1");
    }

    @Test
    public void selectQueryTest(){
        EntityManager em = emf.createEntityManager();
        String sql = "SELECT * FROM PLAYER";
        List<Player> selectedPLayers = em.createNativeQuery(sql, Player.class).getResultList();
        for (Player p : selectedPLayers) {
            System.out.println(p.getName());
        }
    }

    @Test
    public void deleteQueryTest(){

    }


}
