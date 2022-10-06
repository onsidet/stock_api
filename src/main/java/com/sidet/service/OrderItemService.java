package com.sidet.service;

import com.sidet.payload.req.OrderItemReq;
import com.sidet.payload.res.OrderItemRes;
import com.sidet.payload.res.PaginationRes;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemService {
    OrderItemRes create(OrderItemReq req);
    PaginationRes getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    OrderItemRes getById(Long id);
    OrderItemRes update(OrderItemReq req);
    OrderItemRes delete(Long id);
}
