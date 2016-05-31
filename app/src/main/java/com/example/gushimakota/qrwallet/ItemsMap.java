package com.example.gushimakota.qrwallet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gushimakota on 16/05/31.
 */
// アイテム用のMapの入ったクラス
public class ItemsMap {

    private Map<String,Integer> map;

    public ItemsMap(){
        map = new HashMap<String, Integer>();
        map.put("DebugTest", 100);
        map.put("DebugTest2", 200);
    }

    public Map<String,Integer> getMap(){
        return  map;
    }


    public int checkThePrice(String item){
        for ( String key : map.keySet() ) {
            if(item == key){
                return map.get(key);
            }
        }
        return -1;
    }

}
