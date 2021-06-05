package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.service.SupplierService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SupplierController {

    @Autowired
    private SupplierService service;

    @PostMapping("/suppliers")
    public ResponseEntity<Object> addNewSupplier(@Valid @RequestBody SupplierPOJO supplierPOJO){
        var supplier = service.addSupplier(supplierPOJO);
        if (supplier == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is already supplier with this name.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(supplier);
    }

}
