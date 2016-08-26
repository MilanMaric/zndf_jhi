/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Where;

/**
 *
 * @author milan
 */
@Entity(name = "jhi_film")
@Where(clause = "active=1")
public class Film extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @Column(name = "relase_date", nullable = true)
    private LocalDate relaseDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "film")
    private Set<ActorRoles> actorRoles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Genre> genres = new HashSet<>();

    @Column(name = "description")
    private String description;

    @Column(name = "rate")
    @JsonIgnore
    private Double rate;

    @Column(name = "duration")
    private Double duration;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "active")
    @JsonIgnore
    Boolean active = true;

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

    public LocalDate getRelaseDate() {
        return relaseDate;
    }

    public void setRelaseDate(LocalDate relaseDate) {
        this.relaseDate = relaseDate;
    }

    public Set<ActorRoles> getActorRoles() {
        return actorRoles;
    }

    public void setActorRoles(Set<ActorRoles> actorRoles) {
        this.actorRoles = actorRoles;
    }

    @Override
    public String toString() {
        return "Film{" + "id=" + id + ", title=" + title + ", relaseDate=" + relaseDate + '}';
    }

}
