package ee.codestone.indicator;

import ee.codestone.model.ChartData;
import ee.codestone.model.IndicatorDefinition;
import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 25/09/16
 */
public class RelativeStrengthIndexIndicatorLogicTest extends IndicatorLogicTest {

    public static final String RSI_TEST_CSV = "rsiTest.csv";

    @Test
    public void testCalculateRSI() throws Exception {
        IndicatorCalculator calculator = prepareCalculator(new BigDecimal(14));

        Collection<PriceData> testData = prepareTestData(RSI_TEST_CSV);
        List<BigDecimal> expectedResults = prepareExpectations(RSI_TEST_CSV);

        List<ChartData> results = executeCalculation(calculator, testData);

        for (int i = 0; i < results.size(); i++) {
            assertEquals(expectedResults.get(i), findIndicatorValues(results.get(i), IndicatorType.RSI).get("value"));
        }
    }

    private IndicatorCalculator prepareCalculator(BigDecimal period) {
        List<IndicatorDefinition> indicators = new ArrayList<>();
        Map<String, BigDecimal> params = new HashMap<>();
        params.put("period", period);
        indicators.add(new IndicatorDefinition(IndicatorType.RSI, params, null));
        return new IndicatorCalculator(indicators);
    }
}