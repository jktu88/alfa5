package ru.jktu88.alfa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.jktu88.alfa.openapimodels.FinalPriceReceipt;
import ru.jktu88.alfa.openapimodels.PromoMatrix;
import ru.jktu88.alfa.openapimodels.ShoppingCart;
import ru.jktu88.alfa.service.CalculateService;
import ru.jktu88.alfa.webapi.PromoApi;
import ru.jktu88.alfa.webapi.ReceiptApi;

@RestController
public class AppController implements PromoApi, ReceiptApi {
    private final CalculateService calculateService;

    public AppController(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

    @Override
    public Mono<ResponseEntity<FinalPriceReceipt>> calculate(Mono<ShoppingCart> cart, ServerWebExchange exchange) {
        return cart.map(calculateService::calculate);
    }

    @Override
    public Mono<ResponseEntity<Void>> setMatrix(Mono<PromoMatrix> body, ServerWebExchange exchange) {
        return body
                .map(calculateService::setPromo)
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }
}
