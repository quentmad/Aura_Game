package aura_game.app.Weather;

import aura_game.app.GameManager.AudioManager;
import aura_game.app.Tasks.RainUpdateTask;
import aura_game.app.rework.Point;
import aura_game.app.rework.Region;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static aura_game.app.GameManager.Game.screenHeight;
import static aura_game.app.GameManager.Game.screenWidth;

/**
 * La classe `RainManager` gère la pluie dans le jeu. Elle utilise un thread séparé pour mettre à jour les gouttes de pluie
 * afin de ne pas ralentir le jeu principal. Cette classe est responsable de la création, de la mise à jour et du rendu des gouttes de pluie.
 *
 * <p>Le thread de mise à jour de la pluie est démarré avec la méthode `startUpdateThread` et arrêté avec la méthode `stopUpdateThread`.
 * La méthode `update` est appelée par le thread de mise à jour pour gérer les gouttes de pluie en fonction du temps écoulé.</p>
 *
 * <p>Les gouttes de pluie sont divisées en deux catégories : celles qui sont en mouvement dans le ciel (`dropsOnSky`) et celles qui sont
 * sur le sol (`dropsOnFloor`). La classe utilise un verrou (`lock`) pour synchroniser l'accès aux données partagées entre le thread de mise à jour
 * et le thread principal.</p>
 *
 * <p>La méthode `waitForUpdate` permet au thread principal d'attendre que le thread de mise à jour ait terminé son travail, évitant ainsi les problèmes
 * de concurrence.</p>
 */
public class RainManager {
    private final Object lock = new Object();
    private Thread updateThread;
    private RainUpdateTask updateTask;


    private int timer = 0;
    private List<Drop> dropsOnSky;
    private List<Drop> dropsOnFloor;
    private int nbDropsMax;
    private boolean active;
    private int nextRainStart;//Date de la prochaine pluie
    private int nextRainEnd;//Date de fin de la pluie
    private float updateCounter = 0;


    public RainManager() {
        this.dropsOnSky = new CopyOnWriteArrayList<>();
        this.dropsOnFloor = new CopyOnWriteArrayList<>();
        setInfos();
    }

    public void startUpdateThread(Point playerPos) {
        updateTask = new RainUpdateTask(this, playerPos);
        updateThread = new Thread(updateTask);
        updateThread.start();
    }

