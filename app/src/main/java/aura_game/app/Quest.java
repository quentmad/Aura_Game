package aura_game.app;

public class Quest {

    private String name;
    private String description;
    private int reward;
    private boolean isCompleted;

    public Quest(String name, String description, int reward){
        this.name = name;
        this.description = description;
        this.reward = reward;
        this.isCompleted = false;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getReward(){
        return reward;
    }

    public boolean isCompleted(){
        return isCompleted;
    }

    public void complete(){
        isCompleted = true;
    }

    public String toString(){
        return "Quest: " + name + " - " + description + " - " + reward + " - " + isCompleted;
    }
}
