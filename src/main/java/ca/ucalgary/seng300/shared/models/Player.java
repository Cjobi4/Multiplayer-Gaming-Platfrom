package ca.ucalgary.seng300.shared.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Player {

    private final StringProperty name;

    public Player( String name) {

        this.name = new SimpleStringProperty(name);
    }


    public String getName() {
        return name.get();
    }



    public ObservableValue<String> nameProperty() {
        return name;
    }

}