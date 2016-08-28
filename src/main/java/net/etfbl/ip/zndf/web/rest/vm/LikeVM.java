/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest.vm;

/**
 *
 * @author milan
 */
public class LikeVM {

    private Boolean status;

    public LikeVM(Boolean status) {
        this.status = status;
    }

    public LikeVM() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
