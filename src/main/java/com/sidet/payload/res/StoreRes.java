package com.sidet.payload.res;

import lombok.Data;

@Data
public class StoreRes {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String location;
}
