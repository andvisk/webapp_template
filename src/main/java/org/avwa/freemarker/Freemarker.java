package org.avwa.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.avwa.pfUtils.LazyDataModelExt;
import org.avwa.system.ApplicationEJB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Freemarker {

    private static Logger log = LoggerFactory.getLogger(LazyDataModelExt.class);

    public static String process(ApplicationEJB app, Map<String, Object> values, String templateName) {
        try {
            Template template = app.getCfgFreeMarker().getTemplate(templateName);
            StringWriter stringWriter = new StringWriter();
            template.process(values, stringWriter);
            String result = stringWriter.toString();
            return result;
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
