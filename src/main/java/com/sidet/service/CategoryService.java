package com.sidet.service;

import com.sidet.entity.Category;
import com.sidet.payload.req.CategoryReq;
import com.sidet.payload.res.CategoryRes;
import com.sidet.payload.res.PaginationRes;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryRes create(CategoryReq req);
    PaginationRes getAll(String name, int pageNo, int pageSize, String sortBy, String sortDir);
    CategoryRes getById(Long id);
    CategoryRes update(CategoryReq req);
    CategoryRes delete(Long id);
    Boolean existsByNameAndId(String name,Long id);
    Category find(Long id);
}
