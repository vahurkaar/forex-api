package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * AO = SMA(High+Low)/2, 5 Periods) - SMA(High+Low/2, 34 Periods)
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 25/09/16
 */
public class AwesomeOscillatorIndicatorLogic extends IndicatorLogic {

    private HighLowSimpleMovingAverageIndicatorLogic shortPeriodIndicator;
    private HighLowSimpleMovingAverageIndicatorLogic longPeriodIndicator;

    public AwesomeOscillatorIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        Map<String, BigDecimal> shortPeriodIndicatorParams = new HashMap<>();
        Map<String, BigDecimal> longPeriodIndicatorParams = new HashMap<>();

        shortPeriodIndicatorParams.put("period", params.get("shortPeriod"));
        longPeriodIndicatorParams.put("period", params.get("longPeriod"));
        shortPeriodIndicator = new HighLowSimpleMovingAverageIndicatorLogic(shortPeriodIndicatorParams);
        longPeriodIndicator = new HighLowSimpleMovingAverageIndicatorLogic(longPeriodIndicatorParams);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.AO;
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        BigDecimal shortValue = shortPeriodIndicator.calculateValues(priceData, precision, recalculate).get("value");
        BigDecimal longValue = longPeriodIndicator.calculateValues(priceData, precision, recalculate).get("value");

        Map<String, BigDecimal> result = new HashMap<>();
        if (shortValue != null && longValue != null) {
            result.put("value", shortValue.subtract(longValue));
        } else {
            result.put("value", null);
        }

        return result;
    }

    public class HighLowSimpleMovingAverageIndicatorLogic extends SimpleMovingAverageIndicatorLogic {

        public HighLowSimpleMovingAverageIndicatorLogic(Map<String, BigDecimal> params) {
            super(params);
        }

        @Override
        protected BigDecimal getValue(PriceData priceData, Integer precision) {
            return priceData.getHigh().add(priceData.getLow()).divide(new BigDecimal(2));
        }
    }
}
