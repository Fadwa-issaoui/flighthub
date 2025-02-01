package com.example.flighthub.controllers.flight;

import com.example.flighthub.models.Flight;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class DetailsButtonTableCell<Flight, Void> extends TableCell<Flight, Void> {
    private final Button button = new Button("Details");

    public DetailsButtonTableCell() {
        button.setOnAction(event -> {
            Flight flight = (Flight) getTableRow().getItem();
            if (flight != null) {
                //TODO: will be a future feature
                System.out.println("Details button for Flight ID: "+((com.example.flighthub.models.Flight)flight).getFlightId());
            }
        });
    }
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(button);
        }
    }
}