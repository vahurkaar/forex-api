package ee.codestone.indicator;

import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 24/09/16
 */
public abstract class IndicatorLogic {

    private static final Integer DEFAULT_PRECISION = 6;

    private Integer groupId;

    protected Map<String, BigDecimal> params;

    public IndicatorLogic(Map<String, BigDecimal> params) {
        this.params = params;
    }

    public abstract IndicatorType getType();

    public Map<String, BigDecimal> calculate(PriceData chartData) {
        return calculate(chartData, false);
    }

    public Map<String, BigDecimal> calculate(PriceData chartData, boolean recalculate) {
        return calculate(chartData, DEFAULT_PRECISION, recalculate);
    }

    public abstract Map<String, BigDecimal> calculate(PriceData chartData, Integer precision, boolean recalculate);


    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
