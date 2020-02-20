package com.upp.naucnacentrala.service;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.client.OrderClient;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.*;
import com.upp.naucnacentrala.model.enums.Enums;
import com.upp.naucnacentrala.repository.jpa.OrderObjectRepository;
import com.upp.naucnacentrala.repository.jpa.SubscriptionRepository;
import com.upp.naucnacentrala.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Autowired
    private MembershipService membershipService;

    public List<OrderDTO> getAllOrders() {
        return orderObjectRepo.findAll().stream().map(o -> OrderDTO.formDto(o)).collect(Collectors.toList());
    }

    public List<OrderDTO> findAllByUserId(String username){
        return orderObjectRepo.findAllByUserId(username).stream().map(o -> OrderDTO.formDto(o)).collect(Collectors.toList());
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
        if (o.getOrderType() == Enums.OrderType.ORDER_SUBSCRIPTION && o.getMagazine() != null) {
            Magazine magazine = magazineService.findOneById(o.getMagazine().getId());
            o.getSubscription().setStartDate(new Date(System.currentTimeMillis()));
            o.getSubscription().setEndDate(foDTO.getFinalDate());
            Date endDate = foDTO.getFinalDate();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dejt = dateFormat.format(endDate);
            Membership membership = new Membership();
            membership.setUsername(o.getUserId());
            membership.setEndDate(dejt);
            membership.setAgreementId(foDTO.getAgreementId());
            membership.setMagazine(magazine);
            membershipService.save(membership);
            magazineService.save(magazine);
        }
        orderObjectRepo.save(o);
    }

}
