package com.sidet.payload.res;

import lombok.Data;

import java.util.Date;

@Data
public class OrderRes {
    private Long id;
    private Long customerId;
    private String customerName;
    private int orderStatus;
    private Date orderDate;
    private Date requiredDate;
    private Date shippedDate;
    private Long storeId;
    private String storeName;
    private Long staffId;
    private String staffName;
}
