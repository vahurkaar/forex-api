package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/2017
 */
public class MoneyFlowIndexIndicatorLogic extends IndicatorLogic {

    private final Integer period;
    private LinkedList<PriceData> history = new LinkedList<>();
    private PriceData previousRemovedHistoryItem;

    public MoneyFlowIndexIndicatorLogic(Map<String, BigDecimal> params) {
        super(params);
        this.period = params.get("period").intValue();
    }

    @Override
    public IndicatorType getType() {
        return IndicatorType.MFI;
    }

    @Override
    public Map<String, BigDecimal> calculateValues(PriceData priceData, Integer precision, boolean recalculate) {
        Map<String, BigDecimal> result = new HashMap<>();

        if (recalculate) {
            history.pollLast();
            if (previousRemovedHistoryItem != null) history.addFirst(previousRemovedHistoryItem);
        }

        addDataToHistory(priceData);

        if (history.size() < period + 1) {
            result.put("value", null);
            return result;
        }

        BigDecimal moneyFlowRatio = calculateMoneyFlowRatio(precision);
        result.put("value", calculateMoneyFlowIndex(moneyFlowRatio, precision));
        return result;
    }

    private BigDecimal calculateMoneyFlowRatio(Integer precision) {
        BigDecimal positiveMoneyFlow = BigDecimal.ZERO;
        BigDecimal negativeMoneyFlow = BigDecimal.ZERO;

        for (int i = 1; i < history.size(); i++) {
            PriceData previousPriceData = history.get(i - 1);
            PriceData priceData = history.get(i);

            BigDecimal rawMoneyFlow = calculateRawMoneyFlow(priceData, precision);
            if (previousPriceData.getClose().compareTo(priceData.getClose()) < 0) {
                positiveMoneyFlow = positiveMoneyFlow.add(rawMoneyFlow);
            } else if (previousPriceData.getClose().compareTo(priceData.getClose()) > 0) {
                negativeMoneyFlow = negativeMoneyFlow.add(rawMoneyFlow);
            }
        }

        if (negativeMoneyFlow.compareTo(BigDecimal.ZERO) > 0) {
            return positiveMoneyFlow.divide(negativeMoneyFlow, precision * 2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateRawMoneyFlow(PriceData priceData, Integer precision) {
        BigDecimal typicalPrice = calculateTypicalPrice(priceData, precision);
        return typicalPrice.multiply(priceData.getVolume());
    }

    private BigDecimal calculateTypicalPrice(PriceData priceData, Integer precision) {
        return priceData.getHigh()
                .add(priceData.getLow())
                .add(priceData.getClose())
                .divide(new BigDecimal(3), precision * 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateMoneyFlowIndex(BigDecimal moneyFlowRatio, Integer precision) {
        return new BigDecimal(100).subtract(
                new BigDecimal(100).divide(BigDecimal.ONE.add(moneyFlowRatio), precision, RoundingMode.HALF_UP)
        );
    }

    private void addDataToHistory(PriceData forexData) {
        history.offerLast(forexData);
        if (history.size() > period + 1) {
            previousRemovedHistoryItem = history.pollFirst();
        }
    }
}
