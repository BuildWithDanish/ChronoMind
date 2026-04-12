package com.danish.chronoMind.service;

import com.danish.chronoMind.entity.User;
import com.danish.chronoMind.repository.UserRepository;
import com.danish.chronoMind.services.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class UserDetailServiceImplTest {

    @InjectMocks
    private UserDetailServiceImpl userDetailServiceImpl;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsernameTest() {
        when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(User.builder().username("Danish").password("rwsvdsvs").roles(new ArrayList<>()).build());
        userDetailServiceImpl.loadUserByUsername("Bhai");
    }
}
