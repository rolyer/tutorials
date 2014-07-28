package dbunit.tutorial.service;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

import dbunit.tutorial.entity.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:app-test-context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@ActiveProfiles("hsqldb")
public class PersonServiceTest {

	@Autowired
	private PersonService personService;

	@Test
	@DatabaseSetup("classpath:data/sampleData.xml")
	public void testFind() throws Exception {
		List<Person> personList = personService.find("hil");
		assertEquals(1, personList.size());
		assertEquals("Phillip", personList.get(0).getFirstName());
	}

	@Test
	@DatabaseSetup("classpath:data/sampleData.xml")
	@ExpectedDatabase("classpath:data/expectedData.xml")
	public void testRemove() throws Exception {
		personService.remove(1);
	}

}
