package PA1;
import java.io.IOException;
import java.util.ArrayList;
import java.net.*;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

public class WikiCrawler {


    public static final String BASE_URL = "https://en.wikipedia.org";
    private String seed;
    private int max;
    private String[] topics;
    private String output;


    public WikiCrawler(String seed, int max, String[] topics, String output){
        this.seed = seed;
        this.max = max;
        this.topics = topics;
        this.output = output;
    }


    /**the method takes as input a document representing an entire HTML document.
     * It returns a list of strings consisting of links from the document.
     * You can assume that the document is HTML from some wiki page. The method must
      */
     public ArrayList<String> extractLinks(String document){
         if(document == null) return null;
         //remove everything before first <p>
         int iterator = document.indexOf("<p>");
         String trimmed = document.substring(iterator);
         String link;
         ArrayList<String> links = new ArrayList<>();
         int start = trimmed.indexOf("/wiki/", iterator);
         while(start != -1){

             iterator = trimmed.indexOf("\"", start);

             link = trimmed.substring(start, iterator);
             if(!link.contains("#") && !link.contains(":"))
                 links.add(link);

             start = trimmed.indexOf("/wiki/", iterator);
         }
//         for(String s: links){
//             print(s);
//         }
         print("number of links: " + links.size());
         return links;

     }

    /**
     * crawls/explores the web pages starting from the seed URL.
     * Crawl the first max number of pages (including the seed page),
     * that contains every keywords in the Topics list
     * (if Topics list is empty then this condition is vacuously considered true),
     * and are explored starting from the seed.
     * @param focused
     */
    public void crawl(boolean focused){

        int numPagesOpened = 0;
        HashSet<String> discovered = new HashSet<>();
        PriorityQ pq = new PriorityQ();

        //open seed page
        String seedDoc;
        try {
            seedDoc = getWebPage(seed);
        } catch (IOException e) {
            print("Unable to access seed page: " + seed);
            print("Please specify new seed");
            seedDoc = null;
            e.printStackTrace();
        }

        //increment numPagesOpened by 1
        numPagesOpened++;

        //search for links
        List<String> links = extractLinks(seedDoc);


        for(String link: links){
            if(!discovered.contains(link)) {
                if(numPagesOpened++ % 20 == 0){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //case: focused true -> for each page, determine relevance
                if(focused){
                    int prty = getRelevance(link);
                    pq.add(link, prty);
                }
                //case: focused false -> put urls in FIFO
                else{
                    pq.add(link, max - numPagesOpened);
                }
                discovered.add(link);
            }
            if(numPagesOpened >= max) {
                break;
            }
        }


        print(pq.toString());
        print(pq.extractMax());
        return;
        //pop first for pQ or FIFO
        //add it to discovered HashMap


    }

    //Gets web page given it's URL
    public String getWebPage(String url) throws java.io.IOException{

        print("retrieving: " + url);
        URL urlObj = new URL(BASE_URL+url);
        InputStream is = urlObj.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += line + "\n";
        }

        return result;

    }

    private int getRelevance(String url){
        String page;
        try {
            page = getWebPage(url);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int total = 0;
        for(String topic: topics){
            total += numberOfOccurences(page, topic);
        }
        return total;

    }

    public int numberOfOccurences(String page, String topicStr){

        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = page.indexOf(topicStr,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += topicStr.length();
            }
        }

        return count;
    }




    public boolean print (String str){
        System.out.println(str);
        return true;
    }


}
