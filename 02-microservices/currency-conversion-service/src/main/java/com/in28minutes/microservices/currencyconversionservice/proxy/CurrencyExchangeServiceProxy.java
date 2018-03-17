package com.in28minutes.microservices.currencyconversionservice.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.in28minutes.microservices.currencyconversionservice.bean.CurrencyConvertionBean;

@FeignClient(name = "netflix-zuul-api-gateway-server")
@RibbonClient(name = "currency-exchange-service")
//@FeignClient(name = "currency-exchange-service")
//@FeignClient(name = "currency-exchange-service", value = "localhost:8000")
public interface CurrencyExchangeServiceProxy {

    @GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
    //@GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConvertionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
}