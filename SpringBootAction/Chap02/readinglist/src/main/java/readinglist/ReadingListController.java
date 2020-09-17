package readinglist;

public class ReadingListController {
    private static final String reader="craig";
    
    private ReadingListRepository readingListRepository;

    public ReadingListController(ReadingListRepository readingListRepository) {
        this.readingListRepository=readingListRepository;
    }

    public String readersBooks(Model model) { // 요청이 들어올 경우, 
        List<Book> readingList = readingListRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books",readingList); // 모델에 속성을 추가
        }
        return "readingList"; // 모델을 렌더링할 View의 이름
    }

    public String addToReadingList(Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/"; // 리다이렉트 경로 설정
    }
}