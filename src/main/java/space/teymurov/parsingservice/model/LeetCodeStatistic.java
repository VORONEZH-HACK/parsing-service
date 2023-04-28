package space.teymurov.parsingservice.model;

import lombok.Getter;

import java.util.Collections;

import java.util.Map;

@Getter
public class LeetCodeStatistic extends AbstractCodeBattles {
    private final int totalSolved;
    private final int totalQuestions;
    private final int easySolved;
    private final int totalEasy;
    private final int mediumSolved;
    private final int totalMedium;
    private final int hardSolved;
    private final int totalHard;
    private final float acceptanceRate;
    private final int ranking;
    private final int contributionPoints;
    private final int reputation;
    private final Map<String, Integer> submissionCalendar;

    public LeetCodeStatistic(String status, String message, int totalSolved, int totalQuestions, int easySolved, int totalEasy, int mediumSolved, int totalMedium, int hardSolved, int totalHard, float acceptanceRate, int ranking, int contributionPoints, int reputation, Map<String, Integer> submissionCalendar) {
        super(status, message);
        this.totalSolved = totalSolved;
        this.totalQuestions = totalQuestions;
        this.easySolved = easySolved;
        this.totalEasy = totalEasy;
        this.mediumSolved = mediumSolved;
        this.totalMedium = totalMedium;
        this.hardSolved = hardSolved;
        this.totalHard = totalHard;
        this.acceptanceRate = acceptanceRate;
        this.ranking = ranking;
        this.contributionPoints = contributionPoints;
        this.reputation = reputation;
        this.submissionCalendar = submissionCalendar;
    }

    public static LeetCodeStatistic error(String status, String message) {
        return
                new LeetCodeStatistic(status, message, 0,
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, Collections.emptyMap());
    }

    public boolean equals(LeetCodeStatistic rhs) {
        if (rhs == this) return true;

        assert status != null;
        if (!status.equals(rhs.getStatus())) return false;
        assert message != null;
        return message.equals(rhs.getMessage()) &&
                totalSolved == rhs.getTotalSolved() && totalQuestions == rhs.getTotalQuestions() &&
                easySolved == rhs.getEasySolved() && totalEasy == rhs.getTotalEasy() &&
                mediumSolved == rhs.getMediumSolved() && totalMedium == rhs.getTotalMedium() &&
                hardSolved == rhs.getHardSolved() && totalHard == rhs.getTotalHard() &&
                acceptanceRate == rhs.getAcceptanceRate() && ranking == rhs.getRanking() &&
                contributionPoints == rhs.getContributionPoints() && reputation == rhs.getReputation();
    }
}
