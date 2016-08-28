/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 *
 * @author milan
 */
@Embeddable
public class RateId implements Serializable {

    @ManyToOne(optional = false)
    private Film film;
    @ManyToOne(optional = false)
    private User user;

    public RateId(Film film, User user) {
        this.film = film;
        this.user = user;
    }

    public RateId() {
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RateId) {
            RateId objR = (RateId) obj;
            return user.getId().equals(objR.user.getId()) && film.getId().equals(objR.film.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return user.getId().hashCode() + film.getId().hashCode();
    }

    @Override
    public String toString() {
        return "RateId{" + "film=" + film.getId() + ", user=" + user.getId() + '}';
    }

}
