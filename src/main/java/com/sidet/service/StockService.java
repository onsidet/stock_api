package com.sidet.service;

import com.sidet.entity.Stock;
import com.sidet.payload.req.StockReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.StockRes;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    Stock find(Long id);
    StockRes create(StockReq req);
    PaginationRes getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    StockRes getById(Long id);
    StockRes update(StockReq req);
    StockRes delete(Long id);
}
