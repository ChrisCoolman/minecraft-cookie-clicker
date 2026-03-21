package com.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveData {
    public String cookies = "0";
    public String cookiesPerSecond = "0";
    public String handMadeCookies = "0";
    public String maxCookies = "0";

    public Map<String, Integer> buildings = new HashMap<>();

    public List<String> purchasedUpgrades = new ArrayList<>();
    public List<String> purchasedKittens = new ArrayList<>();
    public List<String> purchasedClickUpgrades = new ArrayList<>();
}
