package com.metanit;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        HashMap<Integer, String> passportsAndNames = new HashMap<>();
        passportsAndNames.put(212133, "Лидия Аркадьевна Бубликова");
        passportsAndNames.put(162348, "Иван Михайлович Серебряков");
        passportsAndNames.put(8082771, "Дональд Джон Трамп");
        HashMapIterator it = new HashMapIterator(passportsAndNames);
        try {
            while (it.hasNext()) {
                it.next();
            }
            it.remove();

        }
        catch(IllegalStateException ex){
            System.out.println(ex.getMessage());
        }
        catch(NoSuchElementException ex){
            System.out.println(ex.getMessage());
        }
    }
}
