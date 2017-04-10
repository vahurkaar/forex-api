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
public class AverageTrueRangeIndicatorLogicTest extends IndicatorLogicTest {

	public static final String ATR_TEST_CSV = "atr.csv";

	@Test
	public void testCalculateATR() throws Exception {
		IndicatorCalculator calculator = prepareCalculator(new BigDecimal(14));

		Collection<PriceData> testData = prepareTestData(ATR_TEST_CSV);
		List<BigDecimal> expectedResults = prepareExpectations(ATR_TEST_CSV);

		List<ChartData> results = executeCalculation(calculator, testData);

		for (int i = 0; i < results.size(); i++) {
			assertEquals(expectedResults.get(i), findIndicatorValues(results.get(i), IndicatorType.ATR).get("value"));
		}
	}

	@Override
	protected Collection<PriceData> prepareTestData(String fileLocation) throws IOException {
		Collection<PriceData> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(TEST_RESOURCES + fileLocation)));
		reader.lines().forEach(line -> {
			String[] values = line.split(",");

            PriceData data = new PriceData();
			data.setHigh(new BigDecimal(values[0].trim()));
			data.setLow(new BigDecimal(values[1].trim()));
			data.setClose(new BigDecimal(values[2].trim()));
			result.add(data);
		});

		return result;
	}

	@Override
	protected List<BigDecimal> prepareExpectations(String fileLocation) throws IOException {
		List<BigDecimal> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(TEST_RESOURCES + fileLocation)));
		reader.lines().forEach(line -> {
			String[] values = line.split(",");
			result.add(values.length > 3 && isNotBlank(values[3]) ? new BigDecimal(values[3].trim()) : null);
		});

		return result;
	}

	private IndicatorCalculator prepareCalculator(BigDecimal period) {
		List<IndicatorDefinition> indicators = new ArrayList<>();
		Map<String, BigDecimal> params = new HashMap<>();
		params.put("period", period);
		indicators.add(new IndicatorDefinition(IndicatorType.ATR, params, null));
		return new IndicatorCalculator(indicators);
	}

}