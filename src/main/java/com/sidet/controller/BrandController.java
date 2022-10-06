package com.sidet.controller;

import com.sidet.payload.req.BrandReq;
import com.sidet.payload.res.*;
import com.sidet.service.BrandService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/app/brand")
@RestController
public class BrandController {
    private final BrandService brandService;
    private MessageRes messageRes;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@Valid @RequestBody BrandReq req) {
        messageRes = new MessageRes(ShowMessage.success(Title.BRAND, Operation.CREATED));
        brandService.create(req);
        return new ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<BrandRes>> getAll(
            @RequestParam(name = "name", defaultValue = "", required = false) String name,
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "dsc", required = false) String sortDir
    ) {
        PaginationRes brands = brandService.getAll(name, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<BrandRes>> getById(@PathVariable(name = "id") Long id) {
        ResultRes<BrandRes> resultRes = new ResultRes<BrandRes>();
        resultRes.setData(brandService.getById(id));
        resultRes.setMessage(ShowMessage.success(Title.BRAND, Operation.RETRIEVES));
        return ResponseEntity.ok(resultRes);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@Valid @RequestBody BrandReq req) {
        messageRes = new MessageRes(ShowMessage.success(Title.BRAND, Operation.UPDATED));
        brandService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id) {
        messageRes = new MessageRes(ShowMessage.success(Title.BRAND, Operation.DELETED));
        brandService.delete(id);
        return ResponseEntity.ok(messageRes);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam(name = "id", defaultValue = "") Long id,
            @RequestParam(name = "name", defaultValue = "") String name
    ) {
        return ResponseEntity.ok(brandService.exists(name, id));
    }
}
