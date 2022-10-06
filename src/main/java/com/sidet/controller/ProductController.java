package com.sidet.controller;

import com.sidet.payload.req.ProductReq;
import com.sidet.payload.res.*;
import com.sidet.service.ProductService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/app/product")
@RestController
public class ProductController {
    private final ProductService productService;
    private MessageRes messageRes;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@RequestBody ProductReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.PRODUCT, Operation.CREATED));
        productService.create(req);
        return new ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<ProductRes>> getAll(
            @RequestParam(name = "name",defaultValue = "",required = false) String name,
            @RequestParam(name = "pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = "id",required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = "dsc", required = false) String sortDir
    ){
        PaginationRes products = productService.getAll(name,pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<ProductRes>> getById(@PathVariable(name = "id") Long id){
        ResultRes<ProductRes> resultRes = new ResultRes<ProductRes>();
        resultRes.setData(productService.getById(id));
        resultRes.setMessage(ShowMessage.success(Title.PRODUCT,Operation.RETRIEVES));
        return ResponseEntity.ok(resultRes);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@RequestBody ProductReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.PRODUCT,Operation.UPDATED));
        productService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        messageRes = new MessageRes(ShowMessage.success(Title.PRODUCT,Operation.DELETED));
        productService.delete(id);
        return ResponseEntity.ok(messageRes);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam(name = "id",defaultValue = "") Long id,
            @RequestParam(name = "name",defaultValue = "") String name
    ){
        return ResponseEntity.ok(productService.exists(name , id));
    }

}
