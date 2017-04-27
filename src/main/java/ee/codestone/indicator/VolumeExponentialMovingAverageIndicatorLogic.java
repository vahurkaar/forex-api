package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 23/04/2017
 */
public class VolumeExponentialMovingAverageIndicatorLogic extends ExponentialMovingAverageIndicatorLogic {

    public VolumeExponentialMovingAverageIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.EMA_VOLUME;
    }

    @Override
    protected BigDecimal getValue(PriceData priceData, Integer precision, boolean recalculate) {
        return priceData.getVolume();
    }
}
