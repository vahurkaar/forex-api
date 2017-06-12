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
public class VolumeChangeIndicatorLogicTest extends IndicatorLogicTest {

	public static final String VOLUME_CHANGE_CSV = "volumeChange.csv";

	@Test
	public void testCalculateSMA() throws Exception {
		IndicatorCalculator calculator = prepareCalculator(new BigDecimal(5));

		Collection<PriceData> testData = prepareInnerTestData(VOLUME_CHANGE_CSV);
		List<BigDecimal> expectedResults = prepareExpectations(VOLUME_CHANGE_CSV);

		List<ChartData> results = executeCalculation(calculator, testData);

		for (int i = 0; i < results.size(); i++) {
			assertEquals(expectedResults.get(i), findIndicatorValues(results.get(i), IndicatorType.VOLUME_CHANGE).get("value"));
		}
	}

	private Collection<PriceData> prepareInnerTestData(String fileLocation) throws IOException {
		Collection<PriceData> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream(TEST_RESOURCES + fileLocation)));
		reader.lines().forEach(line -> {
			String[] values = line.split(",");

			PriceData data = new PriceData();
			data.setVolume(new BigDecimal(values[0].trim()));
			result.add(data);
		});

		return result;
	}

	private IndicatorCalculator prepareCalculator(BigDecimal period) {
		List<IndicatorDefinition> indicators = new ArrayList<>();
		Map<String, BigDecimal> params = new HashMap<>();
		params.put("period", period);
		indicators.add(new IndicatorDefinition(IndicatorType.VOLUME_CHANGE, params, null));
		return new IndicatorCalculator(indicators);
	}

}