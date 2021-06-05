package tqs.medex.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.repository.SupplierRepository;

import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    SupplierRepository supplierRepository;
    public Supplier addSupplier(SupplierPOJO supplierPOJO){
        var s = new Supplier();
        Optional<Supplier> existentobj = supplierRepository.findByName(supplierPOJO.getName());
        if(existentobj.isPresent()){
            return null;
        }
        s.setName(supplierPOJO.getName());
        s.setLat(supplierPOJO.getLat());
        s.setLon(supplierPOJO.getLon());
        return supplierRepository.save(s);
    }
}
