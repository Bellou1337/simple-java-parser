import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public final Pattern DATA_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}");

    private String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = DATA_PATTERN.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Не нашёл дату");
    }

    private int printPartValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            boolean isDay = valueLn.text().contains("День");
            boolean isEvening = valueLn.text().contains("Вечер");
            if (isEvening) {
                iterationCount = 1;
            }
            if (isDay) {
                iterationCount = 2;
            }
            if (isMorning) {
                iterationCount = 3;
            }
        }
        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + " ");
            }
            System.out.println();
        }
        return iterationCount;
    }

    private Document getPage() throws IOException {
        final String SITE_URL = "https://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(SITE_URL), 3000);
        return page;
    }

    public void init() throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element search : names) {
            String dateString = search.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date);
            int iterationCount = printPartValues(values, index);
            index += iterationCount;
        }


    }
}
