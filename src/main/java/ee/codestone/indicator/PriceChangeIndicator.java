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
        return IndicatorType.VOLUME_CHANGE;
    }

    @Override
    protected BigDecimal getValue(PriceData priceData) {
        return priceData.getVolume();
    }

    @Override
    protected BigDecimal calculateValue(Integer precision) {
        BigDecimal sum = BigDecimal.ZERO;
        for (PriceData priceData : priceHistory) {
            sum = sum.add(getValue(priceData));
        }

        return sum.setScale(precision, RoundingMode.HALF_UP);
    }
}
