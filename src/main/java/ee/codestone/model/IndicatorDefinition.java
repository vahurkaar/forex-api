package ee.codestone.model;

import ee.codestone.model.type.IndicatorType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 02/10/16
 */
public class IndicatorDefinition {

    private IndicatorType indicatorType;

    private Map<String, BigDecimal> params;

    private Integer groupId;

    public IndicatorDefinition() {
    }

    public IndicatorDefinition(IndicatorType indicatorType, Map<String, BigDecimal> params, Integer groupId) {
        this.indicatorType = indicatorType;
        this.params = params;
        this.groupId = groupId;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public Map<String, BigDecimal> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, BigDecimal> params) {
        this.params = params;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
