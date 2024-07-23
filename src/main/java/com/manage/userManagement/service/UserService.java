package com.manage.userManagement.service;

import com.manage.userManagement.dto.request.UserRequest;
import com.manage.userManagement.dto.response.UserResponse;
import com.manage.userManagement.entity.User;
import com.manage.userManagement.exception.AppException;
import com.manage.userManagement.exception.ErrorCode;
import com.manage.userManagement.mapper.UserMapper;
import com.manage.userManagement.repository.UserElasticRepository;
import com.manage.userManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserElasticRepository userElasticRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String REDIS_USERS_KEY = "users";
    private static final String REDIS_USER_KEY = "user";
    private static final long CACHE_USER_TTL_MINUTES = 60;
    private static final long CACHE_USERS_TTL_MINUTES = 30;

    @Transactional
    public UserResponse createUser(UserRequest userRequest){
        User user = userMapper.toUser(userRequest);

        try {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRole("USER");
            user = userRepository.save(user);
            userElasticRepository.save(userMapper.toUserDocument(user)); //save in elastic

        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(int id, UserRequest userRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, userRequest);
        userElasticRepository.save(userMapper.toUserDocument(user));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUser(){
        HashOperations<String, String, User> hashOps = redisTemplate.opsForHash();
        List<User> users = hashOps.values(REDIS_USERS_KEY);

        if (users.isEmpty()) {
            users = userRepository.findAll();

            if (!users.isEmpty()) {
                // Lưu vào Redis
                Map<String, User> userMap = users.stream().collect(
                        Collectors.toMap(user->String.valueOf(user.getId()), Function.identity()));
                hashOps.putAll(REDIS_USERS_KEY, userMap);

                redisTemplate.expire(REDIS_USERS_KEY, CACHE_USERS_TTL_MINUTES, TimeUnit.MINUTES);
            }
        }

        return users.stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUser(int id) {
        HashOperations<String, String, User> hashOps = redisTemplate.opsForHash();
        String userIdKey = String.valueOf(id); // Convert Integer key to String
        User user = hashOps.get(REDIS_USER_KEY, userIdKey);

        if (user == null) {
//            user = userRepository.findById(id).orElse(null);
            user = userMapper.toUser(userElasticRepository.findById(userIdKey).orElse(null));//find user in elastic
            
            if (user != null) {
                // Store in Redis
                user.setDob(LocalDate.parse(user.getDob().format(DATE_FORMATTER)));
                hashOps.put(REDIS_USER_KEY, userIdKey, user);
                redisTemplate.expire(REDIS_USER_KEY, CACHE_USER_TTL_MINUTES, TimeUnit.MINUTES);
            } else {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
        }

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> searchByKeyword(String keyword){
        List<User> users = userElasticRepository.findByKeyword(keyword).stream().map(userMapper::toUser).toList();
        
        return users.stream().map(userMapper::toUserResponse).toList();
    }


    public UserResponse getUserByUsername(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

//        User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("User not existed"));
        User user = userMapper.toUser(userElasticRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(int id){
        HashOperations<String, String, User> hashOps = redisTemplate.opsForHash();
        String userIdKey = String.valueOf(id); // Convert Integer key to String
        User user = hashOps.get(REDIS_USER_KEY, userIdKey);

        if (user == null) {
            user = userRepository.findById(id).orElse(null);
            if (user == null) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
        }else{
            hashOps.delete(REDIS_USER_KEY, id);
        }

        userRepository.deleteById(id);
        userElasticRepository.deleteById(userIdKey);
    }
}
