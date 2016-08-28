/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest.vm;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import net.etfbl.ip.zndf.domain.FilmRate;

/**
 *
 * @author milan
 */
public class RateVM {

    @Min(value = 1)
    @Max(value = 10)
    private int rate;

    public RateVM(FilmRate filmRate) {
        this.rate = filmRate.getRate();
    }

    public RateVM() {
    }

    public RateVM(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

}
