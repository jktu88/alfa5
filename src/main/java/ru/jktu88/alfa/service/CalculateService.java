package ru.jktu88.alfa.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.jktu88.alfa.model.Item;
import ru.jktu88.alfa.openapimodels.*;

import javax.swing.text.Position;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CalculateService {

    private final Map<String, Item> items;
    private volatile Map<Integer, Double> promoMap;

    public CalculateService(Map<String, Item> items) {
        this.items = items;
    }

    public ResponseEntity<FinalPriceReceipt> calculate(ShoppingCart shoppingCart) {
        var receipt = new FinalPriceReceipt();

        Double globalDiscount = promoMap.getOrDefault(-1, 0.0);
        Double shopDiscount = promoMap.getOrDefault(shoppingCart.getShopId(), 0.0);
        Double discount = shoppingCart.getLoyaltyCard()
                ? (shopDiscount > globalDiscount ? shopDiscount : globalDiscount)
                : globalDiscount;

        receipt.setPositions(shoppingCart.getPositions().stream()
                .map(it -> items.get(it.getItemId()).toPosition(it.getQuantity(), discount))
                .collect(Collectors.toList()));

        receipt.setTotal(receipt.getPositions().stream()
                .map(FinalPricePosition::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        receipt.setDiscount(receipt.getPositions().stream()
                .map(it -> it.getRegularPrice().subtract(it.getPrice()))
                .reduce(BigDecimal.ZERO,BigDecimal::add));

        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    public Mono<Object> setPromo(PromoMatrix promoMatrix) {
        promoMap = Optional.ofNullable(promoMatrix.getLoyaltyCardRules())
                .map(it -> it.stream()
                        .collect(Collectors.toMap(LoyaltyCardRule::getShopId, LoyaltyCardRule::getDiscount)))
                .orElse(Collections.emptyMap());

        return Mono.empty();
    }
}
