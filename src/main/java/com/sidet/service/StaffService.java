package com.sidet.service;

import com.sidet.entity.Staff;
import com.sidet.payload.req.StaffReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.StaffRes;
import org.springframework.stereotype.Service;

@Service
public interface StaffService {
    Staff find(Long id);
    StaffRes create(StaffReq req);
    PaginationRes getAll(String fullName, int pageNo, int pageSize, String sortBy, String sortDir);
    StaffRes getById(Long id);
    StaffRes update(StaffReq req);
    StaffRes delete(Long id);
    Boolean exists(Long id, String fullName);
}
