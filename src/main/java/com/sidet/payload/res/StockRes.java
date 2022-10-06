package com.sidet.payload.res;

import lombok.Data;

@Data
public class StockRes {
    private Long id;
    private Long productId;
    private String productName;
    private Long storeId;
    private String storeName;
    private Long quantity;
}
