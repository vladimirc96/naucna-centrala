package com.upp.naucnacentrala.client;

import com.upp.naucnacentrala.dto.InitOrderResponseDTO;
import com.upp.naucnacentrala.dto.InitOrderRequestDTO;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.OrderObject;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.model.enums.Enums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderClient {

    private final static String returnUrl = "https://192.168.43.134:8600/orders/finalize";
    private final static String BACKEND_URL = "https://192.168.43.124:8500";

    @Autowired
    RestTemplate restTemplate;

    public InitOrderResponseDTO initOrder(Magazine magazine, OrderObject orderObject){
        InitOrderRequestDTO initOrderRequestDTO = new InitOrderRequestDTO(orderObject.getId(), magazine.getName(), "USD",
                magazine.getSellerId(), orderObject.getAmount(), this.returnUrl, orderObject.getOrderType(), orderObject.getOrderStatus());

        if (orderObject.getOrderType() == Enums.OrderType.ORDER_SUBSCRIPTION) {
            initOrderRequestDTO.setTitle("Subscription for: " + initOrderRequestDTO.getTitle());
        }
        HttpEntity<InitOrderRequestDTO> httpEntity = new HttpEntity<>(initOrderRequestDTO);
        ResponseEntity<InitOrderResponseDTO> responseEntity = restTemplate.postForEntity(this.BACKEND_URL + "/sellers/active-order/init", httpEntity, InitOrderResponseDTO.class);
        return responseEntity.getBody();
    }

    public InitOrderResponseDTO initPaperOrder(SciencePaper paper, OrderObject orderObject){
        InitOrderRequestDTO initOrderRequestDTO = new InitOrderRequestDTO(orderObject.getId(), paper.getTitle(), paper.getCurrency(),
                paper.getMagazine().getSellerId(), orderObject.getAmount(), this.returnUrl, orderObject.getOrderType(), orderObject.getOrderStatus());

        HttpEntity<InitOrderRequestDTO> httpEntity = new HttpEntity<>(initOrderRequestDTO);
        ResponseEntity<InitOrderResponseDTO> responseEntity = restTemplate.postForEntity(this.BACKEND_URL + "/sellers/active-order/init", httpEntity, InitOrderResponseDTO.class);
        return responseEntity.getBody();
    }
}
