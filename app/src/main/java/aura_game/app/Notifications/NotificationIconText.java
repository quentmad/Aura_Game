package aura_game.app.Notifications;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NotificationIconText extends Notification {

    private final Texture lootTexture;
    private final int nbLoot;

    public NotificationIconText(String message, Texture Loot, BitmapFont font, float duration, boolean muted) {
        super(message, NotificationManager.NotificationPlace.BOTTOM_LEFT, 1, "src/main/resources/notif_icon-txt48long.png", font, duration, muted);
        this.lootTexture = Loot;
        this.nbLoot = -1;
    }

    public NotificationIconText(String message, Texture Loot, int quantity, BitmapFont font, float duration, boolean muted) {
        super(message, NotificationManager.NotificationPlace.BOTTOM_LEFT, 1, "src/main/resources/notif_icon-txt48long.png", font, duration, muted);
        this.lootTexture = Loot;
        this.nbLoot = quantity;
    }


    public Texture lootTexture() {
        return lootTexture;
    }

    @Override
    public void render(SpriteBatch batch, int decallageY) {
        int sizeCadre = 48;
        int marge = backgroundTexture().getHeight() +2;

        // Draw the background texture with size adapted to the message
        batch.draw(backgroundTexture(), type().x(), type().y() + marge * decallageY * type().directionY(), 0,0,sizeCadre,sizeCadre);//Icon
        batch.draw(backgroundTexture(), type().x() + sizeCadre, type().y() + marge * decallageY * type().directionY(), Math.max(backgroundTexture().getWidth() - message().length()*11, 48),0,Math.min(message().length()*11, 144),48);//Partie droite selon la longueur du message


        // Draw the message text
        font().draw(batch, message(), type().x() + sizeCadre + 6 , type().y() + 30 + marge * decallageY * type().directionY()); // Add padding
        //draw the loot
        batch.draw(lootTexture(), type().x() + (sizeCadre - lootTexture.getWidth())/2, type().y() + (sizeCadre - lootTexture.getHeight()) / 2 + marge * decallageY * type().directionY());
        if(nbLoot != -1)
            font().draw(batch, "x"+ nbLoot + "", type().x() + sizeCadre - 22, type().y() + 15 + marge * decallageY * type().directionY()); // Add padding

        font().getData().setScale(1.0f);

    }

}
