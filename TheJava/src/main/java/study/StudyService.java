package study;

public class StudyService {
    private final MemberService memberService;

    private final StudyRepository repository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createStudy(Long memberId,Study study) {
        Member member = memberService.findById(memberId); // 멤버 서비스에서 멤버 관련 메서드를 제공하는듯

        if(member == null) {
            throw new IllegalArgumentException("Member doesn't exist for id");
        }
        study.setOwner(member);
        return repository.save(study);

    }

    // ToDo1 :

    // ToDo2 : 
}
