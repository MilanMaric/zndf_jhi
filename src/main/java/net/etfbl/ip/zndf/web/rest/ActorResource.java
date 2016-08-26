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
import net.etfbl.ip.zndf.domain.Actor;
import net.etfbl.ip.zndf.repository.ActorRepository;
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
@RequestMapping("/api/actors")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    @Inject
    ActorRepository actorRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Actor>> getAll(Pageable pageable) throws URISyntaxException {
        Page<Actor> page = actorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/actors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actor> save(@RequestBody Actor actor) throws URISyntaxException {
        log.info("Saving actor: {}", actor);
        Actor newActor = actorRepository.save(actor);
        return ResponseEntity.created(new URI("/api/actors/" + newActor.getId())).headers(HeaderUtil.createAlert("films.created", newActor.getId().toString())).body(newActor);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actor> update(@RequestBody Actor actor) throws URISyntaxException {
        log.info("Update actor: {}", actor);
        Actor newActor = actorRepository.save(actor);
        return ResponseEntity.created(new URI("/api/actors/" + newActor.getId())).headers(HeaderUtil.createAlert("films.updated", newActor.getId().toString())).body(newActor);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    @Timed
    public ResponseEntity<Actor> get(@PathVariable Long id) {
        Actor actor = actorRepository.findOne(id);
        if (actor != null) {
            return ResponseEntity.ok().body(actor);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
