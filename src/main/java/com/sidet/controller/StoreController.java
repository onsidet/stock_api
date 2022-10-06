package com.sidet.controller;

import com.sidet.payload.req.StoreReq;
import com.sidet.payload.res.*;
import com.sidet.service.StoreService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/app/store")
@RestController
public class StoreController {
    private final StoreService storeService;
    private MessageRes messageRes;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@RequestBody StoreReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.STORE, Operation.CREATED));
        storeService.create(req);
        return new  ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<StoreRes>> getAll(
            @RequestParam(value = "name",defaultValue = "",required = false) String name,
            @RequestParam(value = "pageNo",defaultValue = Constants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = Constants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = Constants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        PaginationRes paginationRes = storeService.getAll(name,pageNo,pageSize,sortBy, sortDir);
        return ResponseEntity.ok(paginationRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<StoreRes>> getById(@PathVariable(name = "id") Long id){
        ResultRes<StoreRes> resultRes = new ResultRes<StoreRes>();
        resultRes.setData(storeService.getById(id));
        resultRes.setMessage(ShowMessage.success(Title.STORE,Operation.RETRIEVES));
        return ResponseEntity.ok(resultRes);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@RequestBody StoreReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.STORE,Operation.UPDATED));
        storeService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        messageRes = new MessageRes(ShowMessage.success(Title.STORE,Operation.DELETED));
        storeService.delete(id);
        return ResponseEntity.ok(messageRes);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam(name = "id",defaultValue = "") Long id,
            @RequestParam(name = "name", defaultValue = "") String name
    ){
        return ResponseEntity.ok(storeService.exists(id,name));
    }
}
