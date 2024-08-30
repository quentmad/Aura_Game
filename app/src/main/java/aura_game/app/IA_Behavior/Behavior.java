package aura_game.app.IA_Behavior;

import aura_game.app.rework.IAActorEntity;

/**Interface définissant les méthodes des différentes classes de logique d'action des comportements des IA */
public interface Behavior {
    void act(IAActorEntity entity, float deltaTime);
}

//TODO:
//Brouter...