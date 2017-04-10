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
 * Created by vahurkaar on 29.09.16.
 */
public class StandardDeviationIndicatorLogicTest extends IndicatorLogicTest {

	public static final String SD_TEST_CSV = "sd.csv";

	@Test
	public void testCalculateSMA() throws Exception {
		IndicatorCalculator calculator = prepareCalculator(new BigDecimal(20));

		Collection<PriceData> testData = prepareTestData(SD_TEST_CSV);
		List<BigDecimal> expectedResults = prepareExpectations(SD_TEST_CSV);

		List<ChartData> results = executeCalculation(calculator, testData);

		for (int i = 0; i < results.size(); i++) {
			assertEquals(expectedResults.get(i), findIndicatorValues(results.get(i), IndicatorType.SD).get("value"));
		}
	}

	private IndicatorCalculator prepareCalculator(BigDecimal period) {
		List<IndicatorDefinition> indicators = new ArrayList<>();
		Map<String, BigDecimal> params = new HashMap<>();
		params.put("period", period);
		indicators.add(new IndicatorDefinition(IndicatorType.SD, params, null));
		return new IndicatorCalculator(indicators);
	}

}