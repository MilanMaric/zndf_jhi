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
import net.etfbl.ip.zndf.repository.FilmRepository;
import net.etfbl.ip.zndf.web.rest.vm.FilmVM;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author milan
 */
public class FilmsRssFeedView extends AbstractRssFeedView {

    @Autowired
    FilmRepository filmRepository;

    @Override
    protected void buildFeedEntries(Map<String, Object> map, Channel t, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Channel newFeed() {
        Channel channel = new Channel("rss_2.0");
        channel.setLink("http://localhost:8080/feed/");
        channel.setTitle("ZNDF");
        channel.setDescription("ZNDF");
        channel.setPubDate(new Date());
        return channel;
    }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return filmRepository.findAll().stream().limit(5).map(FilmVM::new).map(this::createItem).collect(Collectors.toList());
    }

    private Item createItem(FilmVM film) {
        Item item = new Item();
        item.setLink("http://localhost:8080/#/films/" + film.getId());
        item.setTitle(film.getTitle());
        item.setDescription(createDescription(film));
        return item;
    }

    private Description createDescription(FilmVM film) {
        Description description = new Description();
        description.setType(Content.TEXT);
        description.setValue(film.getDescription());
        return description;
    }

}
