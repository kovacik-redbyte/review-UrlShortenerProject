package com.demo.urlshortener;

import com.demo.urlshortener.interfaces.IUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/api")

public class UrlController {
    private final IUrlService urlService;

    @Autowired
    public UrlController(IUrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortUrlId}")
    public RedirectView redirectToUrl(@PathVariable int shortUrlId) {
        try {
            String longUrl = urlService.getLongUrl(shortUrlId);
            if (longUrl == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "URL not found"
                );
            }
            return new RedirectView(longUrl);
        } catch (Exception ex) {
            return new RedirectView("/error-page");
        }
        // TODO mojsej aby to bolo uplne rest friendly radsej by som vracal ResponseEntity takto. Tu mam aj moznost vratit HTTP status.
        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(longUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
        */
    }

    @PostMapping("/addUrl")
    public ResponseEntity<String> addUrl(@RequestBody Map<String, String> body) {
        // TODO mojsej Map<String, String> nie je vhodna struktura pretoze mi sem vstupuje iba jeden parameter. Preco sem nedate @RequestParam String longUrl ?
        // Automaticky by sa spravila aj validacia requestu a kebyze longUrl chyba tak to vrati HTTP 400
        String longUrl = body.get("longUrl");
        if (longUrl == null) {
            return ResponseEntity.badRequest().body("Missing longUrl");
        }

        try {
            String shortUrl = urlService.addUrl(longUrl);
            return ResponseEntity.ok(shortUrl);
        } catch (Exception ex) {
            // TODO mojsej toto tu nie je potrebne takto explicitne riesit. Controller pri exception sam prebali HTTP response do 500 error kodu.
            // Ak by som aj chcel customizovat co sa presne vrati tak by som spravil genericky error handler, aby som nemusel v kazdej metode explicitne
            // riesit error handling.
            return ResponseEntity.status(500).body("Could not generate short URL");
        }
    }
}
