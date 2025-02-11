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

        // Style for details button
        button.setStyle("-fx-background-color: #5067e9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 3;");

        // Style when the button is hovered (optional)
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #3a4ea0; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 3;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #5067e9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 3;"));

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