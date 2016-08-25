/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.repository;

import net.etfbl.ip.zndf.domain.Comment;
import net.etfbl.ip.zndf.domain.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author milan
 */
public interface CommentsRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findAllByFilm_Id(Film film, Pageable pageable);

    public Page<Comment> findAllByFilmId(Long filmId, Pageable pageable);
}
