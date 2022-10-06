package com.sidet.payload.req;

import lombok.Data;

@Data
public class StockReq {
    private Long id;
    private Long productId;
    private Long storeId;
    private Long quantity;
}
