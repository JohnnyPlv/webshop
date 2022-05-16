package com.greenfox.webshop.controllers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.greenfox.webshop.models.ShopItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebShopController {

    private List<ShopItem> listOfShopItems = getListOfShopItems();

    private List<ShopItem> getListOfShopItems () {
        List<ShopItem> listOfShopItem = new ArrayList<>();
        listOfShopItem.add(new ShopItem("Intel I6","High performance proccessor",5000,7,"processor"));
        listOfShopItem.add(new ShopItem("Nvidia RTX 3060","High performance graphics card",15000,0, "graphics"));
        listOfShopItem.add(new ShopItem("Corsair DDR4","Best RAM on market",2000,30,"ram"));
        listOfShopItem.add(new ShopItem("PC case White","Awesome Intel PC case",2500,2,"case"));

        return listOfShopItem;

    }

    @GetMapping("/webshop")
    public String showItems (Model model){
        model.addAttribute("items",listOfShopItems);
        return "index";
    }

    @GetMapping("/more-filters")
    public String showMoreFilters (Model model){
        model.addAttribute("items",listOfShopItems);
        model.addAttribute("makeEuro",false);
        return "extended_index";
    }

    @GetMapping("/filter-by-type/{type}")
    public String getFilterByType (Model model, @PathVariable(name="type") String type){
        List<ShopItem> listOfItemsByType = listOfShopItems.stream().filter(shopItem -> shopItem.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
        model.addAttribute("items",listOfItemsByType);
        model.addAttribute("makeEuro",false);
        return "extended_index";
    }


    @GetMapping("/only-available")
    public String getOnlyAvailable (Model model) {
        List<ShopItem> filterOutOutOfStock = listOfShopItems.stream()
                .filter(item -> item.getStock() != 0)
                .collect(Collectors.toList());
        model.addAttribute("items",filterOutOutOfStock);
        return "index";
    }

    @GetMapping("/cheapest-first")
    public String getCheapestFirst (Model model) {
        List<ShopItem> sortedFromCheapest = listOfShopItems.stream()
                        .sorted((i1, i2) -> (int) (i1.getPrice() - i2.getPrice()))
                        .collect(Collectors.toList());
        model.addAttribute("items",sortedFromCheapest);
        return "index";
    }

    @GetMapping("/contains-pc")
    public String getOnlyIntel(Model model) {
        List<ShopItem> filteredIntelItems = listOfShopItems.stream()
                        .filter(shopItem -> shopItem.getName()
                        .contains("intel") || shopItem.getDescription().contains("intel") || shopItem.getName().contains("Intel") || shopItem.getDescription().contains("Intel"))
                        .collect(Collectors.toList());

        model.addAttribute("items", filteredIntelItems);
        return "index";
    }

    @GetMapping("/average-stock")
    public String getAverageStock (Model model) {
        double filteredAverageStock = listOfShopItems.stream()
                .mapToDouble(x -> x.getStock())
                .average()
                .getAsDouble();
        model.addAttribute("average", filteredAverageStock );
        return "average";
    }

    @GetMapping("/most-expensive")
    public String getMostExpensive (Model model) {
        List<ShopItem> filteredFromTopPrice = listOfShopItems.stream()
                .sorted((i1, i2) -> (int) (i2.getPrice() - i1.getPrice()))
                .collect(Collectors.toList());
        List<ShopItem> topPriceItem = filteredFromTopPrice.subList(0,1);
        model.addAttribute("items",topPriceItem);
        return "index";


    }

    @GetMapping(path = "/search")
    public String searchItem(Model model, @RequestParam(name="keyword") String keyword) {
        //String lowerCasedKeyword = keyword.toLowerCase();
        List<ShopItem> filteredSearchKeyword = listOfShopItems.stream()
                .filter(shopItem -> shopItem.getName().toLowerCase(Locale.ROOT)
                        .contains(keyword.toLowerCase()) || shopItem.getDescription().contains(keyword.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        model.addAttribute("items",filteredSearchKeyword);
        return "index";
    }

    @GetMapping("/price-in-eur")
    public String getPriceInEuro (Model model) {
        model.addAttribute("items", listOfShopItems );
        model.addAttribute("makeEuro", true );
        return "extended_index";
    }

    @GetMapping("/price-in-original")
    public String getPriceInCzk (Model model) {
        model.addAttribute("items", listOfShopItems );
        model.addAttribute("makeEuro", false );
        return "extended_index";
    }


}
