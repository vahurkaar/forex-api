package ee.codestone.model;

import java.util.List;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 26/03/2017
 */
public class ChartData {

    private PriceData priceData;
    private List<IndicatorValue> indicators;

    public ChartData(PriceData priceData, List<IndicatorValue> indicators) {
        this.priceData = priceData;
        this.indicators = indicators;
    }

    public PriceData getPriceData() {
        return priceData;
    }

    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
    }

    public List<IndicatorValue> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<IndicatorValue> indicators) {
        this.indicators = indicators;
    }

    @Override
    public String toString() {
        return "ChartData{" +
                "priceData=" + priceData +
                ", indicators=" + indicators +
                '}';
    }
}
