package com.sidet.service.impl;

import com.sidet.entity.Order;
import com.sidet.entity.OrderItem;
import com.sidet.entity.Product;
import com.sidet.exception.PaginationException;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.OrderItemReq;
import com.sidet.payload.res.OrderItemRes;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.payload.res.ShowMessage;
import com.sidet.repository.OrderItemRepository;
import com.sidet.service.OrderItemService;
import com.sidet.service.OrderService;
import com.sidet.service.ProductService;
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
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private ModelMapper mapper;
    private final OrderService orderService;
    private final ProductService productService;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ModelMapper mapper, OrderService orderService, ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.mapper = mapper;
        this.orderService = orderService;
        this.productService = productService;
    }

    @Override
    public OrderItemRes create(OrderItemReq req) {
        OrderItem orderItem = new OrderItem();
        Order order = orderService.find(req.getOrderId());
        orderItem.setOrder(order);
        Product product = productService.find(req.getProductId());
        orderItem.setProduct(product);
        orderItem.setDiscount(req.getDiscount());
        orderItem.setQuantity(req.getQuantity());
        orderItem.setPrice(req.getPrice());
        orderItem.setStatus(Constants.ACTIVE_STATUS);
        OrderItem create = orderItemRepository.save(orderItem);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1,pageSize,sort);
        Page<OrderItem> orderItems = orderItemRepository.getAllByStatus(Constants.ACTIVE_STATUS, pageable);
        if (orderItems.getTotalPages() < pageNo){
            throw new PaginationException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ShowMessage.pagination(orderItems.getTotalPages(),pageNo));
        }
        List<OrderItem> list = orderItems.getContent();
        List<OrderItemRes> content = list.stream().map(this::mapToRes).collect(Collectors.toList());
        ParamRes param = new ParamRes();
        param.setPageNo(orderItems.getNumber() + 1);
        param.setPageSize(orderItems.getSize());
        param.setTotalPages(orderItems.getTotalPages());
        param.setTotalElements(orderItems.getTotalElements());
        param.setLast(orderItems.isLast());
        return new PaginationRes<>(content, param);
    }

    @Override
    public OrderItemRes getById(Long id) {
        OrderItem orderItem = orderItemRepository.getByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.ORDER_ITEM,"id",id));
        return mapToRes(orderItem);
    }

    @Override
    public OrderItemRes update(OrderItemReq req) {
        OrderItem orderItem = orderItemRepository.getByIdAndStatus(req.getId(), Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.ORDER_ITEM,"id",req.getId()));
        orderItem.setId(req.getId());
        Order order = orderService.find(req.getOrderId());
        orderItem.setOrder(order);
        Product product = productService.find(req.getProductId());
        orderItem.setProduct(product);
        orderItem.setDiscount(req.getDiscount());
        orderItem.setQuantity(req.getQuantity());
        orderItem.setPrice(req.getPrice());
        orderItem.setStatus(Constants.ACTIVE_STATUS);
        OrderItem update = orderItemRepository.save(orderItem);
        return mapToRes(update);
    }

    @Override
    public OrderItemRes delete(Long id) {
        OrderItem orderItem = orderItemRepository.getByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.ORDER_ITEM,"id",id));
        orderItem.setId(id);
        orderItem.setStatus(Constants.DELETE_STATUS);
        OrderItem delete = orderItemRepository.save(orderItem);
        return mapToRes(delete);
    }

    //convert Entity To Res
    private OrderItemRes mapToRes(OrderItem orderItem){
        OrderItemRes orderItemRes = mapper.map(orderItem, OrderItemRes.class);
        return orderItemRes;
    }
}
