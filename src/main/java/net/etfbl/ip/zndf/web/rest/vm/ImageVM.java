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
public class ImageVM {

    private String image;

    public ImageVM(String image) {
        this.image = image;
    }

    public ImageVM() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
