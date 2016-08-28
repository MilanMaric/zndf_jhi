/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.rss;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.etfbl.ip.zndf.domain.Film;
import net.etfbl.ip.zndf.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

/**
 *
 * @author milan
 */
@Component("filmsRssFeedView")
public class FilmsRssFeedView extends AbstractRssFeedView {

    @Autowired
    FilmRepository filmRepository;

    @Override
    protected Channel newFeed() {
        Channel channel = new Channel("rss_2.0");
        channel.setLink("http://localhost:8080" + "/films/feed");
        channel.setTitle("ZNDF - top 5");
        channel.setDescription("Top 5 movies for rss");
        return channel;
    }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> map, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return filmRepository.findByTitleContainingOrderByRateDesc("").stream().limit(5).map(this::createItem).collect(Collectors.toList());
    }

    private Item createItem(Film film) {
        Item item = new Item();
        item.setLink("http://localhost:8080/api/films" + film.getId());
        item.setTitle(film.getTitle());
        item.setDescription(createDescription(film));
        item.setPubDate(Date.from(film.getLastModifiedDate().toInstant()));
        return item;
    }

    private Description createDescription(Film film) {
        Description description = new Description();
        description.setType(Content.TEXT);
        description.setValue(film.getDescription());
        return description;
    }

}
