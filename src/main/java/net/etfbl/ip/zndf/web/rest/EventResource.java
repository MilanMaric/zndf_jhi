/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;
import javax.validation.Valid;
import net.etfbl.ip.zndf.domain.Event;
import net.etfbl.ip.zndf.repository.EventRepository;
import net.etfbl.ip.zndf.repository.UserRepository;
import net.etfbl.ip.zndf.security.AuthoritiesConstants;
import net.etfbl.ip.zndf.service.MailService;
import net.etfbl.ip.zndf.web.rest.util.HeaderUtil;
import net.etfbl.ip.zndf.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milan
 */
@RestController
@RequestMapping("/api/events")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private MailService mailService;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.SUPERUSER)
    public ResponseEntity<Event> saveEvent(@RequestBody @Valid Event event) throws URISyntaxException {
        event.setSent(false);
        Event newEvent = eventRepository.save(event);
        return ResponseEntity.created(new URI("/api/events/" + newEvent.getId())).headers(HeaderUtil.createEntityCreationAlert("event", newEvent.getId().toString())).body(newEvent);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Event>> getEvents(Pageable pageable) throws URISyntaxException {
        Page<Event> page = eventRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    @Timed
    public ResponseEntity<Event> getEvent(@PathVariable Long id) throws URISyntaxException {
        Event event = eventRepository.findOne(id);
        if (event != null) {
            return ResponseEntity.ok().body(event);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/send")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Event> sendEvent(@PathVariable Long id) throws URISyntaxException {
        Event event = eventRepository.findOne(id);
        if (event != null && !(event.getSent())) {
            event.setSent(true);
            userRepository.findAll().stream().forEach(user -> {
                log.debug("hsidhsaihdiusahdosa");
                mailService.sendNotificationEmail(user, event);
            });
            eventRepository.save(event);
            return ResponseEntity.ok().body(event);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
