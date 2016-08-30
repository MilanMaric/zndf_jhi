/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import net.etfbl.ip.zndf.domain.Comment;
import net.etfbl.ip.zndf.domain.Film;
import net.etfbl.ip.zndf.domain.FilmRate;
import net.etfbl.ip.zndf.domain.Genre;
import net.etfbl.ip.zndf.domain.RateId;
import net.etfbl.ip.zndf.domain.Trailer;
import net.etfbl.ip.zndf.domain.User;
import net.etfbl.ip.zndf.repository.ActorRolesRepository;
import net.etfbl.ip.zndf.repository.CommentsRepository;
import net.etfbl.ip.zndf.repository.FilmRatesRepository;
import net.etfbl.ip.zndf.repository.FilmRepository;
import net.etfbl.ip.zndf.repository.GenreRepository;
import net.etfbl.ip.zndf.repository.TrailerRepository;
import net.etfbl.ip.zndf.repository.UserRepository;
import net.etfbl.ip.zndf.security.AuthoritiesConstants;
import net.etfbl.ip.zndf.service.FileService;
import net.etfbl.ip.zndf.service.UserService;
import net.etfbl.ip.zndf.web.rest.util.HeaderUtil;
import net.etfbl.ip.zndf.web.rest.util.PaginationUtil;
import net.etfbl.ip.zndf.web.rest.vm.CommentVM;
import net.etfbl.ip.zndf.web.rest.vm.FilmVM;
import net.etfbl.ip.zndf.web.rest.vm.LikeVM;
import net.etfbl.ip.zndf.web.rest.vm.RateVM;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    ActorRolesRepository actorRolesRepository;

    @Inject
    TrailerRepository trailerRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private FilmRatesRepository filmRatesRepository;

    @Inject
    private FileService fileService;

    @Inject
    private GenreRepository genreRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FilmVM>> getAll(Pageable pageable, @RequestParam(name = "s", required = false) String search, @RequestParam(name = "g", required = false) Long genreId) throws URISyntaxException {
        Page<Film> page;
        if (search == null) {
            search = "";
        }
        if (genreId == null) {
            page = filmRepository.findAllByTitleContaining(search, pageable);
        } else {
            Genre genre = genreRepository.findOne(genreId);
            if (genre != null) {
                page = filmRepository.findAllByTitleContainingAndGenres(search, genre, pageable);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        log.debug("Searching for film with tile {} and genreId {} ", search, genreId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/films");
        List<FilmVM> list = page.getContent().stream().map(FilmVM::new).collect(Collectors.toList());
        return new ResponseEntity<>(list, headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Film> save(@RequestBody Film film) throws URISyntaxException {
        log.info("Saving film: {}", film);

        Film newFilm = filmRepository.save(film);
        film.getActorRoles().stream().forEach(actorRole -> {
            log.debug("Actor role: {} -> actor {}", actorRole, actorRole.getActor());
            actorRole.setFilm(newFilm);
            actorRolesRepository.save(actorRole);
        });
        return ResponseEntity.created(new URI("/api/films/" + newFilm.getId())).headers(HeaderUtil.createAlert("films.created", newFilm.getId().toString())).body(newFilm);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Film> update(@RequestBody Film film) throws URISyntaxException {
        log.info("Saving film: {}", film);

        Film newFilm = filmRepository.save(film);
        film.getActorRoles().stream().forEach(actorRole -> {
            log.debug("Actor role: {} -> actor {}", actorRole, actorRole.getActor());
            actorRole.setFilm(newFilm);
            actorRolesRepository.save(actorRole);
        });
        return ResponseEntity.created(new URI("/api/films/" + newFilm.getId())).headers(HeaderUtil.createAlert("films.updated", newFilm.getId().toString())).body(newFilm);
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
        Film film = filmRepository.findOne(id);
        if (film != null) {
            Comment commentObject = new Comment();
            commentObject.setText(comment.getText());
            commentObject.setUser(userService.getUserWithAuthorities());
            commentObject.setFilm(film);
            Comment newComment = commentsRepository.save(commentObject);
            return ResponseEntity.created(new URI("/api/films/" + newComment.getId())).headers(HeaderUtil.createAlert("films.created", newComment.getId().toString())).body(newComment);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/comments/{commentId}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @PathVariable Long commentId) throws URISyntaxException {
        Comment commentObject = commentsRepository.findOne(commentId);
        if (commentObject != null && commentObject.getFilm().getId().equals(id)) {
            commentObject.setActive(false);
            commentsRepository.save(commentObject);
            return ResponseEntity.ok().headers(HeaderUtil.createAlert("comment.deleted", commentId.toString())).build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/trailers")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Trailer> saveTrailer(@PathVariable Long id, @RequestBody Trailer trailer) throws URISyntaxException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            trailer.setFilm(film);
            Trailer newTrailer = trailerRepository.save(trailer);
            return ResponseEntity.created(new URI("/api/films/" + newTrailer.getId())).headers(HeaderUtil.createAlert("films.created", newTrailer.getId().toString())).body(newTrailer);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/trailer/{trailerId}")
    @Timed
    public void downloadTrailer(@PathVariable Long id, @PathVariable Long trailerId, HttpServletResponse response) throws URISyntaxException, IOException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            Trailer trailer = trailerRepository.findOne(trailerId);
            if (trailer.getInternal()) {
                fileService.downloadFile(response, "C://trailers", trailer.getUri());
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/trailers")
    @Timed
    public ResponseEntity<List<Trailer>> saveTrailer(@PathVariable Long id) throws URISyntaxException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            List<Trailer> list = trailerRepository.findAllByFilm(film);
            return ResponseEntity.ok().body(list);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/trailer")
    @Timed
    public ResponseEntity<Trailer> uploadTrailer(HttpServletRequest request, @PathVariable Long id, @RequestPart MultipartFile video) throws URISyntaxException, IOException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            Trailer trailer = new Trailer();
            trailer.setFilm(film);
            trailer.setInternal(true);
            trailer.setActive(true);
            String returnStr;
            returnStr = fileService.uploadFile(video, "C://trailers");
            trailer.setUri(returnStr);
            Trailer newTrailer = trailerRepository.save(trailer);
            return ResponseEntity.ok().body(newTrailer);
        } else {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("trailer", "", "")).body(null);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/like")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<LikeVM> checkLike(@PathVariable Long id) throws URISyntaxException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            List<Film> films = userService
                    .getUserWithAuthorities().getFavoriteFilms()
                    .stream()
                    .filter(f -> {
                        return Objects.equals(f.getId(), film.getId());
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(new LikeVM(films.size() == 1));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/like")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<LikeVM> like(@PathVariable Long id) throws URISyntaxException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            User user = userService
                    .getUserWithAuthorities();
            List<Film> films = user.getFavoriteFilms()
                    .stream()
                    .filter(f -> {
                        return Objects.equals(f.getId(), film.getId());
                    })
                    .collect(Collectors.toList());
            if (films.size() == 0) {
                user.getFavoriteFilms().add(film);
                userRepository.save(user);
                return ResponseEntity.ok().body(new LikeVM(true));
            } else {
                return ResponseEntity.ok().body(new LikeVM(true));
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/rate")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<RateVM> rate(@PathVariable Long id, @RequestBody @Valid RateVM rate) throws URISyntaxException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            User user = userService
                    .getUserWithAuthorities();
            RateId rateId = new RateId(film, user);
            FilmRate filmRate = new FilmRate();
            filmRate.setId(rateId);
            filmRate.setRate(rate.getRate());
            filmRatesRepository.save(filmRate);
            Double filmAvgRate = filmRatesRepository.getFilmAverageRate(film.getId());
            log.debug("Film rate after save: {}", filmAvgRate);
            film.setRate(filmAvgRate);
            filmRepository.save(film);
            return ResponseEntity.ok().body(rate);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}/rate")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<RateVM> getRate(@PathVariable Long id) throws URISyntaxException {
        Film film = filmRepository.findOne(id);
        if (film != null) {
            User user = userService
                    .getUserWithAuthorities();
            RateId rateId = new RateId(film, user);
            FilmRate filmRate = filmRatesRepository.findOne(rateId);
            if (filmRate != null) {
                return ResponseEntity.ok().body(new RateVM(filmRate));
            } else {
                return ResponseEntity.ok().body(new RateVM(0));
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/favorites")
    @Timed
    public ResponseEntity<List<FilmVM>> getAll() throws URISyntaxException {
        User user = userService
                .getUserWithAuthorities();
        List<FilmVM> list = user.getFavoriteFilms().stream().map(FilmVM::new).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
