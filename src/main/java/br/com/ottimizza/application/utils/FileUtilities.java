package br.com.ottimizza.application.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;

public class FileUtilities {

    public String getContentType(Resource resource, HttpServletRequest request) throws Exception {
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        return (contentType == null) ? "application/octet-stream" : contentType;
    }

    public String getContentDisposition(Resource resource, String contentDisposition) throws Exception {
        return String.format("%s;filename=\"%s\"", contentDisposition, resource.getFilename());
    }

    public String getContentDisposition(Resource resource) throws Exception {
        return getContentDisposition(resource, "inline");
    }

}