package com.sidet.payload.req;

import lombok.Data;

@Data
public class StaffReq {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Boolean active = true;
    private Long storeId;
}
