package tqs.medex.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.service.SupplierService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
class SupplierControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    @WithMockUser(value = "test")
    void whenAddSupplier_thenReturnValidResponse() {
        Supplier supplier = setUpObject();
        SupplierPOJO supplierPOJO = setUpObjectPOJO();
        when(supplierService.addSupplier(Mockito.any(SupplierPOJO.class))).thenReturn(
                supplier
        );
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(supplierPOJO)
                .post("api/v1/suppliers")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("name", is(supplier.getName()))
                .and()
                .body("lat", is((float) supplier.getLat()))
                .and()
                .body("lon", is((float) supplier.getLon()));
        verify(supplierService, times(1)).addSupplier(supplierPOJO);
    }

    public SupplierPOJO setUpObjectPOJO() {
        return new SupplierPOJO("Pharmacy", 50,50);
    }
    public Supplier setUpObject(){
        Supplier supplier = new Supplier("Pharmacy", 50,50);
        supplier.setId(1L);
        return supplier;
    }
}