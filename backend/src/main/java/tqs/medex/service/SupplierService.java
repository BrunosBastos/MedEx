package tqs.medex.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    SupplierRepository supplierRepository;
    public Supplier addSupplier(SupplierPOJO supplierPOJO){
        Supplier s = new Supplier();
        s.setName(supplierPOJO.getName());
        s.setLat(supplierPOJO.getLat());
        return supplierRepository.save(s);
    }
}
