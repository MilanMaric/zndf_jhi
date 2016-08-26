/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.etfbl.ip.zndf.web.rest.vm.OmdbSearchVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author milan
 */
@RestController
@RequestMapping("/api/omdb")
public class OmdbAPI {

    private final Logger log = LoggerFactory.getLogger(OmdbAPI.class);

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OmdbSearchVM> callApi(@RequestParam(name = "s", required = true) String title, @RequestParam(name = "y", required = false) String year, Pageable pageable) {
        RestTemplate restTemplate = new RestTemplate();
        String requestURL = "http://www.omdbapi.com/?r=json&s=" + title + "&plot=short&page=" + pageable.getPageNumber() + "&offset=" + pageable.getOffset();
        log.info("Sending request URL: " + requestURL);
        OmdbSearchVM search = restTemplate.getForObject(requestURL, OmdbSearchVM.class);
        log.debug("Search: {} ", search);
        return ResponseEntity.ok().body(search);
    }

}
