package com.godcoder.myspring.controller;

import com.godcoder.myspring.model.Board;
import com.godcoder.myspring.model.User;
import com.godcoder.myspring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserApiController {


    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    List<User> all(@RequestParam(required = false)String method, @RequestParam(required = false)String text) {
        List<User> users = null;
        if ("query".equals(method)) {
            users = userRepository.findByUsernameQuery(text);
        } else {
            users = userRepository.findAll();
        }
        return users;
    }


    @PostMapping("/users")
    User newUser(@RequestBody User newUser){
        return userRepository.save(newUser);
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id){
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id){

        return userRepository.findById(id)
                .map(user -> {
//                    user.setTitle(newUser.getTitle());
//                    user.setContent(newUser.getContent());
//                    user.setBoards(newUser.getBoards());
                    user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());
                    for(Board board : user.getBoards()) {
                        board.setUser(user);
                    }
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    log.info("기존 게시판이 없어서 새로운 게시판에 저장");
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("users/{id}")
    public void deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
    }

}
