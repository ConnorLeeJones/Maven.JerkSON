package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroceryReporter {
    private final String originalFileText;
    private final ItemParser parser = new ItemParser();
    List<Item> firstList;
    List<Item> groceryList;
    LinkedHashMap<String, Integer> itemOccurances = new LinkedHashMap<>();
    LinkedHashMap<String, List<Double>> priceMap = new LinkedHashMap<>();
    Integer errors;


    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
        this.groceryList = parser.parseItemList(originalFileText);
        this.errors = parser.getErrors();
        getMaps();
    }

    public void getMaps(){
        for (Item item : groceryList){
            this.itemOccurances.merge(item.getName(), 1, Integer::sum);
            if (priceMap.containsKey(item.getName())){
                priceMap.get(item.getName()).add(item.getPrice());
            } else {
                List<Double> itemList = new ArrayList<>();
                itemList.add(item.getPrice());
                priceMap.put(item.getName(), itemList);
            }
        }
    }

    public String itemToString(Item item) {
        List<Double> prices = priceMap.get(item.getName());
        Collections.sort(prices, Collections.reverseOrder());
        LinkedHashMap<Double, Integer> priceOccurances = new LinkedHashMap<>();
        for (Double price : prices) {
            priceOccurances.merge(price, 1, Integer::sum);
        }

        StringBuilder result = new StringBuilder();

        StringBuilder foodName = new StringBuilder();
        foodName.append(Character.toUpperCase(item.getName().charAt(0)));
        foodName.append(item.getName().substring(1));

        result.append(String.format("name:%8s \t\t seen: %1d times\n", foodName.toString(), itemOccurances.get(item.getName())));
        result.append("============= \t \t =============\n");

        Integer count;
        for (Map.Entry<Double, Integer> entry : priceOccurances.entrySet()) {

            result.append(String.format("Price: \t %3.2f\t\t seen: %1d times\n", entry.getKey(), entry.getValue()));
//            if (entry.getValue() == 2) {
                result.append("-------------\t\t -------------\n");
        }

        result.append("\n");



        return result.toString();
    }




    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();



        for (Item grocery : groceryList){
            StringBuilder foodName = new StringBuilder();
            foodName.append(Character.toUpperCase(grocery.getName().charAt(0)));
            foodName.append(grocery.getName().substring(1));


            if (!(result.toString().contains(foodName))) {
               result.append(itemToString(grocery));
           }


        }

        String errs = String.format("Errors         \t \t seen: %1d times", + errors);
        result.append(errs);

        return result.toString();
    }
}
