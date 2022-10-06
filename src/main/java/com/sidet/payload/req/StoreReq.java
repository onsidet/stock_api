package com.sidet.payload.req;

import lombok.Data;

@Data
public class StoreReq {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String location;
}
