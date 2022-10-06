package com.sidet.payload.req;

import lombok.Data;

import java.util.Date;

@Data
public class OrderReq {
    private Long id;
    private Long customerId;
    private int orderStatus;
    private Date orderDate;
    private Date requiredDate;
    private Date shippedDate;
    private Long storeId;
    private Long staffId;
}
