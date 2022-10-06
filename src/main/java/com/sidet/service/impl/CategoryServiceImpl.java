package com.sidet.service.impl;

import com.sidet.entity.Category;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.CategoryReq;
import com.sidet.payload.res.CategoryRes;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.repository.CategoryRepository;
import com.sidet.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryRes create(CategoryReq req) {
        Category category = new Category();
        category.setName(req.getName());
        category.setStatus(Constants.ACTIVE_STATUS);
        categoryRepository.save(category);
        return mapToRes(category);
    }

    @Override
    public PaginationRes getAll(String name, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Category> categories = categoryRepository.findAllByStatusAndNameContaining(Constants.ACTIVE_STATUS,name,pageable);
        // get content for page object
        List<Category> list = categories.getContent();

        List<CategoryRes> content = list.stream().map(category -> mapToRes(category)).collect(Collectors.toList());
        PaginationRes paginationRes = new PaginationRes();
        paginationRes.setData(content);
        ParamRes paramRes = new ParamRes(
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isLast());
        paginationRes.setParam(paramRes);
        return paginationRes;
    }

    @Override
    public CategoryRes getById(Long id){
        Category category = categoryRepository.findByStatusAndId(Constants.ACTIVE_STATUS,id)
                .orElseThrow(()-> new  ResourceNotFoundException("Category", "id", id));
        return mapToRes(category);
    }

    @Override
    public CategoryRes update(CategoryReq req) {
        // get post by id from the database
        Category category = categoryRepository.findByStatusAndId(Constants.ACTIVE_STATUS,req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", req.getId()));
        category.setName(req.getName());
        category.setStatus(Constants.ACTIVE_STATUS);

        Category update = categoryRepository.save(category);
        return mapToRes(update);
    }

    @Override
    public CategoryRes delete(Long id) {
        // get post by id from the database
        Category category = categoryRepository.findByStatusAndId(Constants.ACTIVE_STATUS,id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setStatus(Constants.DELETE_STATUS);
        Category delete = categoryRepository.save(category);
        return mapToRes(delete);
    }

    @Override
    public Boolean existsByNameAndId(String name, Long id) {
        return categoryRepository.existsByNameAndIdAndStatus(name, id, Constants.ACTIVE_STATUS);
    }

    @Override
    public Category find(Long id) {
        return categoryRepository.findByStatusAndId(Constants.ACTIVE_STATUS, id)
                .orElseThrow(()-> new  ResourceNotFoundException(Title.CATEGORY,"id",id));
    }

    // convert Entity to Res
    private CategoryRes mapToRes(Category category){
        CategoryRes categoryRes = mapper.map(category, CategoryRes.class);
        return categoryRes;
    }



}
