package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (<a href="mailto:vahur.kaar@tieto.com">vahur.kaar@tieto.com</a>)
 * @since 4.10.2016.
 */
public class GrimesEfficiencyRatioIndicatorLogic extends IndicatorLogic {

    private Integer period;

    private LinkedList<PriceData> history = new LinkedList<>();
    private LinkedList<BigDecimal> valuesHistory = new LinkedList<>();

    public GrimesEfficiencyRatioIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.GER;
    }

    @Override
    public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalculate) {
        addDataToHistory(priceData, recalculate);

        Map<String, BigDecimal> result = new HashMap<>();
        if (history.size() < period) {
            result.put("value", null);
            return result;
        }

        BigDecimal highest = null;
        BigDecimal lowest = null;

        // range = highest(h, lookback) - lowest(l, lookback);
        for (PriceData data : history) {
            if (highest == null || data.getHigh().compareTo(highest) > 0) {
                highest = data.getHigh();
            }

            if (lowest == null || data.getLow().compareTo(lowest) < 0) {
                lowest =  data.getLow();
            }
        }

        BigDecimal range = highest.subtract(lowest);

        // iff(rng > 0,((c - lowest(l, lookback)) / rng), 0); //calculate close as % of range, catch divide by zero error
        BigDecimal change = BigDecimal.ZERO;
        if (range.compareTo(BigDecimal.ZERO) > 0) {
            change = priceData.getClose().subtract(lowest).divide(range, precision, RoundingMode.HALF_UP);
        }

        addValueToHistory(change, recalculate);
        result.put("value", calculateAverageFromHistory(precision));
        return result;
    }

    private BigDecimal calculateAverageFromHistory(Integer precision) {
        if (valuesHistory.size() < period) {
            return null;
        }

        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal value : valuesHistory) {
            result = result.add(value);
        }

        return result.divide(new BigDecimal(period), precision, RoundingMode.HALF_UP);
    }

    private void addValueToHistory(BigDecimal value, boolean recalculate) {
        if (recalculate) valuesHistory.pollLast();
        valuesHistory.addLast(value);
        if (valuesHistory.size() > period) {
            valuesHistory.pollFirst();
        }
    }

    private void addDataToHistory(PriceData forexData, boolean recalculate) {
        if (recalculate) history.pollLast();
        history.addLast(forexData);
        if (history.size() > period) {
            history.pollFirst();
        }
    }
}
