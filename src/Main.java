import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus_reservation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "vasa.21/06";

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        try (Connection dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            while (true) {
                System.out.println("===== Bus Reservation System =====");
                System.out.println("1. View Available Routes");
                System.out.println("2. Book a Seat");
                System.out.println("3. Cancel Reservation");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int userChoice = inputScanner.nextInt();

                switch (userChoice) {
                    case 1 -> displayRoutes(dbConnection);
                    case 2 -> reserveSeat(dbConnection, inputScanner);
                    case 3 -> cancelReservation(dbConnection, inputScanner);
                    case 4 -> {
                        System.out.println("Thank you for using the system!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void displayRoutes(Connection dbConnection) throws SQLException {
        String routeQuery = "SELECT r.route_id, b.bus_name, r.origin, r.destination FROM routes r " +
                "JOIN buses b ON r.bus_id = b.bus_id";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(routeQuery)) {

            System.out.println("Available Routes:");
            System.out.println("Route ID | Bus Name | Origin -> Destination");
            while (resultSet.next()) {
                System.out.printf("%d | %s | %s -> %s%n",
                        resultSet.getInt("route_id"),
                        resultSet.getString("bus_name"),
                        resultSet.getString("origin"),
                        resultSet.getString("destination"));
            }
        }
    }

    private static void reserveSeat(Connection dbConnection, Scanner inputScanner) throws SQLException {
        System.out.print("Enter your name: ");
        inputScanner.nextLine();
        String customerName = inputScanner.nextLine();

        System.out.print("Enter Route ID: ");
        int routeId = inputScanner.nextInt();

        System.out.print("Enter your preferred travel time (HH:MM): ");
        String travelTime = inputScanner.next();

        System.out.print("Enter preferred seat number: ");
        int seatNumber = inputScanner.nextInt();

        String reservationQuery = "INSERT INTO reservations (customer_name, route_id, travel_time, seat_number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(reservationQuery)) {
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, routeId);
            preparedStatement.setTime(3, Time.valueOf(travelTime + ":00"));
            preparedStatement.setInt(4, seatNumber);
            preparedStatement.executeUpdate();
            System.out.println("Reservation successful!");
        } catch (SQLException exception) {
            if (exception.getErrorCode() == 1062) {
                System.out.println("This seat is already booked. Please choose another seat.");
            } else {
                throw exception;
            }
        }
    }

    private static void cancelReservation(Connection dbConnection, Scanner inputScanner) throws SQLException {
        System.out.print("Enter your name: ");
        inputScanner.nextLine();
        String customerName = inputScanner.nextLine();

        System.out.print("Enter Route ID to cancel: ");
        int routeId = inputScanner.nextInt();

        String cancelQuery = "DELETE FROM reservations WHERE customer_name = ? AND route_id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(cancelQuery)) {
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, routeId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reservation cancelled successfully!");
            } else {
                System.out.println("No reservation found with the given details.");
            }
        }
    }
}
