package com.surenpi.autotest.report.html.writer;

import com.surenpi.autotest.report.record.NormalRecord;
import com.surenpi.autotest.report.record.ProjectRecord;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;

public class HtmlReportWriterTest {
    @Test
    public void name() throws IOException, TemplateException {
        HtmlReportWriter writer = new HtmlReportWriter();

        writer.setStoreRoot("target");

        NormalRecord record = new NormalRecord();
        record.setModuleName("module A");
        writer.write(record);

        ProjectRecord projectRecord = new ProjectRecord();
        projectRecord.setName("demo project");
        projectRecord.setDescription("This is simple project");
        writer.write(projectRecord);

        writer.saveFile();
    }
}
