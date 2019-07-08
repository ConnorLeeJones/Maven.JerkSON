package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {


    private Integer errors = 0;

    public List<Item> parseItemList(String valueToParse) {
        ArrayList<Item> itemList = new ArrayList<>();
        String[] items = valueToParse.split("##");
        for (String item : items){
            try {
                itemList.add(parseSingleItem(item));
            } catch (ItemParseException e){
                e.printStackTrace();
            }
        }
        return itemList;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {

        Pattern pattern;
        Matcher matcher;



        singleItem = singleItem.replace("##", "");
            String[] pairs = singleItem.split("[;!]");
            String[] splitPairs;
            ArrayList<String> pairList = new ArrayList<>();


            for (String pair : pairs) {
                splitPairs = pair.split("[:@^*%]");
                for (String value : splitPairs) {
                    pattern = Pattern.compile("\\s");
                    matcher = pattern.matcher(value);
                    if (matcher.find()){}
                    else {
                        pairList.add(value);
                    }
                }
            }
            if (pairList.size() != 8){
                errors++;
                throw new ItemParseException();
            }





            pattern = Pattern.compile("0");
            matcher = pattern.matcher(pairList.get(1));
            String name = matcher.replaceAll("o");




        return new Item(name.toLowerCase(), Double.valueOf(pairList.get(3)),
                    pairList.get(5).toLowerCase(), pairList.get(7).toLowerCase());

    }

    public Integer getErrors() {
        return errors;
    }
}
