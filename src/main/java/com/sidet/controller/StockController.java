package com.sidet.controller;

import com.sidet.payload.req.StockReq;
import com.sidet.payload.res.*;
import com.sidet.service.StockService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/app/stock")
@RestController
public class StockController {

    private final StockService stockService;
    private MessageRes messageRes;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@RequestBody StockReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.STOCK, Operation.CREATED));
        stockService.create(req);
        return new ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<StockRes>> getAll(
            @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = Constants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = Constants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        PaginationRes paginationRes = stockService.getAll(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(paginationRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<StockRes>> getById(@PathVariable(name = "id") Long id){
        ResultRes<StockRes> resultRes = new ResultRes<>();
        resultRes.setData(stockService.getById(id));
        resultRes.setMessage(ShowMessage.success(Title.STOCK,Operation.RETRIEVES));
        return ResponseEntity.ok(resultRes);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@RequestBody StockReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.STOCK,Operation.UPDATED));
        stockService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        messageRes = new MessageRes(ShowMessage.success(Title.STOCK,Operation.DELETED));
        stockService.delete(id);
        return ResponseEntity.ok(messageRes);
    }
}
