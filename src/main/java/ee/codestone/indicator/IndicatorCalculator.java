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

    private static final Integer DEFAULT_PRECISION = 6;

    private List<IndicatorDefinition> indicators;

    private Collection<IndicatorLogic> indicatorLogics = new ArrayList<>();

    private Integer precision;

    public IndicatorCalculator(List<IndicatorDefinition> indicatorParams) {
        this(indicatorParams, DEFAULT_PRECISION);
    }

    public IndicatorCalculator(List<IndicatorDefinition> indicatorParams, Integer precision) {
        this.indicators = indicatorParams;
        indicatorParams.forEach(
                param -> indicatorLogics.add(resolveIndicatorLogic(param.getIndicatorType(), param.getParams(), param.getGroupId()))
        );
        // Always add price and volume
        indicatorLogics.add(resolveIndicatorLogic(IndicatorType.PRICE, null, 0));
        indicatorLogics.add(resolveIndicatorLogic(IndicatorType.VOLUME, null, 0));
        this.precision = precision;
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
            case EMA_RSI:
                indicatorLogic = new RSIExponentialMovingAverageIndicatorLogic(params);
                break;
            case BB:
                indicatorLogic = new BollingerBandsIndicatorLogic(params);
                break;
            case SD:
                indicatorLogic = new StandardDeviationIndicatorLogic(params);
                break;
            case ATR:
                indicatorLogic = new AverageTrueRangeIndicatorLogic(params);
                break;
            case ADX:
                indicatorLogic = new AverageDirectionalIndexIndicatorLogic(params);
                break;
            case AO:
                indicatorLogic = new AwesomeOscillatorIndicatorLogic(params);
                break;
            case GER:
                indicatorLogic = new GrimesEfficiencyRatioIndicatorLogic(params);
                break;
            case RSI:
                indicatorLogic = new RelativeStrengthIndexIndicatorLogic(params);
                break;
            case PRICE:
                indicatorLogic = new PriceIndicatorLogic(params);
                break;
            case VOLUME:
                indicatorLogic = new VolumeIndicatorLogic(params);
                break;
            case MOMENTUM:
                indicatorLogic = new MomentumIndicatorLogic(params);
                break;
            case EMA_VOLUME:
                indicatorLogic = new VolumeExponentialMovingAverageIndicatorLogic(params);
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
                new IndicatorValue(indicatorLogic.getType(), indicatorLogic.getGroupId(), indicatorLogic.calculate(priceData, precision, recalculate))));

        return new ChartData(priceData, calculatedIndicators);
    }

    public List<IndicatorDefinition> getIndicators() {
        return indicators;
    }
}
