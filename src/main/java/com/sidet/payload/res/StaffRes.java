package com.sidet.payload.res;

import lombok.Data;

@Data
public class StaffRes {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Boolean active;
    private Long storeId;
    private String storeName;
}
