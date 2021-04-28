package jpabook.start;

import jpabook.start.shop.entity.Member;
import jpabook.start.shop.entity.Order;
import jpabook.start.shop.entity.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;


public class ShopMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook-oracle");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {

            tx.begin(); //트랜잭션 시작
            orderLogic(em);  //비즈니스 로직
            tx.commit();//트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }
    }

    //
    public static void orderLogic(EntityManager em) {
        // 주문으로부터 회원을 찾는 가상의 그래프 탐색 예제
        Long orderId = 1L;
        Order order = em.find(Order.class, orderId);
        Member member = order.getMember();

        // 주문으로부터 해당 주문의 상품을 찾는 가상의 그래프 탐색 예제
        Order order2 = em.find(Order.class, orderId);
        List<OrderItem> orderItem = order2.getOrderItems();

    }
}
