package com.sidet.service;

import com.sidet.entity.Order;
import com.sidet.payload.req.OrderReq;
import com.sidet.payload.res.OrderRes;
import com.sidet.payload.res.PaginationRes;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    Order find(Long id);
    OrderRes create(OrderReq req);
    PaginationRes getAll(int pageNo, int pageSize, String sortBy, String sortDir);
    OrderRes getById(Long id);
    OrderRes update(OrderReq req);
    OrderRes delete(Long id);
}
