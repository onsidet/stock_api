package com.sidet.payload.res;

import lombok.Data;

import java.util.Date;

@Data
public class ProductRes {
    private Long id;
    private String name;

    private Long brandId;

    private String brandName;

    private Long categoryId;

    private String categoryName;

    private Date productionDate;
    private Double price;
}
