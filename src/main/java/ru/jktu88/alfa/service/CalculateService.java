package ru.jktu88.alfa.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.jktu88.alfa.model.Item;
import ru.jktu88.alfa.openapimodels.FinalPricePosition;
import ru.jktu88.alfa.openapimodels.FinalPriceReceipt;
import ru.jktu88.alfa.openapimodels.ShoppingCart;

import javax.swing.text.Position;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalculateService {

    private final Map<String, Item> items;

    public CalculateService(Map<String, Item> items) {
        this.items = items;
    }

    public ResponseEntity<FinalPriceReceipt> calculate(ShoppingCart shoppingCart) {
        var receipt = new FinalPriceReceipt();
        FinalPricePosition positionsItem = new FinalPricePosition();

        receipt.setPositions(shoppingCart.getPositions().stream()
                .map(it -> items.get(it.getItemId()).toPosition(it.getQuantity()))
                .collect(Collectors.toList()));

        receipt.setDiscount(BigDecimal.ZERO);

        receipt.setTotal(receipt.getPositions().stream()
                .map(FinalPricePosition::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }
}
