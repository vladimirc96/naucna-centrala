package com.upp.naucnacentrala;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.SciencePaperService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public class Utils {


    public static HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDto temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

    public static String getUsernameFromRequest(HttpServletRequest request, TokenUtils tokenUtils) {
        String authToken = tokenUtils.getToken(request);
        if (authToken == null) {
            return null;
        }
        String username = tokenUtils.getUsernameFromToken(authToken);
        return username;
    }

    public static String getFormFieldValue(List<FormSubmissionDto> list, String name){
        for(FormSubmissionDto dto: list){
            if(dto.getFieldId().equals(name)){
                return dto.getFieldValue();
            }
        }
        return null;
    }


}
