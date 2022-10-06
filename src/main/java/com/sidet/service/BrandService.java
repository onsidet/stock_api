package com.sidet.service;

import com.sidet.entity.Brand;
import com.sidet.payload.req.BrandReq;
import com.sidet.payload.res.BrandRes;
import com.sidet.payload.res.PaginationRes;
import org.springframework.stereotype.Service;

@Service
public interface BrandService {
    BrandRes create(BrandReq req);
    PaginationRes getAll(String name, int pageNo, int pageSize, String sortBy, String sortDir);
    BrandRes getById(Long id);
    BrandRes update(BrandReq req);
    BrandRes delete(Long id);
    Boolean exists(String name, Long id);
    Brand find(Long id);
}
