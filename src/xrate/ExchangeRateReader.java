package xrate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.util.Properties;

/**
 * Provide access to basic currency exchange rate services.
 * 
 * @author Blake Bellamy, Prince Nwaonicha
 */
public class ExchangeRateReader
{

    //Construct 'global' variables to be accessed throughout the program.
    public URL globalURL;
    private String accessKey;

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
    public ExchangeRateReader(String baseURL) throws MalformedURLException, IOException
    {
        // TODO Your code here
        /*
         * DON'T DO MUCH HERE!
         * People often try to do a lot here, but the action is actually in
         * the two methods below. All you need to do here is store the
         * provided `baseURL` in a field so it will be accessible later.
         */

        //Assigns the global variable 'globalURL' the base string that the user constructs
        globalURL = new URL(baseURL);

        //Read's access key
        readAccessKeys();

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
        // Sets the initialized globalUrl to the format of the url website that we are using with all parameters.
        globalURL = new URL(globalURL.toString() + year + "-" + safeDate(month) + "-" + safeDate(day) + "?access=" + accessKey);

        // Begin 'accepting' the raw text from the website.
        InputStream inputStream = globalURL.openStream();

        // Begin reading that raw text to an object.
        Reader readme = new InputStreamReader(inputStream);

        // We get the raw text of the website and we turn it into a formatted Json that we can navigate to.
        JsonObject urlInfo = new JsonParser().parse(readme).getAsJsonObject();

        //Call getRate to return the info that we want.
        return getRate(urlInfo, currencyCode);

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
        // Sets the initialized globalUrl to the format of the url website that we are using with all parameters.
        globalURL = new URL(globalURL.toString() + year + "-" + safeDate(month) + "-" + safeDate(day) + "?access_key=" + accessKey);

        // Begin 'accepting' the raw text from the website.
        InputStream inputStream = globalURL.openStream();

        // Begin reading that raw text to an object.
        Reader readme = new InputStreamReader(inputStream);

        // We get the raw text of the website and we turn it into a formatted Json that we can navigate to.
        JsonObject urlInfo = new JsonParser().parse(readme).getAsJsonObject();

        //Call getRate to return the info that we want, but this time we divide the two different currencies to get the exchange rate 'difference'.
        return getRate(urlInfo, fromCurrency) / getRate(urlInfo, toCurrency);

    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     *
     * @param ratesInfo
     *              The object containing every element from the json (the api's response in this case)
     * @param country_code
     *              The 3 character currency code associated with each country
     * @return the desired exchange rate
     *
     */

    public float getRate(JsonObject ratesInfo, String country_code)
    {

        //Get the json object called "rates", then get that new object's element called "country_code" as a float then return it.
        return ratesInfo.getAsJsonObject("rates").get(country_code).getAsFloat();

    }

    //This is so that we can return the single digit integers in the form of 0Int.
    public String safeDate(int num)
    {
        //If it's less than 10 it's a single digit.
        if(num < 10)
        {
            //Add a zero and return as a string.
            return "0" + Integer.toString(num);
        }

        else
        {
            //It's not a single digit, so we are safe to return as a string.
            return Integer.toString(num);
        }
    }

    private void readAccessKeys() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            // Don't change this filename unless you know what you're doing.
            // It's crucial that we don't commit the file that contains the
            // (private) access keys. This file is listed in `.gitignore` so
            // it's safe to put keys there as we won't accidentally commit them.
            in = new FileInputStream("etc/access_keys.properties");
        } catch (FileNotFoundException e) {
            /*
             * If this error gets generated, make sure that you have the desired
             * properties file in your project's `etc` directory. You may need
             * to rename the file ending in `.sample` by removing that suffix.
             */
            System.err.println("Couldn't open etc/access_keys.properties; have you renamed the sample file?");
            throw(e);
        }
        properties.load(in);
        // This assumes we're using Fixer.io and that the desired access key is
        // in the properties file in the key labelled `fixer_io`.
        accessKey = properties.getProperty("fixer_io");
    }
}