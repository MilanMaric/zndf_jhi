/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.repository;

import java.util.List;
import net.etfbl.ip.zndf.domain.Film;
import net.etfbl.ip.zndf.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author milan
 */
public interface FilmRepository extends JpaRepository<Film, Long> {

    Page<Film> findAllByTitleContaining(String title, Pageable pageable);

    List<Film> findByTitleContainingOrderByRateDesc(String title);

    Page<Film> findAllByTitleContainingOrderByRateDesc(String title, Pageable pageable);

    Page<Film> findAllByTitleContainingAndGenresOrderByRateDesc(String title, Genre genre, Pageable pageable);

    Page<Film> findAllByTitleContainingAndGenres(String title, Genre genre, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT count(*) as number_of_favorites FROM zndf.jhi_user_favorite_films where film_id=?;")
    Long getFilmFavorites(Long id);

    @Query(nativeQuery = true, value = "select film.*,count(user_id) as number_of_favorites from jhi_film as film left join jhi_user_favorite_films on film.id=jhi_user_favorite_films.film_id order by number_of_favorites desc limit ? offset ?; ")
    List<Film> findTopByFavorites(int limit, int offset);

}
