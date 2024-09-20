package org.example.springv4.User;

import org.example.springv4.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;


}
