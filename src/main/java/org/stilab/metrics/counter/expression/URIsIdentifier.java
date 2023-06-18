package org.stilab.metrics.counter.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URIsIdentifier {
   public List<String> uris = new ArrayList<>();
   public URIsIdentifier() {}
   public List<String> getUris(){
     return uris;
   }
   public int countUri(String blockContent) {
     Pattern pattern = Pattern
                      .compile("\\b(?:\\w+:\\/\\/|www\\.)[^\\s]+");
     Matcher matcher = pattern
                      .matcher(blockContent);
     int count = 0;
     while (matcher.find()) {
       String url = matcher.group();
       uris.add(url);
       count++;
     }
     return count;
   }
}
