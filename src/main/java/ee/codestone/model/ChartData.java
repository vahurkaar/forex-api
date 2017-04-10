package ee.codestone.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 26/03/2017
 */
public class ChartData {

    private PriceData priceData;
    private List<IndicatorValue> indicators;
    private Map<String, BigDecimal> markers;

    public ChartData(PriceData priceData, List<IndicatorValue> indicators, Map<String, BigDecimal> markers) {
        this.priceData = priceData;
        this.indicators = indicators;
        this.markers = markers;
    }

    public ChartData(PriceData priceData, List<IndicatorValue> indicators) {
        this(priceData, indicators, null);
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

    public Map<String, BigDecimal> getMarkers() {
        return markers;
    }

    public void setMarkers(Map<String, BigDecimal> markers) {
        this.markers = markers;
    }

    @Override
    public String toString() {
        return "ChartData{" +
                "priceData=" + priceData +
                ", indicators=" + indicators +
                ", markers=" + markers +
                '}';
    }
}
