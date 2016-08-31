/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.etfbl.ip.zndf.domain.Film;
import net.etfbl.ip.zndf.repository.FilmRepository;
import net.etfbl.ip.zndf.security.AuthoritiesConstants;
import net.etfbl.ip.zndf.web.rest.vm.FilmVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milan
 */
@RestController(value = "/api/statistics")
@Secured(AuthoritiesConstants.ADMIN)
public class StatisticsResource {

    @Inject
    private FilmRepository filmRepository;

    @Inject
    private UserResource userResource;

    @RequestMapping(value = "/api/statistics/films/favorites", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<FilmVM>> getByFovorable(Pageable pageable) {
        List<Film> list = filmRepository.findTopByFavorites(pageable.getPageSize(), pageable.getOffset());
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(list.stream().map(FilmVM::new).collect(Collectors.toList()));
    }

    @RequestMapping(value = "/api/statistics/films/rated", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<FilmVM>> getByRate(Pageable pageable) {
        Page<Film> page = filmRepository.findAllByTitleContainingOrderByRateDesc("", pageable);
        return ResponseEntity.ok().body(page.getContent().stream().map(FilmVM::new).collect(Collectors.toList()));
    }

}
