package com.codewithvivek.library.service;

import com.codewithvivek.library.dao.UserDao;
import com.codewithvivek.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class UserService implements UserServiceAble, UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() throws Exception {
        try {
            return userDao.findAll();
        } catch (Exception e) {
            throw new Exception("Unable to fetch users");
        }
    }

    @Override
    public Boolean isAdmin(int id) {
        return userDao.findById(id).filter(user -> user.getIsAdmin() == 1).isPresent();
    }

    @Override
    public void saveUser(User user) throws Exception {
        try {
            if (userDao.findByEmail( user.getEmail())!=null && userDao.findByEmail( user.getEmail()).getEmail() == user.getEmail()) {
                throw new ValidationException("email already exists!");
            }
//            if (!request.getPassword().equals(request.getRePassword())) {
//                throw new ValidationException("Passwords don't match!");
//            }
//            if (request.getAuthorities() == null) {
//                request.setAuthorities(new HashSet<>());
//            }

//            User user = userEditMapper.create(request);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(user.getRole() == null ? "User": "Admin");
            userDao.save(user);
        } catch (Exception e) {
            throw new Exception("Unable to save "+ e.getMessage());
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.findByEmail(s);
    }
}
