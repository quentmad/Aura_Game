package aura_game.app.GameManager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Singleton garantissant qu'il n'y ait qu'une seule instance de MyInputProc dans l'application
 */
public class AudioManager {

    private static AudioManager instance;
    /**
     * Gère l'audio sans méthode parfaite pour la spatialisation
     * Permet de stocker (load) les audios puis les lires, stop...
     */
    AssetManager assetManager;
    //------Sounds
    Sound soundMoveSelectedItem;
    Sound soundBreakWood;//TODO: tempo ? les stocker dans itemType ceux qu'on peut
    Sound soundPutWood;
    Sound soundPickedUp;
    Sound soundHurtEntity;
    Sound rain;
    //Music music = assetManager.get("audio/music.mp3", Music.class);

    private AudioManager(){
        assetManager = new AssetManager();

        AssetManager assetManager = new AssetManager();
        assetManager.load("src/main/resources/sound/moveSelectedItem.mp3", Sound.class);
        assetManager.load("src/main/resources/sound/putWood.ogg", Sound.class);
        assetManager.load("src/main/resources/sound/woodbreak.ogg", Sound.class);
        assetManager.load("src/main/resources/sound/pickedUp.ogg", Sound.class);
        assetManager.load("src/main/resources/sound/hitEntity.ogg", Sound.class);
        assetManager.load("src/main/resources/sound/rain.ogg", Sound.class);
        assetManager.finishLoading();
        //TODO: sound1.dispose(); // Pour libérer les ressources du son 1
        soundMoveSelectedItem = assetManager.get("src/main/resources/sound/moveSelectedItem.mp3", Sound.class);
        soundBreakWood = assetManager.get("src/main/resources/sound/woodbreak.ogg", Sound.class);
        soundPutWood = assetManager.get("src/main/resources/sound/putWood.ogg", Sound.class);
        soundPickedUp = assetManager.get("src/main/resources/sound/pickedUp.ogg", Sound.class);
        soundHurtEntity = assetManager.get("src/main/resources/sound/hitEntity.ogg", Sound.class);
        rain = assetManager.get("src/main/resources/sound/rain.ogg", Sound.class);

    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }


    /**
     * Joue le son en parametre
     * @param sound son a joué
     * @param volume du sonn joué entre 0 et 1
     */
    public void playSound(Sound sound,float volume ){
        sound.play(volume);//! remettre volume !!!!!!!!! TODO
    }

    /**MEttre action et name dans les type si pas fait (item, entity...) */
    public void playSound(String action,String name, float volume ){
        switch (action +"_"+ name){
            case "wood_hurt":
                soundBreakWood.play(volume);//! remettre volume !!!!!!!!!
                break;
            case"entity_hurt":
                soundHurtEntity.play(volume);//! remettre volume !!!!!!!!!
            case "rain_":
                rain.play(volume);//! remettre volume !!!!!!!!!
                break;
        }

    }
    //Ne rien mettre dans les ItemType... On recherche directement par action et type le son

    public void stopSound(String action,String name){
        switch (action +"_"+ name){
            case "wood_hurt":
                soundBreakWood.stop();
                break;
            case"entity_hurt":
                soundHurtEntity.stop();
            case "rain_":
                rain.stop();
                break;
        }
    }

//TODO: stop, loop,pan?, pause

    public Sound getSoundMoveSelectedItem(){//TODO je vais pas faire 300 get: getSoundPutItem: voit direct en fonction de l'item son bruit
        return soundMoveSelectedItem;
    }

    public Sound getSoundPutWood(){//TODO je vais pas faire 300 get
        return soundPutWood;
    }
//TODO DISPOSE

    public Sound getSoundPickedUp() {
        return soundPickedUp;
    }
}