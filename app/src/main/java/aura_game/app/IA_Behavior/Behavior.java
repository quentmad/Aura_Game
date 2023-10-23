package aura_game.app.IA_Behavior;

import aura_game.app.*;
import aura_game.app.Objects.IAEntity;

/**Interface définissant les méthodes des différentes classes de logique d'action des comportements des IA */
public interface Behavior {
    void act(IAEntity entity, float deltaTime);
}

//TODO:
//Brouter...