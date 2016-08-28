/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.repository;

import net.etfbl.ip.zndf.domain.FilmRate;
import net.etfbl.ip.zndf.domain.RateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author milan
 */
public interface FilmRatesRepository extends JpaRepository<FilmRate, RateId> {

    @Query(value = "SELECT avg(rate) FROM jpa_film_rate where film_id=?;", nativeQuery = true)
    Double getFilmAverageRate(Long id);
}
