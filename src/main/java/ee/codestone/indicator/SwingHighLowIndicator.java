package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static ee.codestone.model.type.IndicatorType.PRICE;

/**
 * Created by vahurkaar on 30.09.16.
 */
public class SwingHighLowIndicator extends IndicatorLogic {
    public SwingHighLowIndicator(Map<String, BigDecimal> params) {
        super(params);
    }

    @Override
    public IndicatorType getType() {
        return PRICE;
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("high", priceData.getClose());
        result.put("low", priceData.getClose());
        return result;
    }
}
