package com.sidet.service.impl;

import com.sidet.entity.Store;
import com.sidet.exception.PaginationException;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.StoreReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.payload.res.ShowMessage;
import com.sidet.payload.res.StoreRes;
import com.sidet.repository.StoreRepository;
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
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private ModelMapper mapper;

    public StoreServiceImpl(StoreRepository storeRepository, ModelMapper mapper) {
        this.storeRepository = storeRepository;
        this.mapper = mapper;
    }

    @Override
    public Store find(Long id) {
        return storeRepository.findByIdAndStatus(id,Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.STORE,"id",id));
    }

    @Override
    public StoreRes create(StoreReq req) {
        Store store = new Store();
        store.setName(req.getName());
        store.setLocation(req.getLocation());
        store.setEmail(req.getEmail());
        store.setPhone(req.getPhone());
        store.setStatus(Constants.ACTIVE_STATUS);
        Store create = storeRepository.save(store);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(String name, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo-1,pageSize,sort);
        Page<Store> stores = storeRepository.findAllByNameContainingAndStatus(name,Constants.ACTIVE_STATUS,pageable);
        //get content for page object
        List<Store> list = stores.getContent();
        List<StoreRes> content = list.stream().map(store -> mapToRes(store)).collect(Collectors.toList());
        if (stores.getTotalPages()< pageNo){
            throw new PaginationException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ShowMessage.pagination(stores.getTotalPages(),pageNo));
        }
        ParamRes paramRes = new ParamRes();
            paramRes.setPageNo(stores.getNumber()+1);
            paramRes.setPageSize(stores.getSize());
            paramRes.setTotalPages(stores.getTotalPages());
            paramRes.setTotalElements(stores.getTotalElements());
            paramRes.setLast(stores.isLast());
        PaginationRes paginationRes = new  PaginationRes(content,paramRes);
        return paginationRes;
    }

    @Override
    public StoreRes getById(Long id) {
        Store store = find(id);
        return mapToRes(store);
    }

    @Override
    public StoreRes update(StoreReq req) {
        Store store = find(req.getId());
        store.setId(req.getId());
        store.setName(req.getName());
        store.setLocation(req.getLocation());
        store.setPhone(req.getPhone());
        store.setEmail(req.getEmail());
        store.setStatus(Constants.ACTIVE_STATUS);
        Store update = storeRepository.save(store);
        return mapToRes(update);
    }

    @Override
    public StoreRes delete(Long id) {
        Store store = find(id);
        store.setStatus(Constants.DELETE_STATUS);
        Store delete = storeRepository.save(store);
        return mapToRes(delete);
    }

    @Override
    public Boolean exists(Long id, String name) {
        return storeRepository.existsByIdAndNameAndStatus(id, name, Constants.ACTIVE_STATUS);
    }

    //convert Entity to Res
    private StoreRes mapToRes(Store store){
        StoreRes storeRes = mapper.map(store,StoreRes.class);
        return storeRes;
    }
}
