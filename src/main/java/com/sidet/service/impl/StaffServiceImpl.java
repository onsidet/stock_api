package com.sidet.service.impl;

import com.sidet.entity.Staff;
import com.sidet.entity.Store;
import com.sidet.exception.PaginationException;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.StaffReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.payload.res.ShowMessage;
import com.sidet.payload.res.StaffRes;
import com.sidet.repository.StaffRepository;
import com.sidet.service.StaffService;
import com.sidet.service.StoreService;
import com.sidet.utils.Constants;
import com.sidet.utils.Title;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private ModelMapper mapper;
    private final StoreService storeService;

    public StaffServiceImpl(StaffRepository staffRepository, ModelMapper mapper, StoreService storeService) {
        this.staffRepository = staffRepository;
        this.mapper = mapper;
        this.storeService = storeService;
    }

    @Override
    public Staff find(Long id) {
        return staffRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.STAFF,"id",id));
    }

    @Override
    public StaffRes create(StaffReq req) {
        Staff staff = new Staff();
        staff.setFullName(req.getFullName());
        staff.setEmail(req.getEmail());
        staff.setPhone(req.getPhone());
        staff.setActive(req.getActive());
        if (req.getStoreId() == null){
            throw new IllegalArgumentException("staff needed a storeId");
        }
        Store store = storeService.find(req.getStoreId());
        staff.setStore(store);
        staff.setStatus(Constants.ACTIVE_STATUS);
        Staff create = staffRepository.save(staff);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(String fullName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1,pageSize,sort);
        Page<Staff> staffs = staffRepository.findAllByStatusAndFullNameContaining(Constants.ACTIVE_STATUS,fullName,pageable);
        if (staffs.getTotalPages() < pageNo){
            throw new PaginationException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ShowMessage.pagination(staffs.getTotalPages(),pageNo));
        }
        List<Staff> list = staffs.getContent();
        List<StaffRes> content = list.stream().map(this::mapToRes).collect(Collectors.toList());
        ParamRes param = new ParamRes();
            param.setPageNo(staffs.getNumber() + 1);
            param.setPageSize(staffs.getSize());
            param.setTotalElements(staffs.getTotalElements());
            param.setTotalPages(staffs.getTotalPages());
            param.setLast(staffs.isLast());
        return new  PaginationRes<>(content,param);
    }

    @Override
    public StaffRes getById(Long id) {
        Staff staff = find(id);
        return mapToRes(staff);
    }

    @Override
    public StaffRes update(StaffReq req) {
        Staff staff = find(req.getId());
        staff.setId(req.getId());
        staff.setFullName(req.getFullName());
        staff.setEmail(req.getEmail());
        staff.setPhone(req.getPhone());
        staff.setActive(req.getActive());
        if (req.getStoreId() == null){
            throw new IllegalArgumentException("staff needed a storeId");
        }
        Store store = storeService.find(req.getStoreId());
        staff.setStore(store);
        staff.setStatus(Constants.ACTIVE_STATUS);
        Staff update = staffRepository.save(staff);
        return mapToRes(update);
    }

    @Override
    public StaffRes delete(Long id) {
        Staff staff = find(id);
        staff.setId(id);
        staff.setStatus(Constants.DELETE_STATUS);
        Staff delete = staffRepository.save(staff);
        return mapToRes(delete);
    }

    @Override
    public Boolean exists(Long id, String fullName) {
        return staffRepository.existsByIdAndFullNameAndStatus(id, fullName, Constants.ACTIVE_STATUS);
    }

    private StaffRes mapToRes(Staff staff){
        StaffRes staffRes = mapper.map(staff,StaffRes.class);
        return staffRes;
    }
}
