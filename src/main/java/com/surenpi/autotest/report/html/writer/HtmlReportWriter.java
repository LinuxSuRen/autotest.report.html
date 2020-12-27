/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.surenpi.autotest.report.html.writer;

import com.surenpi.autotest.report.RecordReportWriter;
import com.surenpi.autotest.report.ReportStatus;
import com.surenpi.autotest.report.ReportStore;
import com.surenpi.autotest.report.html.model.ExcelReport;
import com.surenpi.autotest.report.record.ExceptionRecord;
import com.surenpi.autotest.report.record.NormalRecord;
import com.surenpi.autotest.report.record.ProjectRecord;
import freemarker.template.*;
import org.modelmapper.ModelMapper;

import javax.annotation.PreDestroy;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel格式报告导出
 * @author <a href="http://surenpi.com">suren</a>
 */
public class HtmlReportWriter implements RecordReportWriter, ReportStore
{
    private ProjectRecord projectRecord;
    private String reportRoot;

    private List<Object> recordCache = new ArrayList<>();

    @Override
    public void write(ExceptionRecord record)
    {
        NormalRecord normalRecord = record.getNormalRecord();

        ModelMapper mapper = new ModelMapper();
        ExcelReport excelReport = mapper.map(normalRecord, ExcelReport.class);
        excelReport.setDetail(record.getStackTraceText());
        excelReport.setStatus(ReportStatus.EXCEPTION.name());
        excelReport.setActions(normalRecord.getActions());

        recordCache.add(excelReport);
    }

    @Override
    public void write(NormalRecord normalRecord)
    {
        recordCache.add(normalRecord);
    }

    @Override
    public void write(ProjectRecord projectRecord)
    {
        this.projectRecord = projectRecord;
    }

    /**
     * 保存文件
     */
    @PreDestroy
    public void saveFile() throws IOException, TemplateException {
        if (this.reportRoot != null) {
            File rootDir = new File(this.reportRoot);
            if (!rootDir.exists()) {
                boolean rootMk = rootDir.mkdirs();
                if (!rootMk) {
                    throw new IOException("cannot create directory " + this.reportRoot);
                }
            }
        }

        Configuration cfg = new Configuration();

        cfg.setClassForTemplateLoading(HtmlReportWriter.class, "/");

        // Some other recommended settings:
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Map<String, Object> input = new HashMap<String, Object>();

        input.put("reports", recordCache);
        input.put("project", this.projectRecord);

        Template template = cfg.getTemplate("report.ftl");


        try (Writer fileWriter = new FileWriter(new File(this.reportRoot, String.format("output-%s.html",
                new SimpleDateFormat("yyyy-MM-dd-HH:mm:SS").format(new Date()))))) {
            template.process(input, fileWriter);
        }
    }

    @Override
    public void setStoreRoot(String root)
    {
        this.reportRoot = root;
    }
}
