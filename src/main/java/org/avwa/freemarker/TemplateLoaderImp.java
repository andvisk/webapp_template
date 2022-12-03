package org.avwa.freemarker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.avwa.pfUtils.LazyDataModelExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.TemplateLoader;
import jakarta.servlet.ServletContext;

public class TemplateLoaderImp implements TemplateLoader {

    private static Logger log = LoggerFactory.getLogger(LazyDataModelExt.class);

    private ServletContext servletContext;
    private String uri;

    private Reader reader = null;

    public TemplateLoaderImp(ServletContext servletContext, String uri) {
        this.servletContext = servletContext;
        this.uri = uri;
    }

    public Object findTemplateSource(String name) {
        String path = uri + "/" + name;
        if (getUrl(path) != null)
            return path;
        else
            return null;
    }

    public long getLastModified(Object templateSource) {
        long lastMod = retrieveLastModifiedFromURL(getUrl((String) templateSource));
        return lastMod;
    }

    public Reader getReader(Object templateSource, String encoding) {
        try {
            InputStream inputStream = servletContext.getResourceAsStream((String) templateSource);
            if (inputStream != null) {
                reader = new InputStreamReader(inputStream, encoding);
                return reader;
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void closeTemplateSource(Object templateSource) {
        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private URL getUrl(String templateSource) {
        try {
            return servletContext.getResource(templateSource);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private long retrieveLastModifiedFromURL(URL url) {
        try {
            URLConnection con = url.openConnection();
            try {
                return con.getLastModified();
            } catch (Exception ex) {
                return 0L;
            } finally {
                con.getInputStream().close();
            }
        } catch (IOException | NullPointerException e) {
            return 0L;
        }
    }
}
