package it.polimi.ingsw.network.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.CommunicationChannel;
import it.polimi.ingsw.network.CommunicationProtocol;
import it.polimi.ingsw.server.model.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.CommunicationChannel.SEPARATOR;
import static it.polimi.ingsw.network.CommunicationProtocol.*;

public class MatchStory {
    private final String playerName;
    private List<String> story = new ArrayList<>();
    public final int build = 0;
    public final int move = 1;
    public final int removal = 2;
    public final Map<Integer, CommunicationProtocol> actionConverter;

    public MatchStory(Player player) {
        Map<Integer, CommunicationProtocol> converter = new HashMap<>();
        converter.put(build, BUILD);
        converter.put(move, DESTINATION);
        converter.put(removal, REMOVAL);
        playerName = player.name();
        actionConverter = converter;
    }

    public void addEvent(int[] workerLocation, int action, int[] location) {
        Type type =new TypeToken<int[]>() {}.getType();
        story.add(playerName + SEPARATOR + new Gson().toJson(workerLocation, type) + SEPARATOR + CommunicationChannel.keyToString(actionConverter.get(action)) + SEPARATOR + new Gson().toJson(location, type));
    }

    public void clear() {
        story.clear();
    }
}
