package jpabook.start;

import jpabook.start.shop.entity.Member;
import jpabook.start.league.entity.Player;
import jpabook.start.league.entity.Team;

import javax.persistence.*;
import java.util.List;

/**
 * @author holyeye
 */
public class JpaMain {

    public static void main(String[] args) {
        //1. 엔티티 매니저 팩토리 생성
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("jpabook-oracle");
        //2. 엔티티 매니저 생성
        EntityManager em =
                emf.createEntityManager();
        //3. 트랜잭션 기능 획득
        EntityTransaction tx = em.getTransaction();


        boolean shouldStartMemberLogic = false;
        shouldStartMemberLogic = true;
        boolean shouldStartPlayerLogic = false;
        shouldStartPlayerLogic = true;
        boolean shouldStartJoinLogic = false;
        shouldStartJoinLogic = true;
        
        
        if (shouldStartMemberLogic) {
            // 4. Member Business Logic
            try {

                tx.begin(); //트랜잭션 시작
                logic(em);  //비즈니스 로직
                tx.commit();//트랜잭션 커밋

            } catch (Exception e) {
                e.printStackTrace();
                tx.rollback(); //트랜잭션 롤백
            } finally {
                em.close(); //엔티티 매니저 종료
            }
        }

        em = emf.createEntityManager();
        tx = em.getTransaction();
        if (shouldStartPlayerLogic) {
            // 5. Player Business Logic
            try {
                tx.begin();
                playerLogic(em);
                tx.commit();

            } catch (Exception e) {
                e.printStackTrace();
                tx.rollback();
            } finally {
                em.close();
            }
        }

        em = emf.createEntityManager();
        tx = em.getTransaction();
        if (shouldStartJoinLogic) {
            // 6. Join Logic
            try {
                tx.begin();
                queryLogicJoin(em);
                tx.commit();

            } catch (Exception e) {
                e.printStackTrace();
                tx.rollback();
            } finally {
                em.close();
            }
        }



        // 프로그램 종료
        emf.close(); //엔티티 매니저 팩토리 종료. 아마 종료하지 않으면 Connection Pool이 가득 찰듯
    }

    public static void logic(EntityManager em) {

//        String id = "id1";
        Long id = 1L;
        Member member = new Member();
//        member.setId(id);
        member.setUsername("지한");
        member.setAge(2);

        //등록
        em.persist(member);

        //수정
        member.setAge(20);

        //한 건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        //목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        //삭제
        em.remove(member);

    }

    public static void playerLogic(EntityManager em) {
        Player player = new Player();
        Player player2 = new Player();

        Team team = new Team();
        team.setId("team1");
        team.setName("정승네트워크");

        // 반드시 team을 persist 상태로 만들어야 한다
        em.persist(team);

        player.setId("player1");
        player.setName("조충범");
        player.setTeam(team);
        em.persist(player);

        player2.setId("player2");
        player2.setName("이미나");
        player2.setTeam(team);
        em.persist(player2);

        Player findPlayer = em.find(Player.class, "player1");
//        System.out.println(findPlayer.getName());
    }

    private static void queryLogicJoin(EntityManager em) {
        String jpql = "SELECT p FROM Player p JOIN p.team t WHERE t.name=:teamName";
        
        // JPQL을 이용한 Join 처리후 조회
        // 일반적인 SQL과는 문법이 좀 다르다
        List<Player> resultList = em.createQuery(jpql,Player.class)
                .setParameter("teamName","정승네트워크")
                .getResultList();

        System.out.println("정승네트워크 현재 직원수:"+resultList.size());

        for (Player player : resultList) {
            System.out.println(player.getName());
        }
    }
}
