package com.sidet.service.impl;

import com.sidet.entity.Brand;
import com.sidet.entity.Category;
import com.sidet.entity.Product;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.ProductReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.payload.res.ProductRes;
import com.sidet.repository.ProductRepository;
import com.sidet.service.BrandService;
import com.sidet.service.CategoryService;
import com.sidet.service.ProductService;
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
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private ModelMapper mapper;
    private final BrandService brandService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper mapper, BrandService brandService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @Override
    public Product find(Long id) {
        return productRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.PRODUCT, "id",id));
    }

    @Override
    public ProductRes create(ProductReq req) {
        Product product = new Product();
        product.setName(req.getName());
        if (req.getBrandId() == null) {
            throw new IllegalArgumentException("product needed a brandId");
        }
        Brand brand = brandService.find(req.getBrandId());
        product.setBrand(brand);
        if (req.getCategoryId() == null) {
            throw new IllegalArgumentException("product needed a categoryId");
        }
        Category category = categoryService.find(req.getCategoryId());
        product.setCategory(category);
        product.setProductionDate(req.getProductionDate());
        product.setPrice(req.getPrice());
        product.setStatus(Constants.ACTIVE_STATUS);
        Product create = productRepository.save(product);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(String name, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> products = productRepository.findAllByStatusAndNameContaining(Constants.ACTIVE_STATUS, name, pageable);
        // get content for page object
        List<Product> list = products.getContent();
        List<ProductRes> content = list.stream().map(product -> mapToRes(product)).collect(Collectors.toList());
        ParamRes param = new ParamRes();
            param.setPageNo(products.getNumber());
            param.setPageSize(products.getSize());
            param.setTotalElements(products.getTotalElements());
            param.setTotalPages(products.getTotalPages());
            param.setLast(products.isLast());
        PaginationRes paginationRes = new PaginationRes(content, param);
        return paginationRes;
    }

    @Override
    public ProductRes getById(Long id) {
        Product product = productRepository.findByIdAndStatus(id,Constants.ACTIVE_STATUS)
                .orElseThrow(()-> new ResourceNotFoundException(Title.PRODUCT,"id",id));
        return mapToRes(product);
    }

    @Override
    public ProductRes update(ProductReq req) {
        Product product = productRepository.findByIdAndStatus(req.getId(),Constants.ACTIVE_STATUS)
                .orElseThrow(()-> new ResourceNotFoundException(Title.PRODUCT,"id",req.getId()));
        product.setId(req.getId());
        product.setName(req.getName());
        if (req.getBrandId() == null){
            throw new IllegalArgumentException("product needed a brandId");
        }
        Brand brand = brandService.find(req.getBrandId());
        product.setBrand(brand);
        if (req.getCategoryId() == null){
            throw new IllegalArgumentException("product needed a categoryId");
        }
        Category category = categoryService.find(req.getCategoryId());
        product.setCategory(category);
        product.setPrice(req.getPrice());
        product.setProductionDate(req.getProductionDate());
        product.setStatus(Constants.ACTIVE_STATUS);
        Product update = productRepository.save(product);
        return mapToRes(update);
    }

    @Override
    public ProductRes delete(Long id) {
        Product product = productRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.PRODUCT,"id",id));
        product.setStatus(Constants.DELETE_STATUS);
        Product delete = productRepository.save(product);
        return mapToRes(delete);
    }

    @Override
    public Boolean exists(String name, Long id) {
        return productRepository.existsByNameAndIdAndStatus(name,id,Constants.ACTIVE_STATUS);
    }


    //convert Entity to Res
    private ProductRes mapToRes(Product product) {
        ProductRes productRes = mapper.map(product, ProductRes.class);
        return productRes;
    }
}
