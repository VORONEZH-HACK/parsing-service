package space.teymurov.parsingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CodeForcesStatistic extends AbstractCodeBattles {
    private final int rating;
    private final String rank;
    private final String organization;
    private final int maxRating;
    private final String maxRank;

    public CodeForcesStatistic(String status, String message, int rating,
                               String rank, String organization, int maxRating, String maxRank) {
        super(status, message);
        this.rating = rating;
        this.rank = rank;
        this.organization = organization;
        this.maxRating = maxRating;
        this.maxRank = maxRank;
    }

    public static CodeForcesStatistic error(String status, String message) {
        return
                new CodeForcesStatistic(status, message, 0,
                        "", "", 0, "");
    }

    public boolean equals(CodeForcesStatistic rhs) {
        if (rhs == this) return true;
        return message.equals(rhs.message) &&
                rating == rhs.rating &&
                rank.equals(rhs.rank) &&
                organization.equals(rhs.organization) &&
                maxRating == rhs.maxRating && maxRank.equals(rhs.maxRank);
    }
}
