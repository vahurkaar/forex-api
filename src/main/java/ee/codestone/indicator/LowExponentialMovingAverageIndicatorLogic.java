package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 08/04/2017
 */
public class LowExponentialMovingAverageIndicatorLogic extends ExponentialMovingAverageIndicatorLogic {

    public LowExponentialMovingAverageIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.EMA_LOW;
    }

    @Override
    protected BigDecimal getValue(PriceData priceData) {
        return priceData.getLow();
    }
}
