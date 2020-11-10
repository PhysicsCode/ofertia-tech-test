package ofertia.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import ofertia.example.demo.dto.EventDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RestController
@RequestMapping("/events")
public class EventController {

    @Value("${events.client.url}")
    private String eventClientUrl;
    private final RestTemplate restTemplate = new RestTemplate();


    @GetMapping(value = "/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDTO> retrieveEventsByCity(@PathVariable("city") String city) {

        //proper validation

        log.info("Retrieving events for city = " + city);

        //EventService -> find Info for city -> return DTO
        //Build response based on DTO


        return restTemplate.getForEntity(eventClientUrl + "/events/" + city, EventDTO.class);
    }
}
