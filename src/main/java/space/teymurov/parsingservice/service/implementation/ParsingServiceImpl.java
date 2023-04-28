package space.teymurov.parsingservice.service.implementation;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import space.teymurov.parsingservice.model.CodeForcesStatistic;
import space.teymurov.parsingservice.model.LeetCodeStatistic;
import space.teymurov.parsingservice.service.ParsingService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Service
public class ParsingServiceImpl implements ParsingService {

    @Override
    public LeetCodeStatistic getLeetCodeStatistic(String username) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        String query = String.format("{\"query\":\"query getUserProfile($username: String!) { allQuestionsCount { difficulty count } matchedUser(username: $username) { contributions { points } profile { reputation ranking } submissionCalendar submitStats { acSubmissionNum { difficulty count submissions } totalSubmissionNum { difficulty count submissions } } } } \",\"variables\":{\"username\":\"%s\"}}", username);
        RequestBody requestBody = RequestBody.create(query, mediaType);
        Request request = new Request.Builder()
                .url("https://leetcode.com/graphql/")
                .method("POST", requestBody)
                .addHeader("referer", String.format("https://leetcode.com/%s/", username))
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);

            if (response.isSuccessful()) {
                if (jsonObject.has("errors"))
                    return LeetCodeStatistic.error("error", "user does not exist");
                else
                    return decodeLeetCode(jsonObject);
            } else
                return LeetCodeStatistic.error("error", jsonObject.getString("error"));
        } catch (IOException | JSONException e) {
            return LeetCodeStatistic.error("error", e.getMessage());
        }
    }


    private LeetCodeStatistic decodeLeetCode(JSONObject json) {
        int totalSolved = 0;
        int totalQuestions = 0;
        int easySolved = 0;
        int totalEasy = 0;
        int mediumSolved = 0;
        int totalMedium = 0;
        int hardSolved = 0;
        int totalHard = 0;
        float acceptanceRate = 0;
        int ranking = 0;
        int contributionPoints = 0;
        int reputation = 0;

        final Map<String, Integer> submissionCalendar = new TreeMap<>();

        try {
            JSONObject data = json.getJSONObject("data");
            JSONArray allQuestions = data.getJSONArray("allQuestionsCount");
            JSONObject matchedUser = data.getJSONObject("matchedUser");
            JSONObject submitStats = matchedUser.getJSONObject("submitStats");
            JSONArray actualSubmissions = submitStats.getJSONArray("acSubmissionNum");
            JSONArray totalSubmissions = submitStats.getJSONArray("totalSubmissionNum");

            totalQuestions = allQuestions.getJSONObject(0).getInt("count");
            totalEasy = allQuestions.getJSONObject(1).getInt("count");
            totalMedium = allQuestions.getJSONObject(2).getInt("count");
            totalHard = allQuestions.getJSONObject(3).getInt("count");

            totalSolved = actualSubmissions.getJSONObject(0).getInt("count");
            easySolved = actualSubmissions.getJSONObject(1).getInt("count");
            mediumSolved = actualSubmissions.getJSONObject(2).getInt("count");
            hardSolved = actualSubmissions.getJSONObject(3).getInt("count");

            float totalAcceptCount = actualSubmissions.getJSONObject(0).getInt("submissions");
            float totalSubCount = totalSubmissions.getJSONObject(0).getInt("submissions");
            if (totalSubCount != 0) {
                acceptanceRate = round((totalAcceptCount / totalSubCount) * 100);
            }

            contributionPoints = matchedUser.getJSONObject("contributions").getInt("points");
            reputation = matchedUser.getJSONObject("profile").getInt("reputation");
            ranking = matchedUser.getJSONObject("profile").getInt("ranking");

            final JSONObject submissionCalendarJson = new JSONObject(matchedUser.getString("submissionCalendar"));

            for (String timeKey : submissionCalendarJson.keySet()) {
                submissionCalendar.put(timeKey, submissionCalendarJson.getInt(timeKey));
            }

        } catch (JSONException ex) {
            return LeetCodeStatistic.error("error", ex.getMessage());
        }

        return new LeetCodeStatistic("success", "retrieved", totalSolved, totalQuestions, easySolved, totalEasy, mediumSolved, totalMedium, hardSolved, totalHard, acceptanceRate, ranking, contributionPoints, reputation, submissionCalendar);
    }

    private CodeForcesStatistic decodeCodeforces(JSONObject json) {
        int rating = 0;
        int maxRating = 0;
        String rank = "";
        String maxRank = "";
        String organization = "";

        try {
            JSONArray result = json.getJSONArray("result");
            System.out.println(result.toString());
            JSONObject data = result.getJSONObject(0);
            System.out.println(data.toString());

            rating = data.getInt("rating");
            maxRating = data.getInt("maxRating");
            rank = data.getString("rank");
            maxRank = data.getString("maxRank");
            organization = data.getString("organization");
        }catch (JSONException ex) {
            return CodeForcesStatistic.error("error", ex.getMessage());
        }

        return new CodeForcesStatistic("success", "retrieved", rating, rank, organization, maxRating, maxRank);
    }

    private float round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Override
    public CodeForcesStatistic getCodeForcesStatistic(String username) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse("https://codeforces.com/api/user.info")).newBuilder();
        httpBuilder.addQueryParameter("handles", username);
        Request request = new Request.Builder().url(httpBuilder.build()).build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);

            if (response.isSuccessful()) {
                if (jsonObject.has("errors"))
                    return CodeForcesStatistic.error("error", "user does not exist");
                else
                    return decodeCodeforces(jsonObject);
            } else
                return CodeForcesStatistic.error("error", jsonObject.getString("error"));
        } catch (IOException | JSONException e) {
            return CodeForcesStatistic.error("error", e.getMessage());
        }
    }
}
