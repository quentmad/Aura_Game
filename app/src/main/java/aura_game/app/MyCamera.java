package aura_game.app;

import aura_game.app.rework.AbstractEntity;
import aura_game.app.rework.AbstractObject;
import aura_game.app.rework.Point;

import static aura_game.app.GameManager.Game.*;

public class MyCamera {
    private Point pos;
    /**
     * Limites de la caméra, selon la taille de la carte
     */
    private int cameraMaxX;
    /**
     * Limites de la caméra, selon la taille de la carte
     */
    private int cameraMaxY;
    /**
     * Seuil de la caméra en X pour déclencher le défilement
     */
    private int CAMERA_THRESHOLD_X;
    /**
     * Seuil de la caméra en Y pour déclencher le défilement
     */
    private int CAMERA_THRESHOLD_Y;




    public MyCamera(int thresholdX, int thresholdY) {
        this.pos = new Point(0, 0);
        this.cameraMaxX = actualRegionWidth - screenHeight;
        this.cameraMaxY = actualRegionHeight- screenWidth;
        this.CAMERA_THRESHOLD_X = thresholdX;
        this.CAMERA_THRESHOLD_Y = thresholdY;
    }  
    
    public void updatePosition(int dx, int dy) {
        int cameraMaxX = actualRegionWidth - screenWidth;
        int cameraMaxY = actualRegionHeight - screenHeight;

        if (pos.x() + dx < 0) {
            dx = - pos.x();
        } else if (pos.x() + dx > cameraMaxX) {
            dx = cameraMaxX - pos.x();
        }

        if (pos.y() + dy < 0) {
            dy = - pos.y();
        } else if (pos.y() + dy > cameraMaxY) {
            dy = cameraMaxY - pos.y();
        }
        pos.addToX(dx);
        pos.addToY(dy);
    }
    
    public Point position() {
        return pos;
    }
    /**
     * Appelé par le(s) Player lorsqu'il(s) bouge(nt)
     * Met à jour la position de la caméra pour que la carte "coulisse" si le joueur est au bord de l'écran
     *
     * @param entity
     */
    public void calculAndUpdateCameraPosition(AbstractEntity entity) {
        int[] positionVariation = calculatePositionVariation(entity);
        int dx = positionVariation[0];
        int dy = positionVariation[1];
        if (dx != 0 || dy != 0) {
            updatePosition(dx, dy);
        }
    }

    /**
     * Calcule la variation de position de l'entité en fonction de sa position par rapport à l'écran.
     * Lorsqu'on est sur le bord et qu'on avance l'ecran se décale
     */
        public int[] calculatePositionVariation(AbstractEntity entity) {
        int dx = 0;
        //Bord droit
        if ((entity.getPosOnScreenX(pos.x()) > (screenWidth/2) + CAMERA_THRESHOLD_X)) {
            dx = entity.getPosOnScreenX(pos.x()) - (screenWidth/2) - CAMERA_THRESHOLD_X;
            //Bord gauche
        } else if (entity.getPosOnScreenX(pos.x()) < CAMERA_THRESHOLD_X) {
            dx = entity.getPosOnScreenX(pos.x()) - CAMERA_THRESHOLD_X;
        }

        int dy = 0;
        //Bord haut
        if (entity.getPosOnScreenY(pos.y()) > (screenHeight/2) + CAMERA_THRESHOLD_Y) {
            dy = entity.getPosOnScreenY(pos.y()) - (screenHeight/2) - CAMERA_THRESHOLD_Y;
            //Bord bas
        } else if (entity.getPosOnScreenY(pos.y()) < CAMERA_THRESHOLD_Y) {
            dy = entity.getPosOnScreenY(pos.y()) - CAMERA_THRESHOLD_Y;
        }
        // On met à jour la position de la caméra en ajoutant la variation de position de l'entité,
        // tout en s'assurant que la caméra ne sort pas des limites de la carte
        if (pos.x() + dx < 0) {
            dx = -pos.x();
        } else if (pos.x() + dx > cameraMaxX) {
            dx = cameraMaxX - pos.x();
        }

        if (pos.y() + dy < 0) {
            dy = -pos.y();
        } else if (pos.y() + dy > cameraMaxY) {
            dy = cameraMaxY - pos.y();
        }

        return new int[]{dx, dy};
    }
}
