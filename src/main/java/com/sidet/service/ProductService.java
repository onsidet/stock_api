package com.sidet.service;

import com.sidet.entity.Product;
import com.sidet.payload.req.ProductReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ProductRes;
import org.springframework.stereotype.Service;


@Service
public interface ProductService {
    Product find(Long id);
    ProductRes create(ProductReq req);
    PaginationRes getAll(String name , int pageNo, int pageSize, String sortBy, String sortDir);
    ProductRes getById(Long id);
    ProductRes update(ProductReq req);
    ProductRes delete(Long id);
    Boolean exists(String name , Long id);
}
