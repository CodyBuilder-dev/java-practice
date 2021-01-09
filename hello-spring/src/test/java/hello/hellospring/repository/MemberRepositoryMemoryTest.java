package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberRepositoryMemoryTest {
    MemberRepository repository = new MemberRepositoryMemory();

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");
        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        // 저장후 불러온 멤버와 저장전 멤버가 동일한가?
        Assertions.assertEquals(result,member);
//        Assertions.assertEquals(result,null);
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("Spring1");
        repository.save(member1);

        Member result = repository.findByName("Spring1").get();

        Assertions.assertEquals(result,member1);

    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        Member member2 = new Member();

    }
}
