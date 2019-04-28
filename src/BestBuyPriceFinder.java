import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class BestBuyPriceFinder extends PriceFinder {

    double findPrice(String itemUrl) {
        HttpURLConnection con = null;
        try{
            URL url = new URL(itemUrl);
            con = (HttpURLConnection) url.openConnection();
            String encoding = con.getContentEncoding();
            if (encoding == null)
                encoding = "utf-8";

            System.out.println(encoding);

            InputStreamReader reader = null;
            if("gzip".equals(encoding)){
                reader = new InputStreamReader(new GZIPInputStream(con.getInputStream()));
            } else {
                reader = new InputStreamReader(con.getInputStream(), encoding);
            }

            BufferedReader in = new BufferedReader(reader);
            String line;

            Pattern pattern = Pattern.compile("\\$?(\\d+\\.\\d{2})");

            while ((line = in.readLine()) != null){
                if (line.contains("\"currentPrice\":")) {
                    Matcher matcher = pattern.matcher(line);

                    matcher.find();
                    matcher.find();
                    String price = matcher.group();
                    System.out.println(price);
                    return Double.parseDouble(price);
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }

        return 0;
    }
}
