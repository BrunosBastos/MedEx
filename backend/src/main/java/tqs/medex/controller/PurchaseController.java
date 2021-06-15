package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.medex.entity.Purchase;
import tqs.medex.exception.UserNotFoundException;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.UserRepository;
import tqs.medex.service.PurchaseService;
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PurchaseController {

  @Autowired private PurchaseService orderService;

  @Autowired private UserRepository userRepository;



  @GetMapping("/purchases")
  public ResponseEntity <List<Purchase>> getAllPurchases( Authentication authentication) throws UserNotFoundException {
    var user =
            userRepository
                    .findByEmail(authentication.getName())
                    .orElseThrow(UserNotFoundException::new);
    var purchases = orderService.getPurchases(user);
    return ResponseEntity.status(HttpStatus.OK).body(purchases);
  }
  @GetMapping("/purchases/{id}")
  public ResponseEntity<Purchase> getPurchase (Authentication authentication, @PathVariable Long id) throws UserNotFoundException {
    var usr = userRepository.findByEmail(authentication.getName())
            .orElseThrow(UserNotFoundException::new);
    var purchase = orderService.getPurchaseDetails(usr, id);
    if (purchase == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase not found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(purchase);
  }

  @PostMapping("/purchases")
  public ResponseEntity<Purchase> addNewProduct(
      @Valid @RequestBody CreatePurchasePOJO order, Authentication authentication)
      throws UserNotFoundException {
    var user =
        userRepository
            .findByEmail(authentication.getName())
            .orElseThrow(UserNotFoundException::new);
    var newOrder = orderService.addNewPurchase(order, user);
    if (newOrder == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Product Quantity");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
  }
}
