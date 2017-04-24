package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 25/09/16
 */
public class RelativeStrengthIndexIndicatorLogic extends IndicatorLogic {

    private final Integer period;

    private LinkedList<PriceData> history = new LinkedList<>();

    private BigDecimal previousAverageGain;

    private BigDecimal previousAverageLoss;

    public RelativeStrengthIndexIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.RSI;
    }

    @Override
    public Map<String, BigDecimal> calculate(PriceData priceData, Integer precision, boolean recalulcate) {
        Map<String, BigDecimal> result = new HashMap<>();
        if (history.size() < period) {
            addDataToHistory(priceData);
            result.put("value", null);
            return result;
        }

        BigDecimal averageGain;
        BigDecimal averageLoss;
        if (previousAverageGain != null && previousAverageLoss != null) {
            BigDecimal lastLoss = calculateLastLoss(priceData.getClose());
            BigDecimal lastGain = calculateLastGain(priceData.getClose());

            averageLoss = previousAverageLoss.multiply(BigDecimal.valueOf(period - 1)).add(lastLoss)
                    .divide(BigDecimal.valueOf(period), precision * 2, BigDecimal.ROUND_HALF_UP);
            averageGain = previousAverageGain.multiply(BigDecimal.valueOf(period - 1)).add(lastGain)
                    .divide(BigDecimal.valueOf(period), precision * 2, BigDecimal.ROUND_HALF_UP);
        } else {
            averageLoss = BigDecimal.ZERO;
            averageGain = BigDecimal.ZERO;

            for (int i = 1; i < history.size(); i++) {
                BigDecimal diff = history.get(i).getClose().subtract(history.get(i - 1).getClose());

                averageGain = averageGain.add(diff.compareTo(BigDecimal.ZERO) > 0 ? diff : BigDecimal.ZERO);
                averageLoss = averageLoss.add(diff.compareTo(BigDecimal.ZERO) < 0 ? diff.negate() : BigDecimal.ZERO);
            }

            averageGain = averageGain.divide(BigDecimal.valueOf(period), precision * 2, BigDecimal.ROUND_HALF_UP);
            averageLoss = averageLoss.divide(BigDecimal.valueOf(period), precision * 2, BigDecimal.ROUND_HALF_UP);
        }

        previousAverageGain = averageGain;
        previousAverageLoss = averageLoss;

        BigDecimal resultValue = null;
        if (averageGain != null && averageLoss != null) {
            BigDecimal relativeStrength = averageLoss.compareTo(BigDecimal.ZERO) > 0 ?
                    averageGain.divide(averageLoss, precision * 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
            resultValue = BigDecimal.valueOf(100).subtract(
                    BigDecimal.valueOf(100).divide(
                            BigDecimal.ONE.add(relativeStrength), precision, BigDecimal.ROUND_HALF_UP
                    )
            );
        }

        addDataToHistory(priceData);
        result.put("value", resultValue);
        return result;
    }

    private void addDataToHistory(PriceData forexData) {
        history.offerLast(forexData);
        if (history.size() > period) {
            history.pollFirst();
        }
    }

    private BigDecimal calculateLastGain(BigDecimal closingPrice) {
        BigDecimal lastChange = calculateLastChange(closingPrice);
        return lastChange.compareTo(BigDecimal.ZERO) > 0 ? lastChange : BigDecimal.ZERO;
    }

    private BigDecimal calculateLastLoss(BigDecimal closingPrice) {
        BigDecimal lastChange = calculateLastChange(closingPrice);
        return lastChange.compareTo(BigDecimal.ZERO) < 0 ? lastChange.negate() : BigDecimal.ZERO;
    }

    private BigDecimal calculateLastChange(BigDecimal closingPrice) {
        return closingPrice.subtract(history.peekLast().getClose());
    }

}
