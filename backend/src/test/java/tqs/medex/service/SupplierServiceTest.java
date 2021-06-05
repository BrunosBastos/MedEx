package tqs.medex.service;

import org.junit.jupiter.api.BeforeEach;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.repository.SupplierRepository;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock(lenient = true)
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    @BeforeEach
    void setUp(){
        Supplier supplier = new Supplier("Pharmacy", 50,50);
        supplier.setId(1L);
        Supplier supplier2 = new Supplier("AnotherPharmacy", 60,60);
        List<Supplier> suppliers = Arrays.asList(supplier,supplier2);
        when(supplierRepository.findById(supplier.getId())).thenReturn(Optional.of(supplier));
        when(supplierRepository.findAll()).thenReturn(suppliers);
        when(supplierRepository.save(Mockito.any(Supplier.class))).thenReturn(supplier);

    }

    @Test
    void whenAddSupplier_thenReturnSupplier(){
        Supplier supplier = setUpObject();
        SupplierPOJO supplierPOJO = setUpObjectPOJO();
        when(supplierRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Supplier saved_supplier = supplierService.addSupplier(supplierPOJO);
        assertThat(saved_supplier).isNotNull();
        assertThat(saved_supplier.getName()).isEqualTo(supplier.getName());
        assertThat(saved_supplier.getLat()).isEqualTo(supplier.getLat());
        assertThat(saved_supplier.getLon()).isEqualTo(supplier.getLon());
        verifyAddSupplierisCalledOnce();
        verifyFindByNameisCalledOnce();
    }
    @Test
    void whenAddInvalidSupplier_thenReturnNull(){
        Supplier supplier = setUpObject();
        SupplierPOJO supplierPOJO = setUpObjectPOJO();
        when(supplierRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(supplier));
        Supplier saved_supplier = supplierService.addSupplier(supplierPOJO);
        assertThat(saved_supplier).isNull();
        verifyFindByNameisCalledOnce();

    }
    public SupplierPOJO setUpObjectPOJO() {
        return new SupplierPOJO("Pharmacy", 50,50);
    }
    public Supplier setUpObject(){
        Supplier supplier = new Supplier("Pharmacy", 50,50);
        supplier.setId(1L);
        return supplier;
    }

    private void verifyAddSupplierisCalledOnce() {
        Mockito.verify(supplierRepository, VerificationModeFactory.times(1)).save(Mockito.any(Supplier.class));
    }
    private void verifyFindByNameisCalledOnce(){
        Mockito.verify(supplierRepository, VerificationModeFactory.times(1)).findByName(Mockito.anyString());
    }
}