package PA1;

import java.io.IOException;
import java.util.List;
public class PriorityTest {

    public static void main(String[] args){

//        PriorityQ p = new PriorityQ();
//        for(int i = 0; i < 20; i++){
//            p.add("asdf", (int) (Math.random() * 100));
//        }


//        System.out.println("Max Element: " + p.returnMax());
//        System.out.println(p.toString());
//        System.out.println("Extracting Max..");
//        p.extractMax();
//        System.out.println(p.toString());
//        System.out.println("Decrementing Priority of element 5 (Priority:" + p.getKey(5) + ") by 10");
//        p.decrementPriority(5, 10);
//        System.out.println(p.toString());
//        System.out.println("Removing Element 3...(Priority: " + p.getKey(3) + ")");
//        p.remove(3);
//        System.out.println(p.toString());

        String[] topics = {"TV", "Film", "television"};
        WikiCrawler wc = new WikiCrawler("/wiki/Television_Critics_Association", 45, topics, "asdf" );
        wc.crawl(true);

    }

}
