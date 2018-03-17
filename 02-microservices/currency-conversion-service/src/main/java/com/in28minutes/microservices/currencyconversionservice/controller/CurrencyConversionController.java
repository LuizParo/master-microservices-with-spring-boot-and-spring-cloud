package com.in28minutes.microservices.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.in28minutes.microservices.currencyconversionservice.bean.CurrencyConvertionBean;
import com.in28minutes.microservices.currencyconversionservice.proxy.CurrencyExchangeServiceProxy;

@RestController
@RequestMapping(method = RequestMethod.GET)
public class CurrencyConversionController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CurrencyExchangeServiceProxy proxy;

    @Autowired
    public CurrencyConversionController(CurrencyExchangeServiceProxy proxy) {
        this.proxy = proxy;
    }

    @GetMapping(path = "/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConvertionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConvertionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConvertionBean.class, uriVariables);
        CurrencyConvertionBean response = responseEntity.getBody();

        BigDecimal totalCalculatedAmount = quantity.multiply(response.getConversionMultiple());

        logger.info("{}", response);

        return new CurrencyConvertionBean(
            response.getId(),
            from,
            to,
            response.getConversionMultiple(),
            quantity,
            totalCalculatedAmount,
            response.getPort()
        );
    }

    @GetMapping(path = "/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConvertionBean convertCurrencyWithFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        CurrencyConvertionBean response = this.proxy.retrieveExchangeValue(from, to);
        BigDecimal totalCalculatedAmount = quantity.multiply(response.getConversionMultiple());

        logger.info("{}", response);

        return new CurrencyConvertionBean(
            response.getId(),
            from,
            to,
            response.getConversionMultiple(),
            quantity,
            totalCalculatedAmount,
            response.getPort()
        );
    }
}