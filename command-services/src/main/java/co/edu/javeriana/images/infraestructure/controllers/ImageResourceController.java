package co.edu.javeriana.images.infraestructure.controllers;

import co.edu.javeriana.images.application.ImagesCommandService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ImageResourceController {

    private static final Logger LOG = LoggerFactory.getLogger(ImageResourceController.class);

    private String TPL_TEMPLATE_FILE_PATH = "%s%s";

    @Value("${images.files.upload.directory}")
    private String FILES_PATH;

    private final ImagesCommandService service;

    @GetMapping("/load/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = null;
        if(fileName !=null && !fileName.isEmpty()) {
            try {
                resource = service.download(fileName);
                LOG.info("PATH_IMAGE >>> {}", String.format(this.TPL_TEMPLATE_FILE_PATH, this.FILES_PATH, fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                LOG.info("RESOURCE_IMAGE >>> {}", resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                LOG.error("Could not determine file type.", ex);
            }

            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
