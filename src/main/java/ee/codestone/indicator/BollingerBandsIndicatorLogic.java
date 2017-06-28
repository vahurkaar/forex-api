package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 25/09/16
 */
public class BollingerBandsIndicatorLogic extends IndicatorLogic {

    private Integer deviation;

    private SimpleMovingAverageIndicatorLogic simpleMovingAverage;
    private StandardDeviationIndicatorLogic standardDeviation;

    public BollingerBandsIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.deviation = params.get("deviation").intValue();
        this.simpleMovingAverage = new SimpleMovingAverageIndicatorLogic(params);
        this.standardDeviation = new StandardDeviationIndicatorLogic(params);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.BB;
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        BigDecimal standardDeviationValue = standardDeviation.calculateValues(priceData, precision * 2, recalculate).get("value");
        BigDecimal middleValue = simpleMovingAverage.calculateValues(priceData, precision, recalculate).get("value");
        BigDecimal upperValue = null;
        BigDecimal lowerValue = null;

        if (middleValue != null) {
            upperValue = middleValue.add(standardDeviationValue.multiply(new BigDecimal(deviation))
                    .setScale(precision, RoundingMode.HALF_UP));
            lowerValue = middleValue.subtract(standardDeviationValue.multiply(new BigDecimal(deviation))
                    .setScale(precision, RoundingMode.HALF_UP));
        }

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("middle", middleValue);
        result.put("high", upperValue);
        result.put("low", lowerValue);
        return result;
    }
}
