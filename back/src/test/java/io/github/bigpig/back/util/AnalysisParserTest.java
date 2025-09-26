package io.github.bigpig.back.util;

import io.github.bigpig.back.dto.AnalyseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnalysisParserTest {

    private AnalysisParser analysisParser;

    @BeforeEach
    void setUp() {
        analysisParser = new AnalysisParser();
    }

    @Test
    @DisplayName("parseAnalyse should throw IllegalArgumentException for null input")
    void parseAnalyse_NullInput_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analysisParser.parseAnalyse(null));

        assertEquals("Analysis text cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("parseAnalyse should throw IllegalArgumentException for empty input")
    void parseAnalyse_EmptyInput_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analysisParser.parseAnalyse("   "));

        assertEquals("Analysis text cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("parseAnalyse should parse complete analysis correctly")
    void parseAnalyse_CompleteAnalysis_Success() {
        // Arrange
        String analysisText = """
            Overall Assessment: This company shows strong growth potential with solid fundamentals.
            It has a competitive advantage in its market segment.
            Attractiveness Rating: 8.5/10 - Highly Attractive
            Pros: Strong market position, Innovative products, Experienced management
            Cons: High competition, Regulatory risks, Dependence on key suppliers
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("This company shows strong growth potential with solid fundamentals. It has a competitive advantage in its market segment.",
                result.overallAssessment());
        assertEquals("8.5/10 - Highly Attractive", result.rating());

        assertEquals(3, result.pros().size());
        assertEquals("Strong market position", result.pros().get(0));
        assertEquals("Innovative products", result.pros().get(1));
        assertEquals("Experienced management", result.pros().get(2));

        assertEquals(3, result.cons().size());
        assertEquals("High competition", result.cons().get(0));
        assertEquals("Regulatory risks", result.cons().get(1));
        assertEquals("Dependence on key suppliers", result.cons().get(2));
    }

    @Test
    @DisplayName("parseAnalyse should handle newline characters")
    void parseAnalyse_WithNewlineCharacters_Success() {
        // Arrange
        String analysisText = """
            Overall Assessment: Line 1.\\nLine 2.
            Attractiveness Rating: 8/10\\n- Good
            Pros: item1\\n,item2, item3
            Cons: item4, item5\\n, item6
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Line 1. Line 2.", result.overallAssessment());
        assertEquals("8/10 - Good", result.rating());
        assertEquals(3, result.pros().size());
        assertEquals(3, result.cons().size());
    }

    @Test
    @DisplayName("parseAnalyse should capitalize first letter of list items")
    void parseAnalyse_CapitalizesListItems() {
        // Arrange
        String analysisText = """
            Overall Assessment: Test
            Attractiveness Rating: 7/10
            Pros: strong position, innovative products
            Cons: high risk, low margin
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Strong position", result.pros().get(0));
        assertEquals("Innovative products", result.pros().get(1));
        assertEquals("High risk", result.cons().get(0));
        assertEquals("Low margin", result.cons().get(1));
    }

    @Test
    @DisplayName("parseAnalyse should handle missing sections")
    void parseAnalyse_MissingSections_ReturnsDefaults() {
        // Arrange
        String analysisText = "This is just some random text without proper sections.";

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Overall assessment not found", result.overallAssessment());
        assertEquals("Rating not found", result.rating());
        assertEquals(List.of("Not found"), result.pros());
        assertEquals(List.of("Not found"), result.cons());
    }

    @Test
    @DisplayName("parseAnalyse should handle missing pros section")
    void parseAnalyse_MissingProsSection_Success() {
        // Arrange
        String analysisText = """
            Overall Assessment: Good company.
            Attractiveness Rating: 7/10
            Cons: disadvantage1, disadvantage2
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Good company.", result.overallAssessment());
        assertEquals("Rating not found", result.rating());
        assertEquals(List.of("Not found"), result.pros());
        assertEquals(2, result.cons().size());
    }

    @Test
    @DisplayName("parseAnalyse should handle missing cons section")
    void parseAnalyse_MissingConsSection_Success() {
        // Arrange
        String analysisText = """
            Overall Assessment: Good company.
            Attractiveness Rating: 7/10
            Pros: advantage1, advantage2
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Good company.", result.overallAssessment());
        assertEquals("7/10", result.rating());
        assertEquals(1, result.pros().size());
        assertEquals(List.of("Not found"), result.cons());
    }

    @Test
    @DisplayName("parseAnalyse should normalize whitespace")
    void parseAnalyse_NormalizesWhitespace() {
        // Arrange
        String analysisText = """
            Overall Assessment:   Multiple   spaces   and   tabs
            Attractiveness Rating:   8/10   -   Good
            Pros:   item1   ,   item2   ,   item3
            Cons:   item4,item5,   item6
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Multiple spaces and tabs", result.overallAssessment());
        assertEquals("8/10 - Good", result.rating());
        assertEquals(3, result.pros().size());
        assertEquals("Item1", result.pros().get(0));
        assertEquals("Item2", result.pros().get(1));
        assertEquals("Item3", result.pros().get(2));
        assertEquals(3, result.cons().size());
    }

    @Test
    @DisplayName("parseAnalyse should handle single item lists")
    void parseAnalyse_SingleItemLists_Success() {
        // Arrange
        String analysisText = """
            Overall Assessment: Single items
            Attractiveness Rating: 5/10
            Pros: only one advantage
            Cons: only one disadvantage
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Single items", result.overallAssessment());
        assertEquals("5/10", result.rating());
        assertEquals(List.of("Only one advantage"), result.pros());
        assertEquals(List.of("Only one disadvantage"), result.cons());
    }

    @Test
    @DisplayName("parseAnalyse should handle different rating formats")
    void parseAnalyse_DifferentRatingFormats_Success() {
        // Arrange
        String analysisText = """
            Overall Assessment: Test
            Attractiveness Rating: 9.5/10 - Excellent
            Pros: test
            Cons: test
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("9.5/10 - Excellent", result.rating());
    }

    @Test
    @DisplayName("parseAnalyse should handle text at the end of analysis")
    void parseAnalyse_TextAfterCons_Success() {
        // Arrange
        String analysisText = """
            Overall Assessment: Test company
            Attractiveness Rating: 7/10
            Pros: advantage1, advantage2
            Cons: disadvantage1, disadvantage2
            
            Additional comments: This is additional text that should be ignored.
            """;

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("Test company", result.overallAssessment());
        assertEquals("7/10", result.rating());
        assertEquals(2, result.pros().size());
        assertEquals(2, result.cons().size());
        // Должен остановиться на Cons и проигнорировать дополнительный текст
    }

    @Test
    @DisplayName("parseAnalyse should handle edge case with minimal content")
    void parseAnalyse_MinimalContent_Success() {
        // Arrange
        String analysisText = "Overall Assessment: A Attractiveness Rating: 1 Pros: a Cons: b";

        // Act
        AnalyseDto result = analysisParser.parseAnalyse(analysisText);

        // Assert
        assertNotNull(result);
        assertEquals("A", result.overallAssessment());
        assertEquals("1", result.rating());
        assertEquals(List.of("A"), result.pros());
        assertEquals(List.of("B"), result.cons());
    }

    @Test
    @DisplayName("extractOverallAssessment should extract multi-line content")
    void extractOverallAssessment_MultiLineContent() {
        // Arrange
        String analysisText = """
            Overall Assessment: This is line one.
            This is line two.
            And this is line three.
            Attractiveness Rating: 8/10
            """;

        // Act
        String result = analysisParser.extractOverallAssessment(analysisText);

        // Assert
        assertEquals("This is line one. This is line two. And this is line three.", result);
    }
}