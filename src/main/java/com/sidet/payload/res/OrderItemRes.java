package com.sidet.payload.res;

import lombok.Data;

@Data
public class OrderItemRes {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Long quantity;
    private Double price;
    private Double discount;
}
