package aura_game.app;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import aura_game.app.GameManager.Game;
import aura_game.app.GameManager.LoadManager;
import aura_game.app.GameManager.RenderManager;
import aura_game.app.GameManager.UpdateManager;

/**
 * Classe principale de l'application libGDX. Gère le cycle de vie du jeu, la mise à jour et le rendu graphique.
 */
public class AuraGDX extends ApplicationAdapter {

    //SpriteBatch batch; // déclaration d'un objet SpriteBatch pour dessiner les images

    private UpdateManager updateManager;
    private RenderManager renderManager;

    /**
     * Permet de gérer les sons dans le jeu.
     */
    //private AudioManager audioManager;
    /**
     * Gestion du clavier
     */
    private int frameCount=0;
    FPSCounter fpsCounter;

    //float delay = 0; // Délai initial avant la première exécution (0 signifie tout de suite)
    //float interval = 2; // Intervalle de temps entre chaque exécution (2 secondes)

    @Override
    public void create () {

        fpsCounter = new FPSCounter();//Gestion du comptage des images par seconde (FPS)
        Game game = Game.getInstance();
        game.start();
        updateManager = game.getUpdateManager();
        LoadManager loadManager = game.getLoadManager();
        renderManager = game.getRenderManager();

        loadManager.startNewGame();
        game.setGameStarted(true);


    }

    /**Appelé de façon continue par la boucle principale de libGDX*/
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Mettre à jour et afficher les FPS
        //fpsCounter.update();
        renderManager.render();
        if(frameCount%2==0){
            updateManager.update(0.3f);
        }
        frameCount++;
    }

}

