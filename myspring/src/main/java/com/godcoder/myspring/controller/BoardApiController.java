package com.godcoder.myspring.controller;

import com.godcoder.myspring.model.Board;
import com.godcoder.myspring.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class BoardApiController {


    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/boards")
    List<Board> all(@RequestParam(required = false, defaultValue = "")String title,
                    @RequestParam(required = false, defaultValue = "")String content) {
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)){
            return boardRepository.findAll();
        }else{
            return boardRepository.findByTitleOrContent(title,content);
        }
    }

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board newBoard){
        return boardRepository.save(newBoard);
    }

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id){
        return boardRepository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id){

        return boardRepository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    return boardRepository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    log.info("기존 게시판이 없어서 새로운 게시판에 저장");
                    return boardRepository.save(newBoard);
                });
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("boards/{id}")
    public void deleteBoard(@PathVariable Long id){
        boardRepository.deleteById(id);
    }

}
