package com.sidet.payload.req;

import lombok.Data;

@Data
public class TokenRefreshReq {
    private String refreshToken;
}
