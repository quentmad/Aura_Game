package aura_game.app;

import java.util.ArrayList;
import java.util.List;

import aura_game.app.Objects.CollidableObject;

public class ListCollidableObject {

    private List<CollidableObject> list = new ArrayList<CollidableObject>();

    public void remove(CollidableObject o){
        list.remove(o);
    }

    public void add(CollidableObject o){
        if(o==null){
            System.out.println("add dans ListCollidableObject est null");
        }else{
            list.add(o);
        }
    }

    public void addAll(ListCollidableObject lo){
        if(lo !=null){
            list.addAll(lo.list);
        }
    }

    public int size(){
        return list.size();
    }

    public CollidableObject get(int i){
        return list.get(i);
    }

    public List<CollidableObject> getList() {
        return list;
    }

}
