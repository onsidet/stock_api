package com.sidet.service;

import com.sidet.entity.Store;
import com.sidet.payload.req.StoreReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.StoreRes;
import org.springframework.stereotype.Service;

@Service
public interface StoreService {
    Store find(Long id);
    StoreRes create(StoreReq req);
    PaginationRes getAll(String name, int pageNo, int pageSize,String sortBy, String sortDir);
    StoreRes getById(Long id);
    StoreRes update(StoreReq req);
    StoreRes delete(Long id);
    Boolean exists(Long id, String name);

}
