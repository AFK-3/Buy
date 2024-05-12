package id.ac.ui.cs.advprog.buy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BuyApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void contextTest() {
		BuyApplication.main(new String[] {});
	}

}
