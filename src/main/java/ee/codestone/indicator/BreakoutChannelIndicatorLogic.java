package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 24/09/16
 */
public class BreakoutChannelIndicatorLogic extends IndicatorLogic {

    protected final Integer period;

    private LinkedList<PriceData> history = new LinkedList<>();

    private PriceData lastDroppedHistoryData;

    public BreakoutChannelIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.BREAKOUT;
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();
        BigDecimal high = null;
        BigDecimal low = null;

        if (recalculate && lastDroppedHistoryData != null) {
            history.pollLast();
            history.addFirst(lastDroppedHistoryData);
        }

        if (history.size() == period) {
            for (PriceData pd : history) {
                if (high == null || pd.getHigh().compareTo(high) > 0) {
                    high = pd.getHigh();
                }

                if (low == null || pd.getLow().compareTo(low) < 0) {
                    low = pd.getLow();
                }
            }
        }

        history.addLast(priceData);
        if (history.size() > period) {
            lastDroppedHistoryData = history.pollFirst();
        }

        result.put("high", high);
        result.put("low", low);
        return result;
    }

}
