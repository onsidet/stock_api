package com.sidet.payload.req;

import lombok.Data;

@Data
public class RegisterReq {
    private String username;
    private String email;
    private String phone;
    private String password;
}
