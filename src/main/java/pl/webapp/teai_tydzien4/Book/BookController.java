package pl.webapp.teai_tydzien4.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BookController {
    public BookController() {
        RestTemplate restTemplate = new RestTemplate();

        Book[] forObject = restTemplate.getForObject("https://wolnelektury.pl/api/books", Book[].class);

        for (Book book : forObject) {
            System.out.println(book.getEpoch());
        }
    }

}
