package tqs.medex.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.medex.exception.UserNotFoundException;
import tqs.medex.pojo.ReviewPOJO;
import tqs.medex.repository.PurchaseRepository;
import tqs.medex.repository.UserRepository;
import tqs.medex.service.PurchaseService;
import tqs.medex.service.ReviewService;
import tqs.medex.pojo.ReviewRequestPOJO;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewPOJO> getReview(
            @PathVariable Long id) {
        var purchase = purchaseRepository.findById(id).orElse(null);
        if (purchase == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase not found");
        }
        var review = reviewService.getReview(id);
        if (review == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(review);
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewPOJO> createReview(
            @Valid @RequestBody ReviewRequestPOJO review, Authentication authentication)
            throws UserNotFoundException {
        var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        var purchase = purchaseRepository.findByIdAndUser_UserId(review.getPurchaseId(),user.getUserId()).orElse(null);
        if (purchase == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase not found");
        }
        var review1 = reviewService.addReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(review1);
    }

}
