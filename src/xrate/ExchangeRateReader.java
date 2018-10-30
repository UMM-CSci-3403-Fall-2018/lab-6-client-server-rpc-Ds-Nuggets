package xrate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provide access to basic currency exchange rate services.
 * 
 * @author PUT YOUR TEAM NAME HERE
 */
public class ExchangeRateReader
{

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL
     *            the base URL for requests
     */

    URL globalURL;

    public ExchangeRateReader(String baseURL) throws MalformedURLException
    {
        // TODO Your code here
        /*
         * DON'T DO MUCH HERE!
         * People often try to do a lot here, but the action is actually in
         * the two methods below. All you need to do here is store the
         * provided `baseURL` in a field so it will be accessible later.
         */
        //"facultypages.morris.umn.edu"
        globalURL = new URL(baseURL);

    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException, UnsupportedOperationException
    {
        // TODO Your code here

        globalURL = new URL(globalURL.toString() + "-" + year + "-" + safeDate(month) + "-" + safeDate(day) + "?access=");

        InputStream inputStream = globalURL.openStream();

        Reader readme = new InputStreamReader(inputStream);

        JsonObject urlInfo = new JsonParser().parse(readme).getAsJsonObject();

        //Return dummy object
        return 0;

    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     * 
     * @param fromCurrency
     *            the currency code we're exchanging *from*
     * @param toCurrency
     *            the currency code we're exchanging *to*
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(String fromCurrency, String toCurrency, int year, int month, int day) throws IOException, UnsupportedOperationException
    {
        // TODO Your code here

    }

    public float getRate(JsonObject ratesInfo, String country_code)
    {

        JsonObject crates = ratesInfo.getAsJsonObject("rates");

        return ratesInfo.getAsJsonObject("rates").get(country_code).getAsFloat();

        //return crates.get(country_code).getAsFloat();



    }

    public String safeDate(int num)
    {
        if(num < 10)
        {
            return "0" + Integer.toString(num);
        }

        else
        {
            return Integer.toString(num);
        }
    }
}