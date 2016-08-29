/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.etfbl.ip.zndf.domain.Gallery;
import net.etfbl.ip.zndf.repository.GalleryRepository;
import net.etfbl.ip.zndf.security.AuthoritiesConstants;
import net.etfbl.ip.zndf.service.FileService;
import net.etfbl.ip.zndf.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author milan
 */
@RestController
@RequestMapping(path = "/api/gallery")
public class GalleryResource {

    @Inject
    GalleryRepository galleryRepository;

    @Inject
    FileService fileService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Gallery>> getAll(Pageable pageable) throws URISyntaxException {
        Page<Gallery> page = galleryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gallery");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Gallery> uploadPicture(HttpServletRequest request, @RequestPart MultipartFile image) throws URISyntaxException, IOException {
        Gallery gallery = new Gallery();
        String returnStr = fileService.uploadFile(image, "C://gallery");
        gallery.setUri(returnStr);
        Gallery newGallery = galleryRepository.save(gallery);
        return ResponseEntity.ok().body(newGallery);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE, path = "/{id}")
    @Timed
    public void downloadPicture(@PathVariable Long id, HttpServletResponse response) throws URISyntaxException, IOException {
        Gallery gallery = galleryRepository.findOne(id);
        if (gallery != null) {
            fileService.downloadFile(response, "C://gallery", gallery.getUri());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        Gallery gallery = galleryRepository.findOne(id);
        if (gallery != null) {
            gallery.setActive(false);
            galleryRepository.save(gallery);
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
