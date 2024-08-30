package aura_game.app.rework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ObjectsOnMap<T extends AbstractObject> {
    private List<T> objets;
    private boolean needSort;

    public ObjectsOnMap() {
        this.objets = new ArrayList<>();
        this.needSort = false;
    }

    /**
     * Ajoute le basicObject (loots, items, entités) a la liste de la map, trié selon Z (s'affichant sur la map, dans l'ordre de Z)
     * Ordre Décroissant
     * @param newObj objet a rajouter à la liste
     **/
    public void add(T newObj) {
        int index = 0;
        for (T o : objets) {
            if (newObj.z() > o.z()) {
                break;
            }
            index++;
        }
        objets.add(index, newObj);

    }
    /**
     * Tri dans l'ordre décroissant
     */
    public void sort() {
        objets.sort(Comparator.comparingInt(AbstractObject::z).reversed());
    }

    public void remove(T obj) {
        objets.remove(obj);
    }

    public List<T> objects() {
        return objets;
    }

    public boolean needSort() {
        return needSort;
    }

    public void setNeedSort(boolean needSort) {
        this.needSort = needSort;
    }

    /**
     * Vérifie si la liste d'objets est triée selon Z
     * Seulement pour les tests de vérification
     * @return
     */
    public boolean isSorted(){
        for(int i = 0; i < objets.size()-1; i++){
            System.out.println(objets.get(i).z() + " " + objets.get(i+1).z());
            if(objets.get(i).z() < objets.get(i+1).z()){
                return false;
            }
        }
        return true;
    }
}
