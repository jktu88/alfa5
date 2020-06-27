package ru.jktu88.alfa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jktu88.alfa.openapimodels.FinalPricePosition;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private String groupId;
    private BigDecimal price;

    public FinalPricePosition toPosition(Integer quantity, Double discount) {

        BigDecimal regularPrice = this.price.multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal price = regularPrice.multiply(BigDecimal.valueOf(1 - discount))
                .setScale(2, RoundingMode.HALF_UP);

        return new FinalPricePosition()
                .id(id)
                .name(name)
                .regularPrice(regularPrice)
                .price(price);
    }
}
