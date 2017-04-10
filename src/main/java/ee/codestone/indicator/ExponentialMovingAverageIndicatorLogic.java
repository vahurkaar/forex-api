package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Multiplier: (2 / (Time periods + 1) ) = (2 / (10 + 1) ) = 0.1818 (18.18%)
 * <p>
 * EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 24/09/16
 */
public class ExponentialMovingAverageIndicatorLogic extends SimpleMovingAverageIndicatorLogic {

    private BigDecimal previous;

    public ExponentialMovingAverageIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.EMA;
    }

    @Override
    public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalculate) {
        if (previous == null) {
            Map<String, BigDecimal> result = super.calculate(priceData, precision, recalculate);
            if (result.get("value") != null) {
                previous = result.get("value");
            }

            return result;
        }

        BigDecimal multiplier = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(period).add(BigDecimal.ONE),
                precision, BigDecimal.ROUND_HALF_UP);
        BigDecimal resultValue = priceData.getClose().subtract(previous).multiply(multiplier).add(previous)
                .setScale(precision, BigDecimal.ROUND_HALF_UP);
        previous = resultValue;

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("value", resultValue);
        return result;
    }

}
