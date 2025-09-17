package io.github.bigpig.back.util;

import io.github.bigpig.back.dto.AnalyseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalysisParser {

    private final String PART1 = "Overall Assessment: ";
    private final String PART2 = "Attractiveness Rating: ";
    private final String PART3 = "Pros:";
    private  final String PART4 = "Cons:";

    String text = """
            {
              "analysis": "Overall Assessment: IBM offers stable dividends and strong enterprise clients, but growth remains sluggish and valuation is average compared to peers.  \\nAttractiveness Rating: 5/10\\n\\nPros: Consistent dividend payout, entrenched in enterprise IT, sound cash flow.  \\nCons: Low revenue growth, exposure to legacy businesses, moderate P/E ratio limits upside.",
              "symbol": "IBM"
            }
            """;

    public AnalyseDto parseAnalyse(String analyse) {

        analyse = analyse.replace("\\n", "\n");

        String overallAssessment = getOverallAssessment(analyse);
        String rating = getRating(analyse);
        String pros = getPros(analyse);
        String cons = getCons(analyse);

        return new AnalyseDto(overallAssessment, rating,
                List.of(pros.split(", ")), List.of(cons.split(", ")));
    }

    private String getOverallAssessment(String analyse) {
        System.out.println(analyse.substring(find(analyse, PART1) + PART1.length(), find(analyse, PART2)).strip());
        return analyse.substring(find(analyse, PART1) + PART1.length(), find(analyse, PART2)).trim();
    }

    private String getRating(String analyse) {
        System.out.println(analyse.substring(find(analyse, PART2) + PART2.length(), find(analyse, PART3)).strip());
        return analyse.substring(find(analyse, PART2) + PART2.length(), find(analyse, PART3)).trim();
    }

    private String getPros(String analyse) {
        System.out.println(analyse.substring(find(analyse, PART3) + PART3.length(), find(analyse, PART4)).strip());
        return analyse.substring(find(analyse, PART3) + PART3.length(), find(analyse, PART4)).trim();
    }

    private String getCons(String analyse) {
        System.out.println(analyse.substring(find(analyse, PART4) + PART4.length()).strip());
        return analyse.substring(find(analyse, PART4) + PART4.length()).strip();
    }

    private int find (String text, String pattern) {
        int t = 0;
        int last = pattern.length() - 1;

        while (t < text.length() - last) {
            int p = 0;
            while (p <= last && text.charAt(t + p) == pattern.charAt(p)) {
                p++;
            }
            if (p == pattern.length()) {
                return t;
            }
            t++;
        }
        return -1;
    }
}
