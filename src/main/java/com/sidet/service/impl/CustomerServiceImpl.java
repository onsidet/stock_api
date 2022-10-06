package com.sidet.service.impl;

import com.sidet.entity.Customer;
import com.sidet.exception.PaginationException;
import com.sidet.exception.ResourceNotFoundException;
import com.sidet.payload.req.CustomerReq;
import com.sidet.payload.res.CustomerRes;
import com.sidet.payload.res.PaginationRes;
import com.sidet.payload.res.ParamRes;
import com.sidet.payload.res.ShowMessage;
import com.sidet.repository.CustomerRepository;
import com.sidet.service.CustomerService;
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
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private ModelMapper mapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    public Customer find(Long id) {
        return customerRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS)
                .orElseThrow(() -> new ResourceNotFoundException(Title.CUSTOMER,"id",id));
    }

    @Override
    public CustomerRes create(CustomerReq req) {
        Customer customer = new Customer();
        customer.setFullName(req.getFullName());
        customer.setAddress(req.getAddress());
        customer.setEmail(req.getEmail());
        customer.setPhone(req.getPhone());
        customer.setStatus(Constants.ACTIVE_STATUS);
        Customer create = customerRepository.save(customer);
        return mapToRes(create);
    }

    @Override
    public PaginationRes getAll(String fullName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1 ,pageSize,sort);
        Page<Customer> customers = customerRepository.findAllByStatusAndFullNameContaining(Constants.ACTIVE_STATUS,fullName,pageable);
        if (customers.getTotalPages() < pageNo){
            throw new PaginationException(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ShowMessage.pagination(customers.getTotalPages(),pageNo));
        }
        List<Customer> list = customers.getContent();
        List<CustomerRes> content = list.stream()
                .map(this::mapToRes).collect(Collectors.toList());
        ParamRes param = new ParamRes();
            param.setPageNo(customers.getNumber() + 1);
            param.setPageSize(customers.getSize());
            param.setTotalPages(customers.getTotalPages());
            param.setTotalElements(customers.getTotalElements());
            param.setLast(customers.isLast());
        return new PaginationRes<>(content, param);
    }

    @Override
    public CustomerRes getById(Long id) {
        Customer customer = find(id);
        return mapToRes(customer);
    }

    @Override
    public CustomerRes update(CustomerReq req) {
        Customer customer = find(req.getId());
        customer.setId(req.getId());
        customer.setFullName(req.getFullName());
        customer.setAddress(req.getAddress());
        customer.setEmail(req.getEmail());
        customer.setPhone(req.getPhone());
        customer.setStatus(Constants.ACTIVE_STATUS);
        Customer update = customerRepository.save(customer);
        return mapToRes(update);
    }

    @Override
    public CustomerRes delete(Long id) {
        Customer customer = find(id);
        customer.setId(id);
        customer.setStatus(Constants.DELETE_STATUS);
        Customer delete = customerRepository.save(customer);
        return mapToRes(delete);
    }

    @Override
    public Boolean exists(Long id, String fullName) {
        return customerRepository.existsByIdAndFullNameAndStatus(id,fullName,Constants.ACTIVE_STATUS);
    }

    //convert Entity to Res
    private CustomerRes mapToRes(Customer customer){
        CustomerRes customerRes = mapper.map(customer,CustomerRes.class);
        return customerRes;
    }
}
