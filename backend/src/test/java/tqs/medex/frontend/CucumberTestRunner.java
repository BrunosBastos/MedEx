package tqs.medex.frontend;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.platform.engine.Cucumber;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tqs.medex.MedExApplication;

@Cucumber
@CucumberOptions(plugin = {"pretty"})
@CucumberContextConfiguration
@SpringBootTest(
        classes = MedExApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"security.basic.enabled=false"})
@AutoConfigureTestDatabase
public class CucumberTestRunner {
}
