/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest.vm;

import java.time.LocalDate;
import net.etfbl.ip.zndf.domain.Film;

/**
 *
 * @author milan
 */
public class FilmVM {

    private Long id;

    private String title;
    private LocalDate releaseDate;

    private Double duration;
    private Double rating;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FilmVM() {
    }

    public FilmVM(Film film) {
        this.title = film.getTitle();
        this.id = film.getId();
        this.releaseDate = film.getRelaseDate();
        this.duration = film.getDuration();
        this.rating = film.getRate();
        this.description = film.getDescription();
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

}
