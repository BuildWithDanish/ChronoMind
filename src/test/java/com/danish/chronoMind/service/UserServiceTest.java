package com.danish.chronoMind.service;

//import com.danish.journalApp.entity.User;
//import com.danish.journalApp.services.UserService;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//public class UserServiceTest {
//
//
//    @MockBean
//    UserService userService;
//
//    @ParameterizedTest
//    @ArgumentsSource(UserArgumentProvider.class)
//    public void saveUserTest(User user) {
//        assertTrue(userService.save(user));
//    }
//
//    @Disabled
//    @ParameterizedTest
//    @CsvFileSource(resources = "/test-users.csv",  numLinesToSkip = 1)
//    public void deleteUserTest(String username, String password) {
//        assertTrue(userService.deleteByUsername(username));
//    }
//}
