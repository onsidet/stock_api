package com.sidet.service.impl;

import com.sidet.entity.Product;
import com.sidet.entity.Stock;
import com.sidet.entity.Store;
import com.sidet.exception.PaginationException;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.StockReq;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.payload.res.ShowMessage;
import com.sidet.payload.res.StockRes;
import com.sidet.repository.StockRepository;
import com.sidet.service.ProductService;
import com.sidet.service.StockService;
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
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private ModelMapper mapper;
    private final StoreService storeService;
    private final ProductService productService;

    public StockServiceImpl(StockRepository stockRepository, ModelMapper mapper, StoreService storeService, ProductService productService) {
        this.stockRepository = stockRepository;
        this.mapper = mapper;
        this.storeService = storeService;
        this.productService = productService;
    }

    @Override
    public Stock find(Long id) {
        return stockRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.STOCK,"id",id));
    }

    @Override
    public StockRes create(StockReq req) {
        Stock stock = new Stock();
        if (req.getProductId() == null){
            throw new IllegalArgumentException("stock needed a productId");
        }
        Product product = productService.find(req.getProductId());
        stock.setProduct(product);
        if (req.getStoreId() == null){
            throw new IllegalArgumentException("stock needed a storeId");
        }
        Store store = storeService.find(req.getStoreId());
        stock.setStore(store);
        stock.setQuantity(req.getQuantity());
        stock.setStatus(Constants.ACTIVE_STATUS);
        Stock create = stockRepository.save(stock);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Stock> stocks = stockRepository.findAllByStatus(Constants.ACTIVE_STATUS,pageable);

        if (stocks.getTotalPages() < pageNo){
            throw new PaginationException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ShowMessage.pagination(
                    stocks.getTotalPages(), pageNo
            ));
        }

        List<Stock> list = stocks.getContent();
        List<StockRes> content = list.stream().map(stock -> mapToRes(stock)).collect(Collectors.toList());
        ParamRes param = new ParamRes();
            param.setPageNo(stocks.getNumber() + 1);
            param.setPageSize(stocks.getSize());
            param.setTotalElements(stocks.getTotalElements());
            param.setTotalPages(stocks.getTotalPages());
            param.setLast(stocks.isLast());
        PaginationRes paginationRes = new PaginationRes(content,param);
        return paginationRes;
    }

    @Override
    public StockRes getById(Long id) {
        Stock stock = find(id);
        return mapToRes(stock);
    }

    @Override
    public StockRes update(StockReq req) {
        Stock stock = find(req.getId());
        stock.setId(req.getId());
        if (req.getProductId() == null){
            throw new IllegalArgumentException("stock needed a productId");
        }
        Product product = productService.find(req.getProductId());
        stock.setProduct(product);
        if (req.getStoreId() == null){
            throw new IllegalArgumentException("stock needed a storeId");
        }
        Store store = storeService.find(req.getStoreId());
        stock.setStore(store);
        stock.setQuantity(req.getQuantity());
        stock.setStatus(Constants.ACTIVE_STATUS);

        Stock update = stockRepository.save(stock);
        return mapToRes(update);
    }

    @Override
    public StockRes delete(Long id) {
        Stock stock = find(id);
        stock.setId(id);
        stock.setStatus(Constants.DELETE_STATUS);
        Stock delete = stockRepository.save(stock);
        return mapToRes(delete);
    }

    //convert Entity To Res
    private StockRes mapToRes(Stock stock){
        StockRes stockRes = mapper.map(stock,StockRes.class);
        return stockRes;
    }
}
