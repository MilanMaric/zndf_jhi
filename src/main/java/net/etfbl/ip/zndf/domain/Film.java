/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.domain;

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
import javax.persistence.OneToMany;

/**
 *
 * @author milan
 */
@Entity(name = "jhi_film")
public class Film extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "relase_date", nullable = true)
    private LocalDate relaseDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "film")
    private Set<ActorRoles> actorRoles = new HashSet<>();

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
