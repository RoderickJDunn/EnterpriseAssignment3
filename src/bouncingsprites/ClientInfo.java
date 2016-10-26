package bouncingsprites;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Roderick on 2016-10-25.
 */
public class ClientInfo implements Serializable {

    private UUID id;
    private Color color;

    public ClientInfo(UUID id, Color c) {
        this.id = id;
        this.color = c;
    }

    public Color getColor() {
        return color;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("id: %s (%s)", id.toString(), color.toString());
    }


}
