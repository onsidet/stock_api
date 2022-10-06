package com.sidet.controller;

import com.sidet.payload.req.OrderReq;
import com.sidet.payload.res.*;
import com.sidet.service.OrderService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/api/app/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@RequestBody OrderReq req){
        MessageRes messageRes = new MessageRes(ShowMessage.success(Title.ORDER, Operation.CREATED));
        orderService.create(req);
        return new ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<OrderRes>> getAll(
            @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        PaginationRes paginationRes = orderService.getAll(pageNo, pageSize,sortBy, sortDir);
        return ResponseEntity.ok(paginationRes);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<OrderRes>> getById(@PathVariable(name = "id") Long id){
        ResultRes<OrderRes> resultRes = new ResultRes<>();
        resultRes.setMessage(ShowMessage.success(Title.ORDER,Operation.RETRIEVES));
        resultRes.setData(orderService.getById(id));
        return ResponseEntity.ok(resultRes);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@RequestBody OrderReq req){
        MessageRes messageRes = new MessageRes(ShowMessage.success(Title.ORDER,Operation.UPDATED));
        orderService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        MessageRes messageRes = new MessageRes(ShowMessage.success(Title.ORDER, Operation.DELETED));
        orderService.delete(id);
        return ResponseEntity.ok(messageRes);
    }
}
