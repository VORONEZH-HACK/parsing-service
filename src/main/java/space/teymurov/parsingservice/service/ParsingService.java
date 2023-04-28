package space.teymurov.parsingservice.service;


import space.teymurov.parsingservice.model.CodeForcesStatistic;
import space.teymurov.parsingservice.model.LeetCodeStatistic;

public interface ParsingService {
    LeetCodeStatistic getLeetCodeStatistic(String username);
    CodeForcesStatistic getCodeForcesStatistic(String username);
}
