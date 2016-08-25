/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author milan
 */
@Entity(name = "jhi_actor_roles")
public class ActorRoles extends AbstractAuditingEntity implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    private Actor actor;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Film film;

    @Column(name = "role_name")
    private String roleName;

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
