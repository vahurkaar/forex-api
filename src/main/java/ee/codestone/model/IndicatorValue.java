package ee.codestone.model;

import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 26/03/2017
 */
public class IndicatorValue {
    private IndicatorType type;
    private Integer groupId;
    private Map<String, BigDecimal> values;

    public IndicatorValue(IndicatorType type, Integer groupId, Map<String, BigDecimal> values) {
        this.type = type;
        this.groupId = groupId;
        this.values = values;
    }

    public IndicatorType getType() {
        return type;
    }

    public void setType(IndicatorType type) {
        this.type = type;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Map<String, BigDecimal> getValues() {
        return values;
    }

    public void setValues(Map<String, BigDecimal> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "IndicatorValue{" +
                "type=" + type +
                ", values=" + values +
                '}';
    }
}
