package com.godcoder.myspring.service;


import com.godcoder.myspring.model.Board;
import com.godcoder.myspring.model.User;
import com.godcoder.myspring.repository.BoardRepository;
import com.godcoder.myspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board) {
        User user = userRepository.findByUsername(username);
        board.setUser(user);
        return boardRepository.save(board);
    }
}
