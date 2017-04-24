package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 24/09/16
 */
public class SimpleMovingAverageIndicatorLogic extends IndicatorLogic {

    protected final Integer period;

    private LinkedList<PriceData> history = new LinkedList<>();

    public SimpleMovingAverageIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.SMA;
    }

    @Override
    public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();

        if (recalculate) history.pollLast();
        history.addLast(priceData);
        if (history.size() > period) {
            history.pollFirst();
        }

        result.put("value", calculateValue(precision));
        return result;
    }

    private BigDecimal calculateValue(Integer precision) {
        BigDecimal value = BigDecimal.ZERO;

        if (history.size() < period) {
            return null;
        }

        for (PriceData priceHistory : history) {
            BigDecimal calculateValue = getValue(priceHistory, precision);
            if (calculateValue == null) return null;
            value = value.add(calculateValue);
        }

        return value.divide(BigDecimal.valueOf(period), precision, BigDecimal.ROUND_HALF_UP);
    }

    protected BigDecimal getValue(PriceData priceData, Integer precision) {
        return getValue(priceData, precision, false);
    }

    protected BigDecimal getValue(PriceData priceData, Integer precision, boolean recalculate) {
        return priceData.getClose();
    }

}
