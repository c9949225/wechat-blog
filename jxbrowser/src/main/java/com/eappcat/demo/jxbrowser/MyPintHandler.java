package com.eappcat.demo.jxbrowser;

import com.alibaba.fastjson.JSONObject;
import com.teamdev.jxbrowser.chromium.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.File;
import java.io.IOException;

@Slf4j
public class MyPintHandler implements PrintHandler {
    private DeferredResult<JSONObject> deferredResult;
    public MyPintHandler(DeferredResult<JSONObject> deferredResult){
        this.deferredResult=deferredResult;
    }
    @Override
    public PrintStatus onPrint(PrintJob printJob) {
        PrintSettings settings = printJob.getPrintSettings();
        settings.setPrintToPDF(true);
        settings.setPaperSize(PaperSize.ISO_A4);
        try {
            File tempFile = File.createTempFile("generated",".pdf");
            log.info("print start: {}",tempFile);
            settings.setPDFFilePath(tempFile.getAbsolutePath());
            printJob.addPrintJobListener(new MyPrintJobListener(deferredResult,tempFile));
            return PrintStatus.CONTINUE;
        } catch (IOException e) {
            return PrintStatus.CANCEL;
        }
    }
}
