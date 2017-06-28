package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 25/09/16
 */
public class StandardDeviationIndicatorLogic extends IndicatorLogic {

    private Integer period;

    private LinkedList<PriceData> history = new LinkedList<>();

    public StandardDeviationIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.SD;
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        if (recalculate) history.pollLast();
        history.addLast(priceData);
        if (history.size() > period) {
            history.pollFirst();
        }

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("value", calculateValue(precision));
        return result;
    }

    private BigDecimal calculateValue(Integer precision) {
        if (history.size() < period) {
            return null;
        }

        BigDecimal averageValue = calculateAverageValue(precision);
        BigDecimal sumOfSquares = BigDecimal.ZERO;
        for (PriceData data : history) {
            sumOfSquares = sumOfSquares.add(BigDecimal.valueOf(
                    Math.pow(data.getClose().subtract(averageValue).doubleValue(), 2.0)
            ));
        }

        return new BigDecimal(Math.sqrt(sumOfSquares.divide(BigDecimal.valueOf(history.size()), precision * 2, BigDecimal.ROUND_HALF_UP).doubleValue()))
                .setScale(precision, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateAverageValue(Integer precision) {
        BigDecimal sum = BigDecimal.ZERO;
        for (PriceData data : history) {
            sum = sum.add(data.getClose());
        }

        return sum.divide(BigDecimal.valueOf(history.size()), precision, BigDecimal.ROUND_HALF_UP);
    }
}
