package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * https://www.tradingview.com/stock-charts-support/index.php/Directional_Movement_(DMI)
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_directional_index_adx
 *
 * Created by vahurkaar on 5.10.16.
 */
public class AverageDirectionalIndexIndicatorLogic extends IndicatorLogic {

	private Integer period;
	private AverageTrueRangeIndicatorLogic averageTrueRangeIndicator;
	private LinkedList<PriceData> forexDataHistory = new LinkedList<>();

    private BigDecimal lastAverageDirectionalIndex;
    private LinkedList<BigDecimal> dxHistory = new LinkedList<>();

    private BigDecimal lastAverageDmPlus;
    private BigDecimal lastAverageDmMinus;
    private LinkedList<BigDecimal> dmPlusHistory = new LinkedList<>();
    private LinkedList<BigDecimal> dmMinusHistory = new LinkedList<>();

	public AverageDirectionalIndexIndicatorLogic(Map<String, BigDecimal> params) {
		super(params);
		this.period = params.get("period").intValue();
		averageTrueRangeIndicator = new AverageTrueRangeIndicatorLogic(params);
	}

	@Override
	public IndicatorType getType() {
		return IndicatorType.ADX;
	}

	@Override
	public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        if (forexDataHistory.isEmpty()) {
            addForexDataToHistory(priceData, recalculate);
            return prepareResult(null, null, null, precision);
        }

        PriceData previous = forexDataHistory.peekLast();
		BigDecimal upMove = priceData.getHigh().subtract(previous.getHigh());
		BigDecimal downMove = previous.getLow().subtract(priceData.getLow());

		BigDecimal dmPlus = getDirectionalMove(upMove, downMove);
		BigDecimal dmMinus = getDirectionalMove(downMove, upMove);

		BigDecimal averageDmPlus = calculateAverageDm(dmPlus, precision, 1, recalculate);
		BigDecimal averageDmMinus = calculateAverageDm(dmMinus, precision, -1, recalculate);

        BigDecimal averageTrueRange = averageTrueRangeIndicator.calculateValues(priceData, precision * 2, recalculate).get("value");
        if (averageTrueRange == null) {
            addForexDataToHistory(priceData, recalculate);
            return prepareResult(null, null, null, precision);
        }

		BigDecimal averageDiPlus = averageDmPlus.divide(averageTrueRange, precision + 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
		BigDecimal averageDiMinus = averageDmMinus.divide(averageTrueRange, precision + 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

		BigDecimal directionalIndex = averageDiPlus.subtract(averageDiMinus).divide(averageDiPlus.add(averageDiMinus),
				precision + 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).abs();

        addDataToHistory(directionalIndex, dxHistory, recalculate);
        addForexDataToHistory(priceData, recalculate);
        return prepareResult(averageDiPlus, averageDiMinus,
                calculateAverageDirectionalIndex(directionalIndex, precision), precision);
	}

    private Map<String, BigDecimal> prepareResult(BigDecimal averageDiPlus, BigDecimal averageDiMinus,
                                                  BigDecimal averageDirectionalIndex, Integer precision) {
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("value", roundValue(averageDirectionalIndex, precision));
        result.put("diPlus", roundValue(averageDiPlus, precision));
        result.put("diMinus", roundValue(averageDiMinus, precision));
        return result;
    }

    private BigDecimal roundValue(BigDecimal value, Integer precision) {
        if (value != null) {
            return value.setScale(precision, RoundingMode.HALF_UP);
        }

        return null;
    }

    private BigDecimal calculateAverageDm(BigDecimal dm, Integer precision, int mode, boolean recalcuate) {
        if (mode == 1) addDataToHistory(dm, dmPlusHistory, recalcuate);
        else addDataToHistory(dm, dmMinusHistory, recalcuate);

        BigDecimal lastAverageDm;
        if (mode == 1) lastAverageDm = lastAverageDmPlus;
        else lastAverageDm = lastAverageDmMinus;

        if (lastAverageDm != null) {
            BigDecimal averageDm = lastAverageDm.multiply(new BigDecimal(period - 1)).add(dm).divide(new BigDecimal(period), precision, RoundingMode.HALF_UP);
            if (mode == 1) lastAverageDmPlus = averageDm;
            else lastAverageDmMinus = averageDm;
            return averageDm;
        } else {
            LinkedList<BigDecimal> dmHistory;
            if (mode == 1) dmHistory = dmPlusHistory;
            else dmHistory = dmMinusHistory;

            if (dmHistory.size() < period) {
                return null;
            }

            BigDecimal sum = BigDecimal.ZERO;
            for (BigDecimal value : dmHistory) {
                sum = sum.add(value);
            }

            BigDecimal averageDm = sum.divide(new BigDecimal(period), precision * 2, RoundingMode.HALF_UP);
            if (mode == 1) lastAverageDmPlus = averageDm;
            else lastAverageDmMinus = averageDm;
            return averageDm;
        }
    }

    private BigDecimal calculateAverageDirectionalIndex(BigDecimal directionalIndex, Integer precision) {
        BigDecimal averageDirectionalIndex;
        if (lastAverageDirectionalIndex != null) {
            averageDirectionalIndex = lastAverageDirectionalIndex.multiply(new BigDecimal(period - 1)).add(directionalIndex)
                    .divide(new BigDecimal(period), precision, RoundingMode.HALF_UP);
        } else {
            if (dxHistory.size() < period) {
                return null;
            }

            averageDirectionalIndex = BigDecimal.ZERO;
            for (BigDecimal value : dxHistory) {
                averageDirectionalIndex = averageDirectionalIndex.add(value);
            }

            averageDirectionalIndex = averageDirectionalIndex
                    .divide(new BigDecimal(period), precision + 2, RoundingMode.HALF_UP);
        }

        lastAverageDirectionalIndex = averageDirectionalIndex;
        return averageDirectionalIndex;
    }

    private BigDecimal getDirectionalMove(BigDecimal first, BigDecimal last) {
		if (first.compareTo(last) > 0 && first.compareTo(BigDecimal.ZERO) > 0) {
			return first;
		}
		return BigDecimal.ZERO;
	}

    private void addDataToHistory(BigDecimal value, LinkedList<BigDecimal> history, boolean recalculate) {
        if (recalculate) history.pollLast();
        history.addLast(value);
        if (history.size() > period) {
            history.pollFirst();
        }
    }

    private void addForexDataToHistory(PriceData value, boolean recalculate) {
        if (recalculate) forexDataHistory.pollLast();
        forexDataHistory.addLast(value);
        if (forexDataHistory.size() > period) {
            forexDataHistory.pollFirst();
        }
    }
}
