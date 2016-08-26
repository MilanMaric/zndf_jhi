/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author milan
 */
public class OmdbSearchVM {

    @JsonProperty(value = "Search")
    private List<OmdbVM> Search;
    private String totalResults;

    @Override
    public String toString() {
        return "OmdbSearchVM{" + "totalResults=" + totalResults + '}';
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<OmdbVM> getSearch() {
        return Search;
    }

    public void setSearch(List<OmdbVM> Search) {
        this.Search = Search;
    }

}
