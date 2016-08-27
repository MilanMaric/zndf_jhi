/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.rss;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.feed.AbstractFeedView;

/**
 *
 * @author milan
 */
public abstract class AbstractRssFeedView extends AbstractFeedView<Channel> {

    public AbstractRssFeedView() {
        setContentType("application/rss+xml");
    }

    @Override
    protected Channel newFeed() {
        return new Channel("rss_2.0");
    }

    protected abstract List<Item> buildFeedItems(Map<String, Object> model,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
            throws Exception;

}
