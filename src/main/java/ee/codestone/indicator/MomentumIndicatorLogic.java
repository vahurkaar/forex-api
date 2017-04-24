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
public class MomentumIndicatorLogic extends ExponentialMovingAverageIndicatorLogic {

    private BigDecimal depth;

    private LinkedList<PriceData> history = new LinkedList<>();

    public MomentumIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.depth = params.get("depth");
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.MOMENTUM;
    }

    @Override
    protected BigDecimal getValue(PriceData priceData, Integer precision, boolean recalculate) {
        if (recalculate) history.pollLast();
        history.addLast(priceData);
        if (history.size() > depth.intValue()) {
            history.pollFirst();
        }

        return calculateValue();
    }

    private BigDecimal calculateValue() {
        PriceData first = history.peekFirst();
        PriceData last = history.peekLast();
        return last.getClose().subtract(first.getClose());
    }
}
