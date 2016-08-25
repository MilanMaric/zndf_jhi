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

    public FilmVM() {
    }

    public FilmVM(Film film) {
        this.title = film.getTitle();
        this.id = film.getId();
        this.releaseDate = film.getRelaseDate();
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
