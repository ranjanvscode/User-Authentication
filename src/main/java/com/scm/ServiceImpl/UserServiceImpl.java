package com.scm.ServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.scm.entities.User;
import com.scm.helper.AppConstant;
import com.scm.helper.ResourceNotFound;
import com.scm.repository.UserRepository;
import com.scm.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public User saveUser(User user) {

        String userId = UUID.randomUUID().toString();

        user.setId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstant.ROLE));
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User updateUser(User user) {

        User user2 = userRepo.findById(user.getId()).orElseThrow(()-> new ResourceNotFound("user not found"));

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setPhoneNo(user.getPhoneNo());
        user2.setProfilePic(user.getProfilePic());
        user2.setEmailVarified(user.isEmailVarified());
        user2.setEnabled(user.isEnabled());
        user2.setPhoneVarified(user.isPhoneVarified());
        user2.setProviderId(user.getProviderId());
        user2.setProvider(user.getProvider());

        return userRepo.save(user);
    }

    @Override
    public void deleteUser(User user) {
        User user2 = userRepo.findById(user.getId()).orElseThrow(()-> new ResourceNotFound("user not found"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExistById(String id) {
        User user2 = userRepo.findById(id).orElse(null);
        return user2 != null;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user2 = userRepo.findByEmail(email).orElse(null);
        return user2 != null;
    }


    @Override
    public Optional<User> getuserByEmailAndPassword(String email,String password)
    {
        User user2 = userRepo.findByEmailAndPassword(email,password).orElse(null);
        return Optional.ofNullable(user2);
    }
}
