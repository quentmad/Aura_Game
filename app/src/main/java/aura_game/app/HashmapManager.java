package aura_game.app;
import java.util.*;

/**Hashmap manager générique permettant de gérer l'ajout et la récupération dans une hashmap*/
public class HashmapManager<T, R> {

    private HashMap<T, R> dataMap;

    public HashmapManager() {
        dataMap = new HashMap<>();
    }

    public void addData(T key, R data) {
        dataMap.put(key, data);
    }

    public R getData(T key) {
        return dataMap.getOrDefault(key, null);
    }
}
