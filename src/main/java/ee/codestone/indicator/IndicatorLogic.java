package ee.codestone.indicator;

import ee.codestone.model.IndicatorValue;
import ee.codestone.model.PriceData;
import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 24/09/16
 */
public abstract class IndicatorLogic {

    private Integer groupId;

    protected Map<String, BigDecimal> params;

    public IndicatorLogic(Map<String, BigDecimal> params) {
        this.params = params;
    }

    public abstract IndicatorType getType();

    public abstract Map<String, BigDecimal> calculateValues(PriceData chartData, Integer precision, boolean recalculate);

    public IndicatorValue calculate(PriceData chartData, Integer precision, boolean recalculate) {
        return new IndicatorValue(getType(), groupId, calculateValues(chartData, precision, recalculate));
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
