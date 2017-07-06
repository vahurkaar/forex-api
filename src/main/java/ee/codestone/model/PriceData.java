package ee.codestone.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Returns candlestick chart data.
 * Required GET parameters are
 * "currencyPair",
 * "period" (candlestick period in seconds; valid values are 300, 900, 1800, 7200, 14400, and 86400),
 * "start", and "end".
 *
 * "Start" and "end" are given in UNIX timestamp format and used to specify the date range for the data returned.
 *
 * Sample output:

 [{"date":1405699200,"high":0.0045388,"low":0.00403001,"open":0.00404545,"close":0.00427592,"volume":44.11655644,
 "quoteVolume":10259.29079097,"weightedAverage":0.00430015}, ...]
 *
 * Created by vahurkaar on 20/03/2017.
 */
public class PriceData implements Serializable {

    private Timestamp date;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal volume;
    private BigDecimal quoteVolume;
    private Integer candlePeriod;

    public boolean isBullish() {
        return getOpen().compareTo(getClose()) < 0;
    }

    public boolean isBearish() {
        return getOpen().compareTo(getClose()) > 0;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(BigDecimal quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public Integer getCandlePeriod() {
        return candlePeriod;
    }

    public void setCandlePeriod(Integer candlePeriod) {
        this.candlePeriod = candlePeriod;
    }

    @Override
    public String toString() {
        return "PriceData{" +
                "date=" + date +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", close=" + close +
                ", volume=" + volume +
                ", quoteVolume=" + quoteVolume +
                '}';
    }
}
