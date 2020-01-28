package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.client.OrderClient;
import com.upp.naucnacentrala.dto.FinalizeOrderDTO;
import com.upp.naucnacentrala.dto.InitOrderResponseDTO;
import com.upp.naucnacentrala.dto.MagazineDTO;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.OrderObject;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.model.enums.Enums;
import com.upp.naucnacentrala.repository.OrderObjectRepository;
import com.upp.naucnacentrala.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
public class OrderObjectService {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private OrderObjectRepository orderObjectRepo;

    @Autowired
    private OrderClient orderClient;

    public InitOrderResponseDTO create(MagazineDTO magazineDTO, HttpServletRequest request){
        Magazine magazine = magazineService.findOneById(magazineDTO.getId());
        OrderObject orderObject = new OrderObject();
        orderObject.setMagazine(magazine);
        orderObject.setOrderType(Enums.OrderType.ORDER_CASOPIS);
        orderObject.setUserId(Utils.getUsernameFromRequest(request, tokenUtils));
        orderObject.setAmount(calculateAmount(magazine));
        orderObject.setOrderStatus(Enums.OrderStatus.PENDING);
        orderObject = orderObjectRepo.save(orderObject);

        return orderClient.initOrder(magazine, orderObject);
    }


    private double calculateAmount(Magazine magazine) {
        double amount = 0;
        for (SciencePaper sciencePaper : magazine.getSciencePapers()) {
            amount = amount + sciencePaper.getPrice();
        }
        amount = amount * 0.95;
        return Math.round(amount * 100.0) / 100.0;
    }

    public void finalizeOrder(FinalizeOrderDTO foDTO) {
        OrderObject o = orderObjectRepo.findById(foDTO.getNcOrderId()).get();
        o.setOrderStatus(foDTO.getOrderStatus());
        orderObjectRepo.save(o);

    }

}
