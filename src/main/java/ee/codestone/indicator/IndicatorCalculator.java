package ee.codestone.indicator;

import ee.codestone.model.ChartData;
import ee.codestone.model.IndicatorDefinition;
import ee.codestone.model.IndicatorValue;
import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 24/09/16
 */
public class IndicatorCalculator {

    private Collection<IndicatorLogic> indicatorLogics = new ArrayList<>();

    public IndicatorCalculator(List<IndicatorDefinition> indicatorParams) {
        indicatorParams.forEach(
                param -> indicatorLogics.add(resolveIndicatorLogic(param.getIndicatorType(), param.getParams(), param.getGroupId()))
        );
        // Always add price
        indicatorLogics.add(resolveIndicatorLogic(IndicatorType.PRICE, null, 0));
    }

    private IndicatorLogic resolveIndicatorLogic(IndicatorType indicator, Map<String, BigDecimal> params, Integer groupId) {
        IndicatorLogic indicatorLogic;
        switch (indicator) {
            case SMA:
                indicatorLogic = new SimpleMovingAverageIndicatorLogic(params);
                break;
            case EMA:
                indicatorLogic = new ExponentialMovingAverageIndicatorLogic(params);
                break;
            case EMA_HIGH:
                indicatorLogic = new HighExponentialMovingAverageIndicatorLogic(params);
                break;
            case EMA_LOW:
                indicatorLogic = new LowExponentialMovingAverageIndicatorLogic(params);
                break;
            case BB:
                indicatorLogic = new BollingerBandsIndicatorLogic(params);
                break;
            case SD:
                indicatorLogic = new StandardDeviationIndicatorLogic(params);
                break;
            case PRICE:
                indicatorLogic = new PriceIndicatorLogic(params);
                break;
            default:
                return null;
        }

        indicatorLogic.setGroupId(groupId);
        return indicatorLogic;
    }

    public ChartData calculate(PriceData priceData) {
        return calculate(priceData, false);
    }

    public ChartData calculate(PriceData priceData, boolean recalculate) {
        List<IndicatorValue> calculatedIndicators = new ArrayList<>();
        indicatorLogics.forEach(indicatorLogic -> calculatedIndicators.add(
                new IndicatorValue(indicatorLogic.getType(), indicatorLogic.getGroupId(), indicatorLogic.calculate(priceData, recalculate))));

        return new ChartData(priceData, calculatedIndicators);
    }


}
