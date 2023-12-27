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
    //Music music = assetManager.get("audio/music.mp3", Music.class);

    private AudioManager(){
        assetManager = new AssetManager();

        AssetManager assetManager = new AssetManager();
        assetManager.load("src/main/resources/sound/moveSelectedItem.mp3", Sound.class);
        assetManager.load("src/main/resources/sound/putWood.ogg", Sound.class);
        assetManager.load("src/main/resources/sound/woodbreak.ogg", Sound.class);
        assetManager.load("src/main/resources/sound/pickedUp.ogg", Sound.class);
        assetManager.load("src/main/resources/sound/hitEntity.ogg", Sound.class);
        /*assetManager.load("audio/sound.wav", Sound.class);
        assetManager.load("audio/music.mp3", Music.class);//Pour des sons plus long
        */
        assetManager.finishLoading();
        /*
        Sound sound = assetManager.get("audio/sound.wav", Sound.class);
        Music music = assetManager.get("audio/music.mp3", Music.class);*/
        //TODO: sound1.dispose(); // Pour libérer les ressources du son 1
        soundMoveSelectedItem = assetManager.get("src/main/resources/sound/moveSelectedItem.mp3", Sound.class);
        soundBreakWood = assetManager.get("src/main/resources/sound/woodbreak.ogg", Sound.class);
        soundPutWood = assetManager.get("src/main/resources/sound/putWood.ogg", Sound.class);
        soundPickedUp = assetManager.get("src/main/resources/sound/pickedUp.ogg", Sound.class);
        soundHurtEntity = assetManager.get("src/main/resources/sound/hitEntity.ogg", Sound.class);

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
        sound.play(0);//! remettre volume !!!!!!!!! TODO
    }

    /**MEttre action et name dans les type si pas fait (item, entity...) */
    public void playSound(String action,String name, float volume ){
        switch (action +"_"+ name){
            case "wood_hurt":
                soundBreakWood.play(0);//! remettre volume !!!!!!!!!
                break;
            case"entity_hurt":
                soundHurtEntity.play(0);//! remettre volume !!!!!!!!!
        }

    }
    //Ne rien mettre dans les ItemType... On recherche directement par action et type le son


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