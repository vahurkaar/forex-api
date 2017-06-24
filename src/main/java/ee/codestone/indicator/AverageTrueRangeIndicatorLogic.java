package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by vahurkaar on 5.10.16.
 */
public class AverageTrueRangeIndicatorLogic extends IndicatorLogic {

	private Integer period;

	private LinkedList<BigDecimal> trueRangeHistory = new LinkedList<>();
	private LinkedList<PriceData> history = new LinkedList<>();
	private BigDecimal previousPreviousAverageTrueRange = null;
	private BigDecimal previousAverageTrueRange = null;

	public AverageTrueRangeIndicatorLogic(Map<String, BigDecimal> params) {
		super(params);
		this.period = params.get("period").intValue();
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.ATR;
	}

	@Override
	public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalculate) {
		Map<String, BigDecimal> result = new HashMap<>();

		BigDecimal trueRange = calculateTrueRange(priceData);
		addTrueRangeToHistory(trueRange, recalculate);
		addForexDataToHistory(priceData, recalculate);
		if (recalculate) {
			previousAverageTrueRange = previousPreviousAverageTrueRange;
		}

		if (history.size() < period) {
			result.put("value", null);
			return result;
		}

		BigDecimal averageTrueRange;
		if (previousAverageTrueRange == null) {
			averageTrueRange = calculateAverageValue(precision);
		} else {
			averageTrueRange = previousAverageTrueRange.multiply(new BigDecimal(period - 1)).add(trueRange)
					.divide(new BigDecimal(period), precision, RoundingMode.HALF_UP);
		}

		previousPreviousAverageTrueRange = previousAverageTrueRange;
		previousAverageTrueRange = averageTrueRange;
		result.put("value", averageTrueRange);
		return result;
	}

	private void addTrueRangeToHistory(BigDecimal trueRange, boolean recalculate) {
        if (recalculate) trueRangeHistory.pollLast();
		trueRangeHistory.addLast(trueRange);
		if (trueRangeHistory.size() > period) {
			trueRangeHistory.pollFirst();
		}
	}

	private void addForexDataToHistory(PriceData forexData, boolean recalculate) {
        if (recalculate) history.pollLast();
		history.addLast(forexData);
		if (history.size() > period) {
			history.pollFirst();
		}
	}

	/**
	 * Method 1: Current High less the current Low
	 * Method 2: Current High less the previous Close (absolute value)
	 * Method 3: Current Low less the previous Close (absolute value)
	 *
	 * @param forexData
	 * @return
	 */
	private BigDecimal calculateTrueRange(PriceData forexData) {
		BigDecimal method1 = forexData.getHigh().subtract(forexData.getLow());

		if (history.size() > 0) {
			BigDecimal method2 = forexData.getHigh().subtract(history.peekLast().getClose()).abs();
			BigDecimal method3 = forexData.getLow().subtract(history.peekLast().getClose()).abs();

			return method1.max(method2).max(method3);
		} else {
			return method1;
		}
	}

	private BigDecimal calculateAverageValue(Integer precision) {
		if (trueRangeHistory.size() < period) {
			return null;
		}

		BigDecimal sum = BigDecimal.ZERO;
		for (BigDecimal value : trueRangeHistory) {
			sum = sum.add(value);
		}

		return sum.divide(BigDecimal.valueOf(period), precision, BigDecimal.ROUND_HALF_UP);
	}
}
