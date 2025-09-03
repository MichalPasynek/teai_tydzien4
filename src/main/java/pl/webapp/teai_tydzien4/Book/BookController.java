package pl.webapp.teai_tydzien4.Book;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BookController {

    Book[] books;

    @EventListener(ApplicationReadyEvent.class)
    private void initBooks() {
        books = getBooksInfo();
    }

    private Book[] getBooksInfo() {
        RestTemplate restTemplate = new RestTemplate();
        Book[] forObject = restTemplate.getForObject("https://wolnelektury.pl/api/books", Book[].class);
        return forObject;
    }

    public Set<String> getUniqueAuthors() {
        Set<String> uniqueAuthors = Arrays.stream(books).map(Book::getAuthor).collect(Collectors.toSet());
        return uniqueAuthors;
    }

    @GetMapping("/book")
    public String get(@RequestParam(required = false) String author, Model model) {
        Set<String> uniqueAuthors = getUniqueAuthors();
        model.addAttribute("uniqueAuthors", uniqueAuthors);
        model.addAttribute("selectedAuthor", author);

        if (author != null && !author.isEmpty()) {
            List<Book> filteredBooks = Arrays.stream(books)
                    .filter(b -> author.equals(b.getAuthor()))
                    .collect(Collectors.toList());
            model.addAttribute("bookInfo", filteredBooks);
        } else {
            model.addAttribute("bookInfo", Arrays.asList(books));
        }
        return "bookView";
    }
}
