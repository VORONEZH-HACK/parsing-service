package space.teymurov.parsingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.teymurov.parsingservice.model.CodeForcesStatistic;
import space.teymurov.parsingservice.model.LeetCodeStatistic;
import space.teymurov.parsingservice.service.ParsingService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/")
public class ParsingController {
    @Autowired
    private ParsingService parsingService;

    @GetMapping
    @RequestMapping("leetcode/{username}")
    public LeetCodeStatistic getLeetCode(@PathVariable String username) {
        return parsingService.getLeetCodeStatistic(username);
    }

    @GetMapping
    @RequestMapping("codeforces/{username}")
    public CodeForcesStatistic getCodeForce(@PathVariable String username) {
        return parsingService.getCodeForcesStatistic(username);
    }
}
