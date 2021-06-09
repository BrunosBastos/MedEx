package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tqs.medex.exception.UserNotFoundException;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.UserRepository;
import tqs.medex.service.PurchaseService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PurchaseController {

    @Autowired
    private PurchaseService orderService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/purchases")
    public ResponseEntity<Object> addNewProduct(@Valid @RequestBody CreatePurchasePOJO order, Authentication authentication) throws UserNotFoundException {
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        var newOrder = orderService.addNewPurchase(order, user);
        if (newOrder == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Product Quantity");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);


    }


}
