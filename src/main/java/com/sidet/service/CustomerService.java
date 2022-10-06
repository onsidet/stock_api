package com.sidet.service;

import com.sidet.entity.Customer;
import com.sidet.payload.req.CustomerReq;
import com.sidet.payload.res.CustomerRes;
import com.sidet.payload.res.PaginationRes;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    Customer find(Long id);
    CustomerRes create(CustomerReq req);
    PaginationRes getAll(String fullName, int pageNo, int pageSize, String sortBy, String sortDir);
    CustomerRes getById(Long id);
    CustomerRes update(CustomerReq req);
    CustomerRes delete(Long id);
    Boolean exists(Long id, String fullName);

}
