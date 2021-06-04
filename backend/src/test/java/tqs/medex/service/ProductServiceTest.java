package tqs.medex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.medex.entity.Product;
import tqs.medex.entity.Supplier;
import tqs.medex.pojos.ProductPOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.SupplierRepository;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @Mock(lenient = true)
    private SupplierRepository supplierRepository;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        Product product = new Product("ProductTest", 1, 4.99);
        product.setId(1L);
        product.setSupplier(supplier);
        Product product2 = new Product("SecondProduct",5 , 2.99);
        List<Product> productList = Arrays.asList(product,product2);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.findById(product2.getId())).thenReturn(Optional.of(product2));
        when(productRepository.findAll()).thenReturn(productList);
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
    }

    @Test
    void whenAddProduct_thenReturnProduct(){
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(supplierRepository.findById(supplier.getId()))
                .thenReturn(Optional.of(supplier));
        Product product = new Product("ProductTest", 1, 4.99);
        product.setId(1L);
        product.setSupplier(supplier);
        ProductPOJO productPOJO = setUpObjectPOJO();
        Product saved_product = productService.addNewProduct(productPOJO);
        assertThat(saved_product).isNotNull();
        assertThat(saved_product.getId()).isEqualTo(product.getId());
        assertThat(saved_product.getName()).isEqualTo(product.getName());
        assertThat(saved_product.getSupplier()).isEqualTo(product.getSupplier());
        verifyFindSupplierByIdisCalledOnce();
        verifyAddProductisCalledOnce();
    }

    @Test
    void whenAddInvalidProductSupplier_thenReturnNull(){
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        Product product = new Product("ProductTest", 1, 4.99);
        product.setId(1L);
        product.setSupplier(supplier);
        when(supplierRepository.findById(-99L))
                .thenReturn(Optional.empty());
        ProductPOJO productPOJO = setUpInvalidObjectPOJO();
        Product saved_product = productService.addNewProduct(productPOJO);
        assertThat(saved_product).isNull();
        verifyFindSupplierByIdisCalledOnce();
    }


    public ProductPOJO setUpObjectPOJO() {
        return new ProductPOJO("ProductTest", 1, 4.99, 1L);
    }
    public ProductPOJO setUpInvalidObjectPOJO() {
        return new ProductPOJO("ProductTest", 1, 4.99, -99L);
    }
    private void verifyAddProductisCalledOnce(){
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).save(Mockito.any());
    }
    private void verifyFindSupplierByIdisCalledOnce(){
        Mockito.verify(supplierRepository, VerificationModeFactory.times(1)).findById(Mockito.any());
    }
}
