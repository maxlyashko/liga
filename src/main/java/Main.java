import org.jsoup.Jsoup;

public class Main {
    public static void main ( String[] args ) {
        String test = "2022;</div>";

        System.out.println (test.replaceAll ( "<[^>]*>" , " $0 ").replaceAll ( "[;]", " ; " ));
    }
}
