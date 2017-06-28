package ee.codestone.indicator;

import ee.codestone.model.IndicatorValue;
import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static ee.codestone.model.type.IndicatorType.SWING_HIGH_LOW;

/**
 * Created by vahurkaar on 30.09.16.
 */
public class SwingHighLowIndicator extends IndicatorLogic {

    private Integer period;

    private LinkedList<IndicatorValue> cache = new LinkedList<>();

    private IndicatorValue lastIndicatorValue;

    public SwingHighLowIndicator(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return SWING_HIGH_LOW;
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("high", priceData.getHigh());
        result.put("low", priceData.getLow());
        result.put("date", new BigDecimal(priceData.getDate().getTime()));
        return result;
    }

    @Override
    public IndicatorValue calculate(PriceData chartData, Integer precision, boolean recalculate) {
        IndicatorValue indicatorValue = super.calculate(chartData, precision, recalculate);
        updateCache(recalculate, indicatorValue);
        calculateSwingValues();

        return indicatorValue;
    }

    private void calculateSwingValues() {
        if (cache.size() == 2 * period + 1) {
            IndicatorValue indicatorValue = cache.get(period);
            BigDecimal high = indicatorValue.getValues().get("high");
            BigDecimal low = indicatorValue.getValues().get("low");

            boolean isHighestHigh = true;
            boolean isLowestLow = true;
            for (IndicatorValue cachedIndicatorValue : cache) {
                if (cachedIndicatorValue.getValues().get("date").equals(indicatorValue.getValues().get("date"))) continue;

                BigDecimal h = cachedIndicatorValue.getValues().get("high");
                BigDecimal l = cachedIndicatorValue.getValues().get("low");

                isHighestHigh &= high.compareTo(h) >= 0;
                isLowestLow &= low.compareTo(l) <= 0;
            }

            if (isHighestHigh) indicatorValue.getValues().put("swingHigh", indicatorValue.getValues().get("high"));
            if (isLowestLow) indicatorValue.getValues().put("swingLow", indicatorValue.getValues().get("low"));

            lastIndicatorValue = indicatorValue;
        }
    }

    private void updateCache(boolean recalculate, IndicatorValue indicatorValue) {
        if (recalculate) {
            cache.pollLast();
            if (lastIndicatorValue != null) {
                lastIndicatorValue.getValues().remove("swingHigh");
                lastIndicatorValue.getValues().remove("swingLow");
            }
        }

        cache.addLast(indicatorValue);
        if (cache.size() > 2 * period + 1) {
            cache.pollFirst();
        }
    }
}
