package ca.ucalgary.seng300.shared.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Player {

    private final StringProperty id;
    private final StringProperty name;

    public Player(String id, String name) {

        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
    }


    public String getName() {
        return name.get();
    }

    public String getId() {
        return id.get();
    }


    public ObservableValue<String> nameProperty() {
        return name;
    }

    public ObservableValue<String> idProperty() {
        return id;
    }
}