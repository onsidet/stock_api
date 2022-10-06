package com.sidet.service.impl;

import com.sidet.entity.Brand;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.BrandReq;
import com.sidet.payload.res.BrandRes;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.repository.BrandRepository;
import com.sidet.service.BrandService;
import com.sidet.utils.Constants;
import com.sidet.utils.Title;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private ModelMapper mapper;

    public BrandServiceImpl(BrandRepository brandRepository, ModelMapper mapper) {
        this.brandRepository = brandRepository;
        this.mapper = mapper;
    }

    @Override
    public BrandRes create(BrandReq req) {
        Brand brand = new Brand();
        brand.setName(req.getName());
        brand.setStatus(Constants.ACTIVE_STATUS);
        Brand create = brandRepository.save(brand);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(String name, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Brand> brands = brandRepository.findAllByStatusAndNameContaining(Constants.ACTIVE_STATUS,name,pageable);

        //get content for page object
        List<Brand> list = brands.getContent();
        List<BrandRes> content = list.stream().map(brand -> mapToRes(brand)).collect(Collectors.toList());
        PaginationRes paginationRes = new PaginationRes();
        paginationRes.setData(content);
            ParamRes param = new ParamRes();
            param.setPageNo(brands.getNumber());
            param.setPageSize(brands.getSize());
            param.setTotalElements(brands.getTotalElements());
            param.setTotalPages(brands.getTotalPages());
            param.setLast(brands.isLast());
        paginationRes.setParam(param);
        return paginationRes;
    }

    @Override
    public BrandRes getById(Long id) {
        Brand brand = brandRepository.findByIdAndStatus(id,Constants.ACTIVE_STATUS)
                .orElseThrow(()-> new ResourceNotFoundException(Title.BRAND, "id",id));
        return mapToRes(brand);
    }

    @Override
    public BrandRes update(BrandReq req) {
        Brand brand = brandRepository.findByIdAndStatus(req.getId(),Constants.ACTIVE_STATUS)
                .orElseThrow(()-> new ResourceNotFoundException(Title.BRAND, "id",req.getId()));
        brand.setId(req.getId());
        brand.setName(req.getName());
        brand.setStatus(Constants.ACTIVE_STATUS);
        Brand update = brandRepository.save(brand);
        return mapToRes(update);
    }

    @Override
    public BrandRes delete(Long id) {
        Brand brand = brandRepository.findByIdAndStatus(id,Constants.ACTIVE_STATUS)
                .orElseThrow(()-> new ResourceNotFoundException(Title.BRAND, "id",id));
        brand.setStatus(Constants.DELETE_STATUS);
        Brand delete = brandRepository.save(brand);
        return mapToRes(delete);
    }

    @Override
    public Boolean exists(String name, Long id) {
        return brandRepository.existsByNameAndIdAndStatus(name,id,Constants.ACTIVE_STATUS);
    }

    @Override
    public Brand find(Long id) {
        return brandRepository.findByIdAndStatus(id,Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.BRAND, "id",id));
    }

    //convert Entity to Res
    private BrandRes mapToRes(Brand brand){
        BrandRes brandRes = mapper.map(brand,BrandRes.class);
        return brandRes;
    }
}
