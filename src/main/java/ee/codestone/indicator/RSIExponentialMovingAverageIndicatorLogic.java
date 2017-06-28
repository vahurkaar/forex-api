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
public class RSIExponentialMovingAverageIndicatorLogic extends ExponentialMovingAverageIndicatorLogic {

    private RelativeStrengthIndexIndicatorLogic relativeStrengthIndexIndicatorLogic;

    public RSIExponentialMovingAverageIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        Map<String, BigDecimal> rsiParams = new HashMap<>();
        rsiParams.put("period", params.get("emaPeriod"));
        this.relativeStrengthIndexIndicatorLogic = new RelativeStrengthIndexIndicatorLogic(rsiParams);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.EMA_RSI;
    }

    @Override
    protected BigDecimal getValue(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> rsiValues = relativeStrengthIndexIndicatorLogic.calculateValues(priceData, precision, recalculate);
        return rsiValues.get("value");
    }
}
