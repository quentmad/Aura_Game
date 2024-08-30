package aura_game.app.Notifications;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static aura_game.app.GameManager.Game.screenHeight;
import static aura_game.app.GameManager.Game.screenWidth;

public class NotificationManager {

    private static NotificationManager instance;
    private List<Notification> notifications;


    private NotificationManager() {
        this.notifications = new ArrayList<>();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void update(float deltaTime) {
        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification notification = iterator.next();
            notification.update(deltaTime);
            if (notification.isExpired()) {
                iterator.remove();
            }
        }
    }

    public void render(SpriteBatch batch) {
        int current = 0;
        int iTopLeft = 0;
        int iTopRight = 0;
        int iBottomLeft = 0;
        int iBottomRight = 0;
        for (Notification notification : notifications) {
            if(notification.type() == NotificationPlace.TOP_LEFT) {
                current = iTopLeft++;;
            } else if(notification.type() == NotificationPlace.TOP_RIGHT) {
                current = iTopRight++;
            } else if(notification.type() == NotificationPlace.BOTTOM_LEFT) {
                current = iBottomLeft++;
            } else if(notification.type() == NotificationPlace.BOTTOM_RIGHT) {
                current = iBottomRight++;
            }
            notification.render(batch, current);
        }
    }



    public enum NotificationPlace {
        TOP_LEFT(15, screenHeight - 30, -1),
        TOP_RIGHT(screenWidth - 15, screenHeight - 30, -1),
        BOTTOM_LEFT(15, 30, 1),
        BOTTOM_RIGHT(screenWidth - 15, 30, 1);

        private final int x;
        private final int y;
        private final int directionY;

        NotificationPlace(int x, int y, int directionY) {
            this.x = x;
            this.y = y;
            this.directionY = directionY;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        /**
         * @return la direction Y des notifications
         * 1 pour monter, -1 pour descendre
         */
        public int directionY() {
            return directionY;
        }
    }

}
