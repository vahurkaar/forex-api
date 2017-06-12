package ee.codestone.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 26/03/2017
 */
public class ChartData {

    private PriceData priceData;
    private PriceData heikenAshiPriceData;
    private List<IndicatorValue> indicators;
    private Map<String, BigDecimal> markers;

    public ChartData(PriceData priceData) {
        this.priceData = priceData;
    }

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
        if (markers == null) {
            markers = new HashMap<>();
        }
        return markers;
    }

    public void setMarkers(Map<String, BigDecimal> markers) {
        this.markers = markers;
    }

    public PriceData getHeikenAshiPriceData() {
        return heikenAshiPriceData;
    }

    public void setHeikenAshiPriceData(PriceData heikenAshiPriceData) {
        this.heikenAshiPriceData = heikenAshiPriceData;
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
