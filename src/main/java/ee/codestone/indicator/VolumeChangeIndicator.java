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
public class VolumeChangeIndicator extends ChangeIndicator {

    public VolumeChangeIndicator(Map<String, BigDecimal> params) {
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
    protected BigDecimal calculateValue(PriceData priceData, Integer precision) {
        BigDecimal sum = BigDecimal.ZERO;
        for (PriceData p : priceHistory) {
            sum = sum.add(getValue(p));
        }

        return sum.setScale(precision, RoundingMode.HALF_UP);
    }
}
