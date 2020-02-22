package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Author;
import com.upp.naucnacentrala.model.Coauthor;
import com.upp.naucnacentrala.model.ScienceField;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.repository.jpa.SciencePaperRepository;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.cs.ISO_8859_2;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class SciencePaperService {

    private final String path = "C:\\GITHUB\\naucna-centrala\\backend\\src\\main\\resources\\files";
    private final Path storageLocation = Paths.get(this.path);

    @Autowired
    SciencePaperRepository sciencePaperRepository;

    @Autowired
    private ScienceFieldService scienceFieldService;

    @Autowired
    private CoauthorService coauthorService;

    public SciencePaper save(SciencePaper sciencePaper){
        return sciencePaperRepository.save(sciencePaper);
    }

    public void remove(SciencePaper sciencePaper){
        sciencePaperRepository.delete(sciencePaper);
    }

    public SciencePaper findOneById(Long id){
        return sciencePaperRepository.findOneById(id);
    }

    public SciencePaper create(SciencePaper sciencePaper,List<FormSubmissionDto> sciencePaperData, ArrayList<Coauthor> coauthorList, Author author) {
        for(FormSubmissionDto dto: sciencePaperData){
            if(dto.getFieldId().equals("naslov_rada")){
                sciencePaper.setTitle(dto.getFieldValue());
            }else if(dto.getFieldId().equals("apstrakt")){
                sciencePaper.setPaperAbstract(dto.getFieldValue());
            }else if(dto.getFieldId().equals("kljucni_pojam")){
                sciencePaper.setKeyTerm(dto.getFieldValue());
            }else if(dto.getFieldId().equals("naucna_oblast")){
                ScienceField scienceField = scienceFieldService.findOneByName(dto.getFieldValue());
                sciencePaper.setScienceField(scienceField);
            }
        }
        for(Coauthor coauthor: coauthorList){
            Coauthor coauthorTemp = coauthorService.findOneById(coauthor.getId());
            sciencePaper.addCoauthor(coauthorTemp);
        }
        sciencePaper.setAuthor(author);
        sciencePaper = sciencePaperRepository.save(sciencePaper);
        return sciencePaper;
    }


    public SciencePaper savePdf(MultipartFile file, SciencePaper sciencePaper){
        try {
            sciencePaper.setPdf(file.getBytes());
            // sacuvaj pdf u resources folderu
            Files.deleteIfExists(this.storageLocation.resolve(sciencePaper.getPdfName()));
            Files.copy(file.getInputStream(), this.storageLocation.resolve(sciencePaper.getPdfName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sciencePaperRepository.save(sciencePaper);
    }

    public String getPath(Long id) throws UnsupportedEncodingException {
        SciencePaper sciencePaper = sciencePaperRepository.findOneById(id);
        return this.path + "\\" + sciencePaper.getPdfName();
    }

}
