package com.sidet.controller;

import com.sidet.payload.req.StaffReq;
import com.sidet.payload.res.*;
import com.sidet.service.StaffService;
import com.sidet.utils.Constants;
import com.sidet.utils.Operation;
import com.sidet.utils.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/api/app/staff")
@RestController
public class StaffController {
    private final StaffService staffService;
    private MessageRes messageRes;

    @PostMapping("/create")
    public ResponseEntity<MessageRes> create(@RequestBody StaffReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.STAFF, Operation.CREATED));
        staffService.create(req);
        return new ResponseEntity<>(messageRes, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationRes<StaffRes>> getAll(
            @RequestParam(name = "fullName",defaultValue = "",required = false) String fullName,
            @RequestParam(name = "pageNo", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = Constants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = Constants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        PaginationRes paginationRes = staffService.getAll(fullName,pageNo, pageSize, sortBy,sortDir);
        return ResponseEntity.ok(paginationRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultRes<StaffRes>> getById(@PathVariable(name = "id") Long id){
        ResultRes<StaffRes> resultRes = new ResultRes<StaffRes>();
        resultRes.setData(staffService.getById(id));
        resultRes.setMessage(ShowMessage.success(Title.STAFF,Operation.RETRIEVES));
        return ResponseEntity.ok(resultRes);
    }

    @PutMapping("/update")
    public ResponseEntity<MessageRes> update(@RequestBody StaffReq req){
        messageRes = new MessageRes(ShowMessage.success(Title.STAFF, Operation.UPDATED));
        staffService.update(req);
        return ResponseEntity.ok(messageRes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageRes> delete(@PathVariable(name = "id") Long id){
        messageRes = new MessageRes(ShowMessage.success(Title.STAFF, Operation.DELETED));
        staffService.delete(id);
        return ResponseEntity.ok(messageRes);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(
            @RequestParam(name = "id",defaultValue = "") Long id,
            @RequestParam(name = "fullName", defaultValue = "") String  fullName
    ){
        return ResponseEntity.ok(staffService.exists(id, fullName));
    }
}
