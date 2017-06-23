package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static ee.codestone.model.type.IndicatorType.VOLUME;
import static ee.codestone.model.type.IndicatorType.VOLUME_SUM;

/**
 * Created by vahurkaar on 30.09.16.
 */
public class VolumeSumIndicatorLogic extends IndicatorLogic {

    private Integer period;

    private LinkedList<BigDecimal> history = new LinkedList<>();

    public VolumeSumIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return VOLUME_SUM;
    }

    @Override
    public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalculate) {
        if (recalculate) history.pollLast();
        history.addLast(priceData.getVolume());
        if (history.size() > period) {
            history.pollFirst();
        }

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("value", calculateValue(precision));
        return result;
    }

    private BigDecimal calculateValue(Integer precision) {
        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal volume : history) {
            sum = sum.add(volume);
        }

        return sum.setScale(precision, RoundingMode.HALF_UP);
    }
}
