package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static ee.codestone.model.type.IndicatorType.PRICE;
import static ee.codestone.model.type.IndicatorType.VOLUME;

/**
 * Created by vahurkaar on 30.09.16.
 */
public class VolumeIndicatorLogic extends IndicatorLogic {
    public VolumeIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
    }

    @Override
    public IndicatorType getType() {
        return VOLUME;
    }

    @Override
    public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("value", priceData.getVolume());
        return result;
    }
}
