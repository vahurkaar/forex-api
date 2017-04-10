package ee.codestone.indicator;

import ee.codestone.model.ChartData;
import ee.codestone.model.IndicatorValue;
import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by vahurkaar on 29.09.16.
 */
public class IndicatorLogicTest {

	protected static final String TEST_RESOURCES = "ee/codestone/indicator/";

	protected List<BigDecimal> prepareExpectations(String fileLocation) throws IOException {
		List<BigDecimal> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(TEST_RESOURCES + fileLocation)));
		reader.lines().forEach(line -> {
			String[] values = line.split(",");
			result.add(values.length > 1 && isNotBlank(values[1]) ? new BigDecimal(values[1].trim()) : null);
		});

		return result;
	}

	protected boolean isNotBlank(String value) {
		return value != null && value.trim().length() > 0;
	}

	protected Collection<PriceData> prepareTestData(String fileLocation) throws IOException {
		Collection<PriceData> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(TEST_RESOURCES + fileLocation)));
		reader.lines().forEach(line -> {
			String[] values = line.split(",");

			PriceData data = new PriceData();
			data.setClose(new BigDecimal(values[0].trim()));
			result.add(data);
		});

		return result;
	}

	protected List<ChartData> executeCalculation(IndicatorCalculator calculator, Collection<PriceData> testData) {
		List<ChartData> result = new ArrayList<>();
		for (PriceData priceData : testData) {
			result.add(calculator.calculate(priceData));
		}

		return result;
	}

	protected Map<String, BigDecimal> findIndicatorValues(ChartData chartData, IndicatorType indicatorType) {
		for (IndicatorValue priceDataIndicatorDto : chartData.getIndicators()) {
			if (priceDataIndicatorDto.getType().equals(indicatorType)) {
				return priceDataIndicatorDto.getValues();
			}
		}

		return null;
	}

}
