package com.sidet.service.impl;

import com.sidet.entity.Customer;
import com.sidet.entity.Order;
import com.sidet.entity.Staff;
import com.sidet.entity.Store;
import com.sidet.exception.PaginationException;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.OrderReq;
import com.sidet.payload.res.OrderRes;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.payload.res.ShowMessage;
import com.sidet.repository.OrderRepository;
import com.sidet.service.CustomerService;
import com.sidet.service.OrderService;
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
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private ModelMapper mapper;
    private final CustomerService customerService;
    private final StoreService storeService;
    private final StaffService staffService;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper mapper, CustomerService customerService, StoreService storeService, StaffService staffService) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.customerService = customerService;
        this.storeService = storeService;
        this.staffService = staffService;
    }

    @Override
    public Order find(Long id) {
        return orderRepository.getByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.ORDER,"id",id));
    }

    @Override
    public OrderRes create(OrderReq req) {
        Order order = new Order();
        if (req.getCustomerId() == null){
            throw new IllegalArgumentException("order needed a customerId");
        }
        Customer customer = customerService.find(req.getCustomerId());
        order.setCustomer(customer);
        order.setOrderStatus(req.getOrderStatus());
        order.setOrderDate(req.getOrderDate());
        order.setRequiredDate(req.getRequiredDate());
        order.setShippedDate(req.getShippedDate());
        if (req.getStoreId() == null){
            throw new IllegalArgumentException("order needed a storeId");
        }
        Store store = storeService.find(req.getStoreId());
        order.setStore(store);
        if (req.getStaffId() == null){
            throw new IllegalArgumentException("order needed a staffId");
        }
        Staff staff = staffService.find(req.getStaffId());
        order.setStaff(staff);
        order.setStatus(Constants.ACTIVE_STATUS);
        Order create = orderRepository.save(order);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Order> orders = orderRepository.getAllByStatus(Constants.ACTIVE_STATUS,pageable);
        if (orders.getTotalPages()< pageNo){
            throw new PaginationException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ShowMessage.pagination(orders.getTotalPages(),pageNo));
        }
        List<Order> list = orders.getContent();
        List<OrderRes> content = list.stream().map(this::mapToRes).collect(Collectors.toList());
        ParamRes param = new ParamRes();
        param.setPageNo(orders.getNumber() + 1);
        param.setPageSize(orders.getSize());
        param.setTotalPages(orders.getTotalPages());
        param.setTotalElements(orders.getTotalElements());
        param.setLast(orders.isLast());
        return new PaginationRes<>(content,param);
    }

    @Override
    public OrderRes getById(Long id) {
        Order order = find(id);
        return mapToRes(order);
    }

    @Override
    public OrderRes update(OrderReq req) {
        Order order = find(req.getId());
        order.setId(req.getId());
        Customer customer = customerService.find(req.getCustomerId());
        order.setCustomer(customer);
        order.setOrderStatus(req.getOrderStatus());
        order.setShippedDate(req.getShippedDate());
        order.setRequiredDate(req.getRequiredDate());
        order.setOrderDate(req.getOrderDate());
        Staff staff = staffService.find(req.getStaffId());
        order.setStaff(staff);
        Store store = storeService.find(req.getStoreId());
        order.setStore(store);
        order.setStatus(Constants.ACTIVE_STATUS);
        Order update = orderRepository.save(order);
        return mapToRes(update);
    }

    @Override
    public OrderRes delete(Long id) {
        Order order = find(id);
        order.setId(id);
        order.setStatus(Constants.DELETE_STATUS);
        Order delete = orderRepository.save(order);
        return mapToRes(delete);
    }

    //convert Entity to Res
    private OrderRes mapToRes(Order order){
        OrderRes orderRes = mapper.map(order,OrderRes.class);
        return orderRes;
    }
}
