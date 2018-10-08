package PA1;
import java.io.*;
import java.util.ArrayList;
import java.net.*;
import java.util.HashSet;
import java.util.List;

public class WikiCrawler {


    public static final String BASE_URL = "en.wikipedia.org";
    private String seed;
    private int max;
    private String[] topics;
    private String output;


    public WikiCrawler(String seed, int max, String[] topics, String output) throws IOException {
        this.seed = seed;
        this.max = max;
        this.topics = topics;
        this.output = output;

        File f = new File(output);
        try {
            f.createNewFile();
            PrintWriter writer = new PrintWriter(f);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            print("Output file is not valid");
            throw e;
        }
    }


    /**the method takes as input a document representing an entire HTML document.
     * It returns a list of strings consisting of links from the document.
     * You can assume that the document is HTML from some wiki page. The method must
      */
     public ArrayList<String> extractLinks(String document){
         print("\nExtracting Links");
         if(document == null) return null;
         //remove everything before first <p>
         int trimmedIndex = document.indexOf("<p>");
         String trimmed = document.substring(trimmedIndex);
         String link;
         ArrayList<String> links = new ArrayList<>();

         int start = trimmed.indexOf("/wiki/");
         int iterator;
         while(start != -1){

             iterator = trimmed.indexOf("\"", start);

             link = trimmed.substring(start, iterator);
             String decoded;
             try {
                 decoded = URLDecoder.decode(link, "UTF-8");
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
                 decoded = "";
             }
             if(decoded.equals("/wiki/Main_Page"))
                 return links;
             if(!decoded.contains("#") && !decoded.contains(":"))
                 links.add(link);

             start = trimmed.indexOf("/wiki/", iterator);
         }

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

        HashSet<String> discovered = new HashSet<>();
        int numPagesOpened = 0;

        crawlHelper(focused, discovered, seed, numPagesOpened);

    }

    public void crawlHelper(boolean focused, HashSet<String> discovered, String url, int numPagesOpened){


        PriorityQ pq = new PriorityQ();

        //open seed page
        String seedDoc = getWebPage(url);

        //increment numPagesOpened by 1
        numPagesOpened++;

        //search for links
        List<String> links = extractLinks(seedDoc);


        for(String link: links){
            if(!discovered.contains(link)) {
                if(numPagesOpened++ % 20 == 0){
                    print("Being Polite, sleeping for 3 seconds");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Write Edge
                writeEdgeToOutput(url, link);
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
                return;
            }
        }

        crawlHelper(focused, discovered, pq.extractMax(), numPagesOpened);

        return;
    }


    //Gets web page given it's URL
    public String getWebPage(String url){

        System.out.print("retrieving: " + url);
        String result = "";

        try {
            URI uri = new URI(
                    "https",
                    BASE_URL,
                    url,
                    null);
            URL urlObj = new URL(uri.toString());
            InputStream is = null;
            is = urlObj.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }
        } catch (IOException | URISyntaxException e) {
            print("\nCan't Retrieve URL. Probably wrong subdomain");
        }


        return result;

    }

    private int getRelevance(String url){
        String page;

        page = getWebPage(url);
        int trimmedIndex = page.indexOf("<p>");
        String trimmed = page.substring(trimmedIndex);

        int total = 0;
        for(String topic: topics){
            total += numberOfOccurences(trimmed, topic);
        }

        System.out.print(" | relevance: " + total + "\n");
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

    private void writeEdgeToOutput(String node1, String node2){

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
            writer.append('\n');
            writer.append(node1 + " -> " + node2);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean print (String str){
        System.out.println(str);
        return true;
    }



}