    public void stopUpdateThread() {
        if (updateTask != null) {
            updateTask.stop();
        }
        if (updateThread != null) {
            try {
                updateThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Appelé par le thread de mise à jour de la pluie une fois par frame/tick.
     * Si la pluie est active, met à jour les gouttes de pluie en mouvement.
     * Crée de nouvelles gouttes de pluie si nécessaire.
     * Quand la pluie est terminée, désactive la pluie et planifie la prochaine pluie.
     * Si la pluie n'est pas active, vérifie si la date actuelle est supérieure à la date de début de la prochaine pluie.
     * Si c'est le cas, active la pluie.
     * @param deltaTime
     */
    public void update(float deltaTime, Point playerPos) {
        synchronized (lock) {
            timer++;
            updateCounter += deltaTime;
            //System.out.println("update "+ timer);
            if (updateCounter > 0.2) {
                updateCounter = 0;
                if (active || dropsOnSky.size() > 0 || dropsOnFloor.size() > 0) {
                    updateDropsOnSky(deltaTime, playerPos);
                    updateDropsOnFloor(deltaTime);

                } else if (active && timer > nextRainEnd && dropsOnSky.size() == 0 && dropsOnFloor.size() == 0) {
                    AudioManager.getInstance().stopSound("rain", "");
                    setInfos();
                }
            } else {
                if (timer > nextRainStart) {
                    active = true;
                }
            }
        }
    }


    /**
     * Met à jour les gouttes de pluie en mouvement.
     * @param deltaTime
     * @param playerPos
     */
    public void updateDropsOnSky(float deltaTime, Point playerPos) {
        ConcurrentLinkedQueue<Drop> toRemove = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Drop> goOnFloor = new ConcurrentLinkedQueue<>();
        if (nextRainStart != -1) {
            AudioManager.getInstance().playSound("rain", "", 0.2f);
            nextRainStart = -1;
        }
        if (timer < nextRainEnd) {
            int nbDrops = (int) (Math.random() * 200) + 50;
            if (dropsOnSky.size() + nbDrops < nbDropsMax && active) {
                System.out.println("create drop: " +nbDrops + " drops, now there are " + (dropsOnSky.size() + nbDrops) + " drops /"+ nbDropsMax);
                for (int i = 0; i < nbDrops; i++) {
                    createDrops(playerPos);
                }
            }
            dropsOnSky.parallelStream().forEach(drop -> {
                drop.update(deltaTime, timer);
                if (drop.toRemove()) {
                    toRemove.add(drop);
                } else if (!drop.isMoving()) {
                    goOnFloor.add(drop);
                }
            });
            //pas d'explosions possible, on les retire
            dropsOnSky.removeAll(toRemove);
            //explosions sur le sol
            dropsOnSky.removeAll(goOnFloor);
            dropsOnFloor.addAll(goOnFloor);


        }
    }

    /**
     * Met à jour les gouttes de pluie sur le sol.
     * @param deltaTime
     */
    public void updateDropsOnFloor(float deltaTime) {
        ConcurrentLinkedQueue<Drop> toRemove = new ConcurrentLinkedQueue<>();
        dropsOnFloor.parallelStream().forEach(drop -> {
            drop.update(deltaTime, timer);
            if (drop.toRemove()) {
                toRemove.add(drop);
            }
        });
        dropsOnFloor.removeAll(toRemove);
    }


    /**
     * Crée une nouvelle instance de goutte de pluie en mouvement, et l'ajoute à la liste des gouttes actives.
     */
    public void createDrops(Point playerPos) {
        //Position random sur l'ecran autour du joueur
        //Point a + - screenWidth du joueur et + - screenHeight du joueur
        Point pos = new Point((int)(playerPos.x() + (Math.random() * screenWidth*2+60) - screenWidth - 30), (int)(playerPos.y() + (Math.random() * screenHeight*2+50) - screenHeight - 25));
        int size = (int)(Math.random() * 3);
        dropsOnSky.add(new Drop(size, pos, timer, getTextureWidth(size), getTextureHeight(size)));
    }

    public int getTextureWidth(int size) {
        return switch (size) {
            case 0 -> 4;
            default -> 8;
        };
    }

    public int getTextureHeight(int size) {
        return switch (size) {
            case 0 -> 8;
            case 1 -> 16;
            default -> 12;
        };
    }

    public void setInfos(){
        active = false;
        //Date de la prochaine pluie
        nextRainStart = (int) (timer + (long) (Math.random() * 20) + 50);
        nbDropsMax = (int) (Math.random() * 400) + 600;
        System.out.println("nombre de goutes: "+ nbDropsMax);
        //Date de fin de la prochaine pluie
        nextRainEnd = (int) (nextRainStart + (long) (Math.random() * 2000) + 500);

    }

    /**
     * Attend que la mise à jour de la pluie soit terminée.
     * Utilisé pour éviter les problèmes de concurrence.
     * Fonctionnement : le thread principal attend que le thread de mise à jour de la pluie soit terminé.
     * @throws InterruptedException
     */
    public void waitForUpdate() throws InterruptedException {
        synchronized (lock) {
            lock.wait();
        }
    }

    public Object lock() {
        return lock;
    }


    public void renderDropsOnSky(SpriteBatch batch, Region region) {
        for (Drop drop : dropsOnSky) {
            drop.render(batch, region);
        }
    }

    public void renderDropsOnFloor(SpriteBatch batch, Region region) {
        for (Drop drop : dropsOnFloor) {
            drop.render(batch, region);
        }
    }

}
