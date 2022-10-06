package com.sidet.payload.req;

import com.sidet.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
public class CategoryReq {
    private Long id;
    @Size(min = 2, message = Constants.MIN_SIZE_2)
    @NotEmpty()
    private String name;
}
