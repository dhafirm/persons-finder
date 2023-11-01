package nz.co.dhafir.supplier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Slf4j
@SpringBootApplication
public class SupplierApplication {

	public static void main(String[] args) {
		log.info("Starting Supplier Application");
		SpringApplication.run(SupplierApplication.class, args);
	}

}
