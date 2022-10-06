package com.sidet.payload.req;

import lombok.Data;

import java.util.Date;

@Data
public class ProductReq {
    private Long id;
    private String name;
    private Long brandId;
    private Long categoryId;
    private Date productionDate;
    private Double price;
}
