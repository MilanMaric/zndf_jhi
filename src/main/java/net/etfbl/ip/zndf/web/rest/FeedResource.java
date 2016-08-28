/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest;

import javax.inject.Inject;
import net.etfbl.ip.zndf.rss.FilmsRssFeedView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milan
 */
@RestController
@RequestMapping("/api/feed")
public class FeedResource {

    @Inject
    FilmsRssFeedView filmsRssFeedView;

    @RequestMapping(method = RequestMethod.GET, produces = "application/*")
    public FilmsRssFeedView getFeed() {
        return filmsRssFeedView;
    }
}
