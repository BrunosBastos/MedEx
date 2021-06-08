package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tqs.medex.entity.CustomUserDetails;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.service.PurchaseService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class PurchaseController {

    @Autowired
    private PurchaseService orderService;

    @PostMapping("/purchases")
    public ResponseEntity<Object> addNewProduct(@Valid @RequestBody CreatePurchasePOJO order, Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) principal;

        var newOrder = orderService.addNewOrder(order, userDetails.getUser().getUserId());
        if (newOrder == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Not Found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }


}
