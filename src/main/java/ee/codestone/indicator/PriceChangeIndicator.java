package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 14/05/2017
 */
public class PriceChangeIndicator extends ChangeIndicator {

    public PriceChangeIndicator(Map<String, BigDecimal> params) {
        super(params);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.PRICE_CHANGE;
    }

    @Override
    protected BigDecimal getValue(PriceData priceData) {
        return priceData.getClose();
    }

    @Override
    protected BigDecimal calculateValue(PriceData priceData, Integer precision) {
        return priceData.getClose();
    }
}
