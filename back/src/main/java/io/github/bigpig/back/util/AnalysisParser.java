package io.github.bigpig.back.util;

import io.github.bigpig.back.dto.AnalyseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnalysisParser {

    Pattern OVERALL_PATTERN = Pattern.compile(
            "Overall Assessment:\\s*(.*?)\\s*Attractiveness Rating:",
            Pattern.DOTALL
    );


    Pattern RATING_PATTERN = Pattern.compile(
            "Attractiveness Rating:\\s*(.*?)\\s*Pros: ",
            Pattern.DOTALL
    );

    Pattern PROS_PATTERN = Pattern.compile(
            "Pros:\\s*(.*?)\\s*Cons: ",
            Pattern.DOTALL
    );

    Pattern CONS_PATTERN = Pattern.compile(
            "Cons:\\s*(.*?)(?=\\n\\n|\\z)",
            Pattern.DOTALL
    );

    public AnalyseDto parseAnalyse(String analyse) {
        if (analyse == null || analyse.trim().isEmpty()) {
            throw new IllegalArgumentException("Analysis text cannot be null or empty");
        }

        analyse = analyse.replace("\\n", "\n").trim();

        String overallAssessment = extractOverallAssessment(analyse);
        String rating = extractRating(analyse);
        List<String> pros = getItems(analyse, PROS_PATTERN);
        List<String> cons = getItems(analyse, CONS_PATTERN);

        return new AnalyseDto(
                overallAssessment,
                rating,
                pros,
                cons
        );
    }

    public String extractOverallAssessment(String analyse) {
        Matcher matcher = OVERALL_PATTERN.matcher(analyse);
        if (matcher.find()) {
            return matcher.group(1).replace("\\n", " ").replaceAll("\\s+", " ").trim();
        }
        return "Overall assessment not found";
    }

    public String extractRating(String analyse) {
        Matcher matcher = RATING_PATTERN.matcher(analyse);
        if (matcher.find()) {
            return matcher.group(1).replace("\\n", " ").replaceAll("\\s+", " ").trim();
        }
        return "Rating not found";
    }

    List<String> getItems(String content, Pattern pattern) {
        List<String> items = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String[] pros = matcher.group(1).replace("\\n", " ").replaceAll("\\s+", " ").trim().split(",");
            for (String item : pros) {
                String capitalized = Character.toUpperCase(item.trim().charAt(0)) +
                        (item.trim().length() > 1 ? item.trim().substring(1) : "");
                items.add(capitalized);
            }
        } else {
            items.add("Not found");
        }

        return items;
    }
}