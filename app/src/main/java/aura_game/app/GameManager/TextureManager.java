package aura_game.app.GameManager;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**Cette classe permet de gérer et accéder aux textures du jeu, pour éviter de les charger plusieurs fois */
public class TextureManager {
    private static TextureManager instance;
    private Map<String, Texture> textureMap;

    private TextureManager() {
        textureMap = new HashMap<>();
    }

    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }

    /**
     * Récupère une texture à partir de son chemin. Si la texture n'existe pas, elle est chargée.
     * @param path Chemin de la texture.
     * @return
     */
    public Texture getTexture(String path) {
        if (!textureMap.containsKey(path)) {
            System.out.println("Loading texture: " + path);
            textureMap.put(path, new Texture(path)); //-> pas charger ici, si provient d'un autre thread
        }
        return textureMap.get(path);
    }

    /**
     * Ajoute une texture à la liste des textures gérées par le TextureManager.
     * @param path Chemin de la texture.
     */
    public void addTexture(String path) {
        textureMap.put(path, new Texture(path));
    }

    public void dispose() {
        for (Texture texture : textureMap.values()) {
            texture.dispose();
        }
        textureMap.clear();
    }

    public void loadUsesTextures(){
        addTexture("src/main/resources/UI/bar_font.png");
        addTexture("src/main/resources/UI/bar_red.png");
        addTexture("src/main/resources/Particles/drop_0.png");
        addTexture("src/main/resources/Particles/drop_1.png");
        addTexture("src/main/resources/Particles/drop_2.png");
        addTexture("src/main/resources/Particles/drop_floor_1.png");
        addTexture("src/main/resources/Particles/drop_floor_2.png");
        addTexture("src/main/resources/Particles/drop_floor_3.png");
    }

    /**
     * Libère la mémoire de toutes les textures chargées.
     */
    public void disposeAllTextures() {
        for (Texture texture : textureMap.values()) {
            texture.dispose();
        }
        textureMap.clear();
    }
}