/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author milan
 */
@Entity(name = "jpa_film_rate")
public class FilmRate implements Serializable {

    @EmbeddedId
    private RateId id;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "rate")
    private Integer rate;

    public RateId getId() {
        return id;
    }

    public void setId(RateId id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "FilmRate{" + "id=" + id + ", rate=" + rate + '}';
    }

}
