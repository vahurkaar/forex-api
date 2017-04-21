package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (<a href="mailto:vahur.kaar@tieto.com">vahur.kaar@tieto.com</a>)
 * @since 21.04.2017.
 */
public class MomentumIndicatorLogic extends SimpleMovingAverageIndicatorLogic {

    private LinkedList<PriceData> history = new LinkedList<>();

    public MomentumIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.MOMENTUM;
    }

    @Override
    public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();

        if (recalculate) history.pollLast();
        history.addLast(priceData);
        if (history.size() > period) {
            history.pollFirst();
        }

        result.put("value", calculateValue());
        return result;
    }

    private BigDecimal calculateValue() {
        PriceData first = history.peekFirst();
        PriceData last = history.peekLast();
        return last.getClose().subtract(first.getClose());
    }
}
