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
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.etfbl.ip.zndf.domain.Comment;
import net.etfbl.ip.zndf.domain.Film;
import net.etfbl.ip.zndf.repository.CommentsRepository;
import net.etfbl.ip.zndf.repository.FilmRepository;
import net.etfbl.ip.zndf.security.AuthoritiesConstants;
import net.etfbl.ip.zndf.service.UserService;
import net.etfbl.ip.zndf.web.rest.util.HeaderUtil;
import net.etfbl.ip.zndf.web.rest.util.PaginationUtil;
import net.etfbl.ip.zndf.web.rest.vm.CommentVM;
import net.etfbl.ip.zndf.web.rest.vm.FilmVM;
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
@RequestMapping("/api/films")
public class FilmResource {

    private final Logger log = LoggerFactory.getLogger(FilmResource.class);

    @Inject
    FilmRepository filmRepository;

    @Inject
    CommentsRepository commentsRepository;

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FilmVM>> getAll(Pageable pageable) throws URISyntaxException {
        Page<Film> page = filmRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/films");
        List<FilmVM> list = page.getContent().stream().map(FilmVM::new).collect(Collectors.toList());
        return new ResponseEntity<>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Film> save(@RequestBody Film film) throws URISyntaxException {
        log.info("Saving film: {}", film);
        Film newFilm = filmRepository.save(film);
        return ResponseEntity.created(new URI("/api/films/" + newFilm.getId())).headers(HeaderUtil.createAlert("films.created", newFilm.getId().toString())).body(newFilm);
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Film film = filmRepository.findOne(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Film> getOne(@PathVariable Long id) {
        log.debug("Rest Get one: {}", id);
        Film film = filmRepository.findOne(id);
        log.info("REST get one film: {}", film);
        if (film != null) {
            return ResponseEntity.ok().body(film);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/comments")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id, Pageable pageable) throws URISyntaxException {
        Page<Comment> page = commentsRepository.findAllByFilmId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/films/" + id + "/comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/comments")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Comment> saveComment(@PathVariable Long id, @RequestBody CommentVM comment) throws URISyntaxException {
        log.info("Saving comment: {}", comment);
        Comment commentObject = new Comment();
        commentObject.setText(comment.getText());
        commentObject.setUser(userService.getUserWithAuthorities());
        Comment newComment = commentsRepository.save(commentObject);
        return ResponseEntity.created(new URI("/api/films/" + newComment.getId())).headers(HeaderUtil.createAlert("films.created", newComment.getId().toString())).body(newComment);

    }

}
