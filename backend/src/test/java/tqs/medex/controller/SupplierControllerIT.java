package tqs.medex.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.SupplierPOJO;
import tqs.medex.repository.SupplierRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SupplierControllerIT {

  @Autowired private MockMvc mvc;
  @Autowired private SupplierRepository supplierRepository;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  @WithMockUser(value = "test")
  void whenAddSupplier_thenReturnValidResponse() {
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

    assertThat(supplierRepository.findByName(supplierPOJO.getName())).isPresent();
  }

  @Test
  @WithMockUser(value = "test")
  void whenAddInvalidSupplier_thenReturnInvalidResponse() {
    SupplierPOJO supplierPOJO = setInvalidUpObjectPOJO();

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(supplierPOJO)
        .post("api/v1/suppliers")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetSuppliers_thenReturnValidResponse() {
    Supplier supplier = getExistingSupplier();
    Supplier supplier2 = getExistingSupplier2();

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
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetSupplierById_thenReturnValidResponse() {
    Supplier supplier = getExistingSupplier();
    long id = supplier.getId();

    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/suppliers/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("id", Matchers.is(supplier.getId().intValue()))
        .and()
        .body("name", Matchers.is(supplier.getName()));
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetSupplierByInvalidId_thenReturnBadRequest() {
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/suppliers/-99")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Supplier Not Found");
  }

  public SupplierPOJO setUpObjectPOJO() {
    return new SupplierPOJO("New Pharmacy", 50, 50);
  }

  public SupplierPOJO setInvalidUpObjectPOJO() {
    return new SupplierPOJO("Pharmacy", 50, 50);
  }

  public Supplier getExistingSupplier() {
    return supplierRepository.findByName("Pharmacy").orElse(null);
  }

  public Supplier getExistingSupplier2() {
    return supplierRepository.findByName("Pharmacy2").orElse(null);
  }
}
