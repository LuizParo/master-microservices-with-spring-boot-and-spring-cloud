package com.in28minutes.microservices.currencyexchangeservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.microservices.currencyexchangeservice.bean.ExchangeValue;
import com.in28minutes.microservices.currencyexchangeservice.repository.ExchangeValueRepository;

@RestController
@RequestMapping(path = "/currency-exchange", method = RequestMethod.GET)
public class CurrencyExchangeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Environment environment;
    private final ExchangeValueRepository repository;

    @Autowired
    public CurrencyExchangeController(Environment environment, ExchangeValueRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    @GetMapping(path = "/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        int port = Integer.parseInt(this.environment.getProperty("local.server.port"));

        ExchangeValue exchangeValue = this.repository.findByFromAndTo(from, to);
        exchangeValue.setPort(port);

        logger.info("{}", exchangeValue);

        return exchangeValue;
    }
}