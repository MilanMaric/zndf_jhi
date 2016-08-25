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
import net.etfbl.ip.zndf.domain.Genre;
import net.etfbl.ip.zndf.repository.GenreRepository;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milan
 */
@RestController
@RequestMapping("/api/genres")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    @Inject
    GenreRepository genreRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Genre>> getAll(Pageable pageable) throws URISyntaxException {
        Page<Genre> page = genreRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/films");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genre> save(@RequestBody Genre genre) throws URISyntaxException {
        log.info("Saving genre: {}", genre);
        Genre newGenre = genreRepository.save(genre);
        return ResponseEntity.created(new URI("/api/genres/" + newGenre.getId())).headers(HeaderUtil.createAlert("films.created", newGenre.getId().toString())).body(newGenre);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Genre> update(@RequestBody Genre genre) throws URISyntaxException {
        log.info("Update genre: {}", genre);
        Genre newGenre = genreRepository.save(genre);
        return ResponseEntity.created(new URI("/api/genres/" + newGenre.getId())).headers(HeaderUtil.createAlert("films.updated", newGenre.getId().toString())).body(newGenre);
    }
}
