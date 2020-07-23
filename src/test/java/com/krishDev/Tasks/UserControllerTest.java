package com.krishDev.Tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import com.krishDev.Tasks.controllers.UserController;
import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    LocalDate date;

    // @Before
	// public void beforeGetUserById() throws ParseException {
    //     DateTimeFormatter fIn = DateTimeFormatter.ofPattern( "uuuu/MM/dd" , Locale.US );
    //     date = LocalDate.parse(new LocalDate(), fIn);
	// }

    // @Test
    // public void getUsersTest() throws Exception {
    //     User mockUser = new User(10L,"dummyTest", "a@a.com", date, "testPassword", Arrays.asList());
    //     Optional<User> opt = Optional.ofNullable(mockUser);
    //     String expected = "{id:10,username:dummyTest,email:a@a.com,password:testPassword,tasks:[],dob:2020-07-21}";
    //     Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(opt);

    //     // GET
    //     RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/10").accept(MediaType.APPLICATION_JSON);
        
    //     MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    //     System.out.println("test result - " +result);
    //     JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        
    // }

    @Test
	public void addUserTest() {
		// User mockeUser = new User(8L, "createUserTest","t@t.com", )
	}
}