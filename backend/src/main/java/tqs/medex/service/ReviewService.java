package tqs.medex.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.medex.pojo.ReviewPOJO;
import tqs.medex.pojo.ReviewRequestPOJO;

@Service
public class ReviewService {

    @Autowired
    private ExternalService externalService;

    public ReviewPOJO getReview(Long purchaseId){

        return externalService.getReview(purchaseId);
    }

    public ReviewPOJO addReview(ReviewRequestPOJO reviewPOJO){

        return externalService.createReview(reviewPOJO);
    }
}
