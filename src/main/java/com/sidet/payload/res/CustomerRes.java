package com.sidet.payload.res;

import lombok.Data;

@Data
public class CustomerRes {
    private Long id;
    private String fullName;
    private String address;
    private String email;
    private String phone;
}
