package com.upp.naucnacentrala.service.elasticsearch;

import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.model.SciencePaperES;
import com.upp.naucnacentrala.repository.elasticsearch.SciencePaperESRepository;
import com.upp.naucnacentrala.service.SciencePaperService;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class SciencePaperESService {

    @Autowired
    private SciencePaperESRepository sciencePaperESRepo;

    @Autowired
    private SciencePaperService sciencePaperService;

    public SciencePaperES save(SciencePaperES sciencePaperES){
        return sciencePaperESRepo.save(sciencePaperES);
    }


    public SciencePaperES findOneById(String id) {
        return sciencePaperESRepo.findOneById(id);
    }

    public String parsePDF(SciencePaper sciencePaper) throws UnsupportedEncodingException {
        String path = sciencePaperService.getPath(sciencePaper.getId());
        File pdf = new File(path);
        String text = null;
        try {
            System.out.println("*******************************************");
            System.out.println("Parsiranje PDF-a");
            System.out.println("Path: " + path);
            System.out.println("*******************************************");
            PDFParser parser = new PDFParser(new RandomAccessFile(pdf, "r"));
            parser.parse();
            text = getText(parser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public String getText(PDFParser parser) {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(parser.getPDDocument());
            return text;
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }
        return null;
    }

}
