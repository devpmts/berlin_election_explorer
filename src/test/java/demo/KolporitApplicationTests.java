package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devpmts.kolporit.KolporitApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = KolporitApplication.class)
@WebAppConfiguration
public class KolporitApplicationTests {

	@Test
	public void contextLoads() {
	}

}
