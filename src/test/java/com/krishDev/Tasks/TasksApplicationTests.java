package com.krishDev.Tasks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.krishDev.Tasks.models.User;

import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TasksApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TasksApplicationTests {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	Date date;

	@Before
	public void before() {
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	@Test
	public void getUsersTest() {
		String url = createURLWithPort("/users");

		HttpEntity entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		String expected = "[{id:1,username:Bob,email:a@a.com,password:DummyPassword,tasks:[],dob:1994-10-26},"
				+ "{id:9,username:krishna,email:k@k.com,password:DummyPassword,tasks:[],dob:1994-10-26}]";

		try {
			JSONAssert.assertEquals(expected, response.getBody(), false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
	@Test
	// public void getUserByIdTest() {
	// 	String url = createURLWithPort("/users");
	// 	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	// 	User user = new User("dummyTest","a@a.com", new Date(), "testPassword", Arrays.asList());
	// 	HttpEntity entity = new HttpEntity<User>(user, headers);
	// 	ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	// 	String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

	// 	System.out.println(actual);

	// 	assertTrue(actual.contains("/users/"));
	// }

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
