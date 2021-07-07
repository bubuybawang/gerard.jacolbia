package planit.gjacolbia.framework.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

@Log4j2
@Data
@AllArgsConstructor
public class PriceModel {
    Currency currency;
    BigDecimal amount;
    Locale locale;

    public static PriceModel of(String price) {
        return PriceModel.of(Locale.US, price);
    }

    public static PriceModel of(Locale locale, String price) {
        try {
            var amount = BigDecimal.valueOf((double)NumberFormat.getCurrencyInstance(locale).parse(price));
            return new PriceModel(Currency.getInstance(locale), amount, locale);
        } catch (ParseException e) {
            log.warn(e.getMessage());
            log.warn("Looks like price/money not displayed in a standard way. Create parsers to manually parse them.");
            return null;
        }
    }

    public PriceModel multiply(int multiplicand) {
        BigDecimal multiplied = this.amount.multiply(BigDecimal.valueOf(multiplicand));
        return PriceModel.of(this.currency.getSymbol(locale) + multiplied);
    }

    @Override
    public String toString() {
        return currency.getSymbol(locale) + amount;
    }
}
