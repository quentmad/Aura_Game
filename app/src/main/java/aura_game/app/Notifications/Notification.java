package aura_game.app.Notifications;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Notification {
    private String message;
    private float size;
    private Texture backgroundTexture;
    private BitmapFont font;
    private float duration; // Duration in seconds, -1 for infinite
    private boolean muted;
    private float elapsedTime; // Time elapsed since the notification was created
    private NotificationManager.NotificationPlace type;

    public Notification(String message, NotificationManager.NotificationPlace type, float size, String backgroundTexturePath, BitmapFont font, float duration, boolean muted) {
        this.message = message;
        this.type = type;
        this.size = size;
        this.backgroundTexture = new Texture(backgroundTexturePath);
        this.font = font;
        this.duration = duration;
        this.muted = muted;
        this.elapsedTime = 0;
    }

    public float size() {
        return size;
    }

    public Texture backgroundTexture() {
        return backgroundTexture;
    }

    public BitmapFont font() {
        return font;
    }

    public float duration() {
        return duration;
    }

    public boolean muted() {
        return muted;
    }

    public String message() {
        return message;
    }

    public void update(float deltaTime) {
        elapsedTime += deltaTime;
    }

    public boolean isExpired() {
        return elapsedTime >= duration;
    }

    /**
     * Affiche la notification
     * @param batch SpriteBatch
     * @param decallageY décalage en y pour les notifications qui se superposent
     */
    public void render(SpriteBatch batch, int decallageY) {
        int marge = backgroundTexture.getHeight()/2;
        int decallageBorderW = 0;
        //int decallageBorderH = 0;
        font.getData().setScale(size);
        GlyphLayout layout = new GlyphLayout(font, message);
        int width = (int) layout.width + 20; // Add padding
        int height = (int) layout.height + 20; // Add padding
        if(type.equals(NotificationManager.NotificationPlace.BOTTOM_RIGHT) || type.equals(NotificationManager.NotificationPlace.TOP_RIGHT)){
            decallageBorderW = width;
        }


        // Draw the background texture with size adapted to the message
        batch.draw(backgroundTexture, type.x() - decallageBorderW, type.y() + marge * decallageY * type.directionY(), width, height);

        // Draw the message text
        font.draw(batch, message, type.x()+6 - decallageBorderW, type.y() + 20 + marge * decallageY * type.directionY()); // Add padding
        font.getData().setScale(1.0f);
    }

    public NotificationManager.NotificationPlace type() {
        return type;
    }
}