package ee.codestone.util;

import ee.codestone.model.ChartData;
import ee.codestone.model.IndicatorValue;
import ee.codestone.model.type.IndicatorType;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 12/04/2017
 */
public class ForexApiUtils {

    public static IndicatorValue getIndicatorValue(ChartData chartData, IndicatorType indicatorType) {
        for (IndicatorValue indicator : chartData.getIndicators()) {
            if (indicator.getType().equals(indicatorType)) {
                return indicator;
            }
        }

        return null;
    }

}
