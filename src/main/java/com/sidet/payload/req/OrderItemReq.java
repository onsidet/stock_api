package com.sidet.payload.req;

import lombok.Data;

@Data
public class OrderItemReq {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long quantity;
    private Double price;
    private Double discount;
}
