package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

    @RestController
    @RequestMapping("/api")
    class BoardApiController {

        @Autowired
        private BoardRepository repository;

        // http://localhost:8080/api/boards?title=안녕하세요&content=하이
        @GetMapping("/boards") // @RequestParam으로 파라미터를 받을때 값이 들어오지 않으면 에러가 발생 -> required = false 추가
        List<Board> all(@RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String content){
                if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)){
                    return repository.findAll();
                } else{
                    return repository.findByTitleOrContent(title, content);
                }
            }



        @PostMapping("/boards")
        Board newBoard(@RequestBody Board newBoard) {
            return repository.save(newBoard);
        }

        @GetMapping("/boards/{id}")
        Board one(@PathVariable Long id) {

            return repository.findById(id).orElse(null);
        }

        @PutMapping("/boards/{id}")
        Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

            return repository.findById(id)
                    .map(board -> {
                        board.setTitle(newBoard.getTitle());
                        board.setContent(newBoard.getContent());
                        return repository.save(board);
                    })
                    .orElseGet(() -> {
                        newBoard.setId(id);
                        return repository.save(newBoard);
                    });
        }

        @DeleteMapping("/boards/{id}")
        void deleteBoard(@PathVariable Long id) {
            repository.deleteById(id);
        }
    }

