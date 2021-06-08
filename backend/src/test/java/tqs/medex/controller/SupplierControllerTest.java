package tqs.medex.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
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

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class SupplierControllerTest {
  @Autowired private MockMvc mvc;
  @MockBean private SupplierService supplierService;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  @WithMockUser(value = "test")
  void whenAddSupplier_thenReturnValidResponse() {
    Supplier supplier = setUpObject();
    SupplierPOJO supplierPOJO = setUpObjectPOJO();
    when(supplierService.addSupplier(Mockito.any(SupplierPOJO.class))).thenReturn(supplier);
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

  @Test
  @WithMockUser(value = "test")
  void whenAddInvalidSupplier_thenReturnBadRequest() {
    SupplierPOJO supplierPOJO = setUpObjectPOJO();
    when(supplierService.addSupplier(Mockito.any(SupplierPOJO.class))).thenReturn(null);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(supplierPOJO)
        .post("api/v1/suppliers")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 There is already supplier with this name.");
    verify(supplierService, times(1)).addSupplier(supplierPOJO);
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetAllSuppliers_thenReturnSuppliers() {
    Supplier supplier = new Supplier(3L, "Pharmacy", 50, 50);
    Supplier supplier2 = new Supplier(4L, "Pharmacy2", 60, 60);
    when(supplierService.getSuppliers()).thenReturn(Arrays.asList(supplier, supplier2));
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/suppliers")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("", hasSize(2))
        .and()
        .body("[0].name", Matchers.is(supplier.getName()))
        .and()
        .body("[1].name", Matchers.is(supplier2.getName()));
    verify(supplierService, times(1)).getSuppliers();
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetSupplierById_thenReturnValidResponse() {
    Supplier supplier = setUpObject();
    when(supplierService.getSupplier(Mockito.anyLong())).thenReturn(supplier);
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/suppliers/1")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("id", Matchers.is(supplier.getId().intValue()))
        .and()
        .body("name", Matchers.is(supplier.getName()));
    verify(supplierService, times(1)).getSupplier(Mockito.anyLong());
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetSupplierByInvalidId_thenReturnBadRequest() {
    when(supplierService.getSupplier(Mockito.anyLong())).thenReturn(null);
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/suppliers/-99")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Supplier Not Found");

    verify(supplierService, times(1)).getSupplier(Mockito.anyLong());
  }

  public SupplierPOJO setUpObjectPOJO() {
    return new SupplierPOJO("Pharmacy", 50, 50);
  }

  public Supplier setUpObject() {
    Supplier supplier = new Supplier(3L, "Pharmacy", 50, 50);
    supplier.setId(1L);
    return supplier;
  }
}
