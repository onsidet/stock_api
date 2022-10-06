package com.sidet.controller;

import com.sidet.payload.req.CustomerReq;
import com.sidet.payload.res.*;
import com.sidet.service.CustomerService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/app/customer")
@RestController
public class CustomerController {
    private final CustomerService customerService;
    private MessageRes messageRes;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@RequestBody CustomerReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.CUSTOMER, Operation.CREATED));
        customerService.create(req);
        return new  ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<CustomerRes>> getAll(
            @RequestParam(name = "fullName",defaultValue = "",required = false) String fullName,
            @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = Constants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIRECTION) String sortDir
    ){
        PaginationRes paginationRes = customerService.getAll(fullName,pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(paginationRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<CustomerRes>> getById(@PathVariable(name = "id") Long id){
        ResultRes<CustomerRes> result = new ResultRes<>();
        result.setData(customerService.getById(id));
        result.setMessage(ShowMessage.success(Title.CUSTOMER,Operation.RETRIEVES));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@RequestBody CustomerReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.CUSTOMER,Operation.UPDATED));
        customerService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        messageRes = new MessageRes(ShowMessage.success(Title.CUSTOMER,Operation.DELETED));
        customerService.delete(id);
        return ResponseEntity.ok(messageRes);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam(name = "id", defaultValue = "") Long id,
            @RequestParam(name = "fullName",defaultValue = "") String fullName
    ){
        return ResponseEntity.ok(customerService.exists(id, fullName));
    }


}
