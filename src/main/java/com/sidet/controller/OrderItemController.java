package com.sidet.controller;

import com.sidet.payload.req.OrderItemReq;
import com.sidet.payload.res.*;
import com.sidet.service.OrderItemService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/api/app/orderItem")
@RestController
public class OrderItemController {
    private final OrderItemService orderItemService;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@RequestBody OrderItemReq req){
        MessageRes messageRes = new MessageRes(ShowMessage.success(Title.ORDER_ITEM, Operation.CREATED));
        orderItemService.create(req);
        return new  ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<OrderItemRes>> getAll(
            @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = Constants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = Constants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        PaginationRes paginationRes = orderItemService.getAll(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(paginationRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<OrderItemRes>> getById(@PathVariable(name = "id") Long id){
        ResultRes<OrderItemRes> resultRes = new ResultRes<>();
        resultRes.setData(orderItemService.getById(id));
        resultRes.setMessage(ShowMessage.success(Title.ORDER_ITEM,Operation.RETRIEVES));
        return ResponseEntity.ok(resultRes);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@RequestBody OrderItemReq req){
        MessageRes messageRes = new MessageRes();
        messageRes.setMessage(ShowMessage.success(Title.ORDER_ITEM,Operation.UPDATED));
        orderItemService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        MessageRes messageRes = new MessageRes();
        messageRes.setMessage(ShowMessage.success(Title.ORDER_ITEM,Operation.DELETED));
        orderItemService.delete(id);
        return ResponseEntity.ok(messageRes);
    }
}
