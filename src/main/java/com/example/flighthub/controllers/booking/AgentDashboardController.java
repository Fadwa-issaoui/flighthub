package com.example.flighthub.controllers.booking;

import com.example.flighthub.databaseConnection.DatabaseConnection;
import com.example.flighthub.models.Booking;
import com.example.flighthub.models.Flight;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Line;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Image;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javafx.geometry.Insets;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AgentDashboardController {

    @FXML
    private ChoiceBox<String> departureChoiceBox;

    @FXML
    private ChoiceBox<String> destinationChoiceBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<TableRowData> upcomingFlightsTable;
    @FXML
    private TableColumn<TableRowData, String> flightNumberCol;
    @FXML
    private TableColumn<TableRowData, String> departureCol;
    @FXML
    private TableColumn<TableRowData, String> arrivalCol;
    @FXML
    private TableColumn<TableRowData, String> fullCol;
    @FXML
    private TableView<TableRowData> recentBookingsTable;
    @FXML
    private TableColumn<TableRowData, String> passengerNameCol;
    @FXML
    private TableColumn<TableRowData, String> bookingDateCol;
    @FXML
    private TableColumn<TableRowData, String> flightInfoCol;
    @FXML
    private TableColumn<TableRowData, Void> emailColumn;
    @FXML
    private TableColumn<TableRowData, Void> ticketColumn;

    @FXML
    private Label totalBookingsLabel;
    @FXML
    private Button emailButton;

    @FXML
    public void initialize() {
        loadLocations();
        initializeTables();
        loadUpcomingFlights();
        loadRecentBookings();
        loadTotalBookings();
        setupEmailButton();
        setupTicketButton();
    }


    private void loadLocations() {
        ObservableList<String> locations = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT location FROM airport");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("location"));
            }

            departureChoiceBox.setItems(locations);
            destinationChoiceBox.setItems(locations);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load locations from the database.");
        }
    }

    private void initializeTables() {
        // Upcoming flights table setup
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("column1"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("column2"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("column3"));

        // Recent bookings table setup
        passengerNameCol.setCellValueFactory(new PropertyValueFactory<>("column1"));
        bookingDateCol.setCellValueFactory(new PropertyValueFactory<>("column2"));
        flightInfoCol.setCellValueFactory(new PropertyValueFactory<>("column3"));
        recentBookingsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void loadUpcomingFlights() {
        ObservableList<TableRowData> flightRows = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT f.flightNumber, a1.location AS departure, a2.location AS arrival , f.full " +
                    "FROM flight f " +
                    "JOIN airport a1 ON f.departureAirportId = a1.airportId " +
                    "JOIN airport a2 ON f.arrivalAirportId = a2.airportId " +
                    "WHERE f.departureTime >= NOW()";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Retrieve the flight details
                String fullStatus = rs.getInt("full") == 1 ? "Yes" : "No";

                // Add the data to the flightRows list
                flightRows.add(new TableRowData(
                        rs.getString("flightNumber"),
                        rs.getString("departure"),
                        rs.getString("arrival")
                ));
            }

            // Set the items to the table
            upcomingFlightsTable.setItems(flightRows);

            // Set the cell value factory for the 'fullCol' column
            fullCol.setCellValueFactory(cellData -> {
                TableRowData row = cellData.getValue();
                // Use logic to set "full" status directly
                String fullStatus = (row.getColumn1().equals("Yes")) ? "Yes" : "No";
                return new SimpleStringProperty(fullStatus);
            });
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load upcoming flights.");
        }
    }

    private void loadRecentBookings() {
        ObservableList<TableRowData> bookingRows = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT b.bookingDate, p.firstName, p.lastName, f.flightNumber " +
                    "FROM booking b " +
                    "JOIN passenger p ON b.passengerId = p.passengerId " +
                    "JOIN flight f ON b.flightId = f.flightId " +
                    "ORDER BY b.bookingDate DESC LIMIT 5";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("firstName") + " " + rs.getString("lastName");
                bookingRows.add(new TableRowData(
                        fullName,
                        rs.getString("bookingDate"),
                        rs.getString("flightNumber")
                ));
            }
            recentBookingsTable.setItems(bookingRows);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load recent bookings.");
        }
    }

    private void loadTotalBookings() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String query = "SELECT COUNT(*) FROM booking";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalBookingsLabel.setText(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load total bookings.");
        }
    }

    @FXML
    private void handleSearchButtonAction() {
        String departureLocation = departureChoiceBox.getValue();
        String destinationLocation = destinationChoiceBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (departureLocation == null || destinationLocation == null || selectedDate == null) {
            showAlert("Input Error", "Please select all fields before searching!");
            return;
        }
        System.out.println("Searching for flights...");
        System.out.println("Departure: " + departureLocation);
        System.out.println("Destination: " + destinationLocation);
        System.out.println("Date: " + selectedDate);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FlightHub/SceneBuilder/booking_search.fxml"));
            Parent root = loader.load();

            // Retrieve the already instantiated controller
            FlightSearchController searchController = loader.getController();
            if (searchController != null) {
                System.out.println("Controller loaded successfully!");
                searchController.setFlightData(departureLocation, destinationLocation, selectedDate);
            } else {
                System.out.println("Error: FlightSearchController is NULL!");
            }

            // Switch scene
            Stage stage = (Stage) searchButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Loading Error", "Could not load the search page!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Custom data holder for rows in the tables
    public static class TableRowData {
        private String column1;
        private String column2;
        private String column3;

        public TableRowData(String column1, String column2, String column3) {
            this.column1 = column1;
            this.column2 = column2;
            this.column3 = column3;
        }

        public String getColumn1() {
            return column1;
        }

        public String getColumn2() {
            return column2;
        }

        public String getColumn3() {
            return column3;
        }
    }

    private void setupEmailButton() {
        emailColumn.setCellFactory(col -> new TableCell<TableRowData, Void>() {
            private final Button emailButton = new Button("Send Email");

            {
                emailButton.setOnAction(event -> {
                    TableRowData rowData = getTableView().getItems().get(getIndex()); // Get selected row data
                    if (rowData != null) {
                        String passengerName = rowData.getColumn1();  // Assuming column1 has passenger name
                        String flightId = rowData.getColumn3();  // Assuming column3 has flight ID

                        // Fetch flight details
                        Map<String, Object> flightDetails = fetchFlightDetails(flightId);

                        if (flightDetails != null) {
                            // Open the pop-up to enter the client's email
                            showEmailPopup(passengerName, flightDetails, -1);  // Pass passengerId as needed
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(emailButton);
                }
            }
        });
    }


    // 2. Get Passenger ID from Name
    private int fetchPassengerId(String passengerName) {
        int passengerId = -1;
        String[] nameParts = passengerName.split(" ");
        if (nameParts.length < 2) {
            System.out.println("Invalid passenger name format");
            return passengerId;
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        String query = "SELECT passengerId FROM passenger WHERE firstName = ? AND lastName = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                passengerId = rs.getInt("passengerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passengerId;
    }

    private Map<String, Object> fetchFlightDetails(String flightNumber) {
        Map<String, Object> flightDetails = new HashMap<>();
        String query = "SELECT f.flightNumber, f.departureAirportId, f.arrivalAirportId, " +
                "f.departureTime, f.arrivalTime, a1.location AS departureLocation, " +
                "a2.location AS arrivalLocation " +
                "FROM flight f " +
                "JOIN airport a1 ON f.departureAirportId = a1.airportId " +
                "JOIN airport a2 ON f.arrivalAirportId = a2.airportId " +
                "WHERE f.flightNumber = ?";

        if (flightNumber == null || flightNumber.isEmpty()) {
            System.out.println("Invalid flightNumber provided");
            return null;  // Return null if flightNumber is invalid
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, flightNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String departureLocation = rs.getString("departureLocation");
                String arrivalLocation = rs.getString("arrivalLocation");
                System.out.println("Departure Location: " + departureLocation);  // Debugging line
                System.out.println("Arrival Location: " + arrivalLocation);      // Debugging line

                flightDetails.put("flightNumber", rs.getString("flightNumber"));
                flightDetails.put("departureLocation", departureLocation);
                flightDetails.put("arrivalLocation", arrivalLocation);
                flightDetails.put("departureTime", rs.getString("departureTime"));
                flightDetails.put("arrivalTime", rs.getString("arrivalTime"));
            } else {
                System.out.println("No flight found with the given flightNumber: " + flightNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flightDetails;
    }




    // 4. Open the Pop-Up for Email Entry
    private void showEmailPopup(String passengerName, Map<String, Object> flightDetails, int passengerId) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Send Email");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Label emailLabel = new Label("Client's Email:");
        TextField emailField = new TextField();

        String flightInfoText = String.format("Flight: %s\nFrom: %s\nTo: %s\nDate: %s",
                flightDetails.get("flightNumber"),
                flightDetails.get("departureLocation"),
                flightDetails.get("arrivalLocation"),
                flightDetails.get("departureTime"));
        Label flightInfo = new Label(flightInfoText);

        Button sendButton = new Button("Send Email");
        sendButton.setOnAction(e -> {
            String clientEmail = emailField.getText();
            if (!clientEmail.isEmpty()) {
                sendEmail(clientEmail, passengerName, flightDetails);
                popupStage.close();
            } else {
                System.out.println("Email field is empty!");
            }
        });

        vbox.getChildren().addAll(emailLabel, emailField, flightInfo, sendButton);

        Scene scene = new Scene(vbox, 350, 250);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }


    // 5. Send Email Function
    private void sendEmail(String clientEmail, String passengerName, Map<String, Object> flightDetails) {
        try {
            EmailSender.sendBookingEmail(clientEmail, passengerName,
                    (String) flightDetails.get("flightNumber"),
                    (String) flightDetails.get("departureLocation"),
                    (String) flightDetails.get("arrivalLocation"),
                    flightDetails.get("departureTime").toString(), true);

            // Show confirmation alert if email is sent successfully
            showAlert("Email Sent", "The email was successfully sent to " + clientEmail);
        } catch (Exception e) {
            // Show error alert if something goes wrong
            showError("Email Sending Error", "Failed to send the email to " + clientEmail + ". Please try again.");
            e.printStackTrace();
        }
    }
    private void setupTicketButton() {
        ticketColumn.setCellFactory(col -> new TableCell<TableRowData, Void>() {
            private final Button ticketButton = new Button("Get Ticket");

            {
                ticketButton.setOnAction(event -> {
                    TableRowData rowData = getTableView().getItems().get(getIndex()); // Get row data
                    if (rowData != null) {
                        String passengerName = rowData.getColumn1();  // Assuming column1 has passenger name
                        String flightId = rowData.getColumn3();  // Assuming column3 has flight ID

                        // Fetch flight details
                        Map<String, Object> flightDetails = fetchFlightDetails(flightId);
                        if (flightDetails != null) {
                            generateTicketPDF(passengerName, flightDetails);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(ticketButton);
                }
            }
        });
    }
    private void generateTicketPDF(String passengerName, Map<String, Object> flightDetails) {
        String timestamp = java.time.LocalDateTime.now().toString().replace(":", "-");
        String fileName = passengerName.replace(" ", "_") + "_" + timestamp + "_ticket.pdf";
        String filePath = "tickets/" + fileName;

        try {
            // Ensure the directory exists
            Files.createDirectories(Paths.get("tickets"));

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setMargins(40, 40, 40, 40);
            Color primaryColor = new DeviceRgb(80, 103, 233); // #5067E9
            Color whiteColor = new DeviceRgb(255, 255, 255);
            Color darkGray = new DeviceRgb(50, 50, 50);

            // Add some basic text
            String logoPath = "C:/Users/maiss/flighthub/src/main/resources/images/logoTranspAzrek.png";  // Adjust to your logo's path
            Image logo = new Image(ImageDataFactory.create(logoPath));
            logo.setFixedPosition(20, 790);  // Position the logo at top left
            logo.scaleToFit(100, 100);  // Resize to fit
            document.add(logo);

            // Add header with airline name (larger font)
            document.add(new Paragraph("FLIGHT HUB")
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(primaryColor)
                    .setTextAlignment(TextAlignment.CENTER));

            // Add the passenger details section
            document.add(new Paragraph("Passenger: " + passengerName)
                    .setFontSize(14)
                    .setFontColor(darkGray));
            document.add(new Paragraph("Flight Number: " + flightDetails.get("flightNumber"))
                    .setFontSize(12)
                    .setFontColor(darkGray));
            document.add(new Paragraph("From: " + flightDetails.get("departureLocation"))
                    .setFontSize(12)
                    .setFontColor(darkGray));
            document.add(new Paragraph("To: " + flightDetails.get("arrivalLocation"))
                    .setFontSize(12)
                    .setFontColor(darkGray));
            document.add(new Paragraph("Departure Time: " + flightDetails.get("departureTime"))
                    .setFontSize(12)
                    .setFontColor(darkGray));
            document.add(new Paragraph("Arrival Time: " + flightDetails.get("arrivalTime"))
                    .setFontSize(12)
                    .setFontColor(darkGray));

            // Add footer with thank you message
            document.add(new Paragraph("Thank you for flying with FlightHub!")
                    .setFontSize(10)
                    .setFontColor(darkGray)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            System.out.println("Ticket PDF generated: " + filePath);

            // Open the generated PDF
            java.awt.Desktop.getDesktop().open(new java.io.File(filePath));

            // Show a confirmation message
            showAlert("Ticket Generated", "The ticket has been successfully generated and opened.");
        } catch (IOException e) {
            e.printStackTrace();
            showError("File Error", "Could not generate the ticket.");
        }
    }





}


