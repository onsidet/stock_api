package com.sidet.controller;

import com.sidet.payload.req.CategoryReq;
import com.sidet.payload.res.*;
import com.sidet.service.CategoryService;
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
@RestController
@RequestMapping("/api/app/category")
public class CategoryController {
    private final CategoryService categoryService;
    private ResultRes<CategoryRes> resultRes;
    private MessageRes messageRes;
    // create rest api
    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@Valid @RequestBody CategoryReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.CATEGORY, Operation.CREATED));
        categoryService.create(req);
        return new  ResponseEntity<>(messageRes,HttpStatus.CREATED);
    }
    // get all rest api
    @GetMapping("/list")
    public ResponseEntity<PaginationRes<CategoryRes>> getAll(
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "dsc", required = false) String sortDir
    )
    {
        PaginationRes categories = categoryService.getAll(name, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(categories);
    }
    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<CategoryRes>> getById(@PathVariable(name = "id") Long id){
        resultRes = new ResultRes<>(
                categoryService.getById(id),
                ShowMessage.success(Title.CATEGORY,Operation.RETRIEVES)
        );
        return ResponseEntity.ok(resultRes);
    }

    // update by id rest api
    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@Valid @RequestBody CategoryReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.CATEGORY, Operation.UPDATED));
        categoryService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    // delete rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        messageRes = new MessageRes(ShowMessage.success(Title.CATEGORY,Operation.DELETED));
        categoryService.delete(id);
        return ResponseEntity.ok(messageRes);
    }

    // exists rest api
    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(@RequestParam(defaultValue = "") Long id,
                                          @RequestParam(defaultValue = "") String name){
        return ResponseEntity.ok(categoryService.existsByNameAndId(name, id));
    }
}
