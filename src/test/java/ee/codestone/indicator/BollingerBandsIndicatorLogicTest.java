package ee.codestone.indicator;

import ee.codestone.model.ChartData;
import ee.codestone.model.IndicatorDefinition;
import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by vahurkaar on 29.09.16.
 */
public class BollingerBandsIndicatorLogicTest extends IndicatorLogicTest {

	public static final String SD_TEST_CSV = "bb.csv";

	@Test
	public void testCalculateSMA() throws Exception {
		IndicatorCalculator calculator = prepareCalculator(new BigDecimal(20), new BigDecimal(2));

		Collection<PriceData> testData = prepareTestData(SD_TEST_CSV);
		List<Map<String, BigDecimal>> expectedResults = prepareExpectationsArray(SD_TEST_CSV);

		List<ChartData> results = executeCalculation(calculator, testData);

		for (int i = 0; i < results.size(); i++) {
			Map<String, BigDecimal> expectedResult = expectedResults.get(i);
			Map<String, BigDecimal> actualValues = findIndicatorValues(results.get(i), IndicatorType.BB);

			assertEquals(expectedResult, actualValues);
		}
	}

	private List<Map<String, BigDecimal>> prepareExpectationsArray(String sdTestCsv) throws IOException {
		List<Map<String, BigDecimal>> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(TEST_RESOURCES + sdTestCsv)));
		reader.lines().forEach(line -> {
			String[] values = line.split(",");
			BigDecimal middleBand = values.length > 1 && isNotBlank(values[1]) ? new BigDecimal(values[1].trim()) : null;
			BigDecimal upperBand = values.length > 2 && isNotBlank(values[2]) ? new BigDecimal(values[2].trim()) : null;
			BigDecimal lowerBand = values.length > 3 && isNotBlank(values[3]) ? new BigDecimal(values[3].trim()) : null;

			Map<String, BigDecimal> lineValues = new HashMap<>();
			lineValues.put("middle", middleBand);
			lineValues.put("high", upperBand);
			lineValues.put("low", lowerBand);
			result.add(lineValues);
		});

		return result;
	}

	private IndicatorCalculator prepareCalculator(BigDecimal period, BigDecimal deviation) {
		List<IndicatorDefinition> indicators = new ArrayList<>();
		Map<String, BigDecimal> params = new HashMap<>();
		params.put("period", period);
		params.put("deviation", deviation);
		indicators.add(new IndicatorDefinition(IndicatorType.BB, params, null));
		return new IndicatorCalculator(indicators);
	}

}