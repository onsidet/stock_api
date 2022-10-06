package com.sidet.payload.req;

import lombok.Data;

@Data
public class CustomerReq {
    private Long id;
    private String fullName;
    private String address;
    private String email;
    private String phone;
}
