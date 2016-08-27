/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.ip.zndf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author milan
 */
@Component
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    public void downloadFile(HttpServletResponse response, String path, String fileName) throws IOException {
        fileName = fileName.replaceAll("\\.\\.", "");
        log.debug("Download file name: {}", fileName);
        File file = new File(path, fileName);
        log.debug("Download file path: {}", file.getAbsoluteFile());
        InputStream is = new FileInputStream(file);

        // MIME type of the file
        response.setContentType("application/octet-stream");
        // Response header
        response.setHeader("Content-Disposition", "attachment; filename=\""
                           + file.getName() + "\"");
        // Read from the file and write into the response
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }

    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFileName = file.getOriginalFilename();
        log.debug("Uploading originalFileName: {} at path: {}", originalFileName, path);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        String newFileName = dateFormat.format(new Date()) + originalFileName;
        Files.copy(file.getInputStream(), Paths.get(path, newFileName));
        return newFileName;
    }

}
