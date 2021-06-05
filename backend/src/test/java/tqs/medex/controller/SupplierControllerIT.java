package tqs.medex.controller;


import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.repository.SupplierRepository;
import tqs.medex.service.SupplierService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;


@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SupplierControllerIT {


    @Autowired private MockMvc mvc;
    @Autowired
    private SupplierRepository supplierRepository;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    @WithMockUser(value = "test")
    void whenAddSupplier_thenReturnValidResponse(){
        SupplierPOJO supplierPOJO = setUpObjectPOJO();
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(supplierPOJO)
                .post("api/v1/suppliers")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("name", is(supplierPOJO.getName()))
                .and()
                .body("lat", is((float) supplierPOJO.getLat()))
                .and()
                .body("lon", is((float) supplierPOJO.getLon()));
        assertThat(supplierRepository.findAll()).hasSize(1);
    }

    @Test
    @WithMockUser(value = "test")
    void whenAddInvalidSupplier_thenReturnValidResponse(){
        SupplierPOJO supplierPOJO = setUpObjectPOJO();
        Supplier supplier = setUpObject();
        supplierRepository.save(supplier);
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(supplierPOJO)
                .post("api/v1/suppliers")
                .then()
                .assertThat()
                .statusCode(400);
        assertThat(supplierRepository.findAll()).hasSize(1);
    }

    public SupplierPOJO setUpObjectPOJO() {
        return new SupplierPOJO("Pharmacy", 50,50);
    }
    public Supplier setUpObject(){
        Supplier supplier = new Supplier("Pharmacy", -201,50);
        supplier.setId(1L);
        return supplier;
    }
}
