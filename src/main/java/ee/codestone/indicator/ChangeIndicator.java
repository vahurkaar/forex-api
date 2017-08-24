package ee.codestone.indicator;

import ee.codestone.model.PriceData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 14/05/2017
 */
public abstract class ChangeIndicator extends IndicatorLogic {

    private Integer period;

    protected LinkedList<BigDecimal> valueHistory = new LinkedList<>();
    protected LinkedList<PriceData> priceHistory = new LinkedList<>();

    public ChangeIndicator(Map<String, BigDecimal> params) {
        super(params);
        period = params.get("period").intValue();
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();

        if (recalculate) priceHistory.pollLast();
        priceHistory.addLast(priceData);
        if (priceHistory.size() > period) {
            priceHistory.pollFirst();
        }

        if (priceHistory.size() == period) {
            BigDecimal periodValue = calculateValue(priceData, precision);

            if (recalculate) valueHistory.pollLast();
            valueHistory.addLast(periodValue);
            if (valueHistory.size() > period) {
                valueHistory.pollFirst();
            }
        }

        result.put("value", calculateChangeValue(precision));
        return result;
    }

    protected BigDecimal calculateChangeValue(Integer precision) {
        BigDecimal value = null;

        if (valueHistory.size() == period) {
            BigDecimal first = valueHistory.peekFirst();
            BigDecimal last = valueHistory.peekLast();
            value = last.subtract(first).divide(first, precision + 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(precision, RoundingMode.HALF_UP);
        }

        return value;
    }

    protected abstract BigDecimal calculateValue(PriceData priceData, Integer precision);

    protected abstract BigDecimal getValue(PriceData priceData);
}
