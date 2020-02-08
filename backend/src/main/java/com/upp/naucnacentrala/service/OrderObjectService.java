package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.client.OrderClient;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.OrderObject;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.model.Subscription;
import com.upp.naucnacentrala.model.enums.Enums;
import com.upp.naucnacentrala.repository.OrderObjectRepository;
import com.upp.naucnacentrala.repository.SubscriptionRepository;
import com.upp.naucnacentrala.security.TokenUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderObjectService {

    @Autowired
    private MagazineService magazineService;

    @Autowired
    SciencePaperService sciencePaperService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private OrderObjectRepository orderObjectRepo;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private SubscriptionRepository subRepo;

    public List<OrderDTO> getAllOrders() {
        return orderObjectRepo.findAll().stream().map(o -> OrderDTO.formDto(o)).collect(Collectors.toList());
    }

    public InitOrderResponseDTO create(MagazineDTO magazineDTO, HttpServletRequest request){
        Magazine magazine = magazineService.findOneById(magazineDTO.getId());
        OrderObject orderObject = createOrderObject(magazine, request);
        orderObject.setOrderType(Enums.OrderType.ORDER_CASOPIS);
        orderObject = orderObjectRepo.save(orderObject);

        return orderClient.initOrder(magazine, orderObject);
    }

    public InitOrderResponseDTO createSub(MagazineDTO magazineDTO, HttpServletRequest request){
        Magazine magazine = magazineService.findOneById(magazineDTO.getId());
        OrderObject orderObject = createSubscriptionOrderObject(magazine, request);
        orderObject.setOrderType(Enums.OrderType.ORDER_SUBSCRIPTION);
        orderObject = orderObjectRepo.save(orderObject);

        return orderClient.initOrder(magazine, orderObject);
    }



    public InitOrderResponseDTO createPaper(SciencePaperDTO paperDTO, HttpServletRequest request) {
        SciencePaper paper = sciencePaperService.findOneById(paperDTO.getId());
        OrderObject orderObject = new OrderObject();
        orderObject.setSciencePaper(paper);
        orderObject.setUserId(Utils.getUsernameFromRequest(request, tokenUtils));
        orderObject.setAmount(paperDTO.getPrice());
        orderObject.setOrderStatus(Enums.OrderStatus.PENDING);
        orderObject.setOrderType(Enums.OrderType.ORDER_RAD);
        orderObject = orderObjectRepo.save(orderObject);

        return orderClient.initPaperOrder(paper, orderObject);
    }

    private OrderObject createOrderObject(Magazine m, HttpServletRequest request) {
        OrderObject oo = new OrderObject();
        oo.setMagazine(m);
        oo.setUserId(Utils.getUsernameFromRequest(request, tokenUtils));
        oo.setAmount(calculateAmount(m));
        oo.setOrderStatus(Enums.OrderStatus.PENDING);
        return oo;
    }

    private OrderObject createSubscriptionOrderObject(Magazine m, HttpServletRequest request) {
        OrderObject oo = new OrderObject();
        Subscription s = new Subscription();
        s.setMagazine(m);
        s = subRepo.save(s);
        oo.setSubscription(s);
        oo.setUserId(Utils.getUsernameFromRequest(request, tokenUtils));
        oo.setAmount(calculateAmount(m));
        oo.setOrderStatus(Enums.OrderStatus.PENDING);
        return oo;
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

        if (o.getOrderType() == Enums.OrderType.ORDER_SUBSCRIPTION) {
            o.getSubscription().setStartDate(new Date(System.currentTimeMillis()));
            o.getSubscription().setEndDate(foDTO.getFinalDate());

        }
        orderObjectRepo.save(o);

    }

}
