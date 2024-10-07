import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Conversor {
    public static void main(String[] args) {
        // Crear un objeto Scanner para recibir la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        // Variables para almacenar la divisa de origen, divisa de destino y el monto
        String monedaOrigen = "";
        String monedaDestino = "";
        double monto = 0.0;

        // Pedir al usuario que seleccione la divisa de origen
        System.out.println("Seleccione la divisa de origen:");
        System.out.println("1. USD (Dólar estadounidense)");
        System.out.println("2. EUR (Euro)");
        System.out.println("3. GBP (Libra esterlina)");
        System.out.println("4. JPY (Yen japonés)");
        int opcionOrigen = scanner.nextInt();  // Leer la selección del usuario para la divisa de origen

        // Usar un switch-case para asignar la divisa de origen
        switch (opcionOrigen) {
            case 1 -> monedaOrigen = "USD";  // Asignar USD si elige la opción 1
            case 2 -> monedaOrigen = "EUR";  // Asignar EUR si elige la opción 2
            case 3 -> monedaOrigen = "GBP";  // Asignar GBP si elige la opción 3
            case 4 -> monedaOrigen = "JPY";  // Asignar JPY si elige la opción 4
            default -> {
                System.out.println("Opción no válida para la divisa de origen.");  // Mensaje si la opción no es válida
                return;  // Terminar el programa si la opción es inválida
            }
        }

        // Pedir al usuario que seleccione la divisa de destino
        System.out.println("Seleccione la divisa de destino:");
        System.out.println("1. USD (Dólar estadounidense)");
        System.out.println("2. EUR (Euro)");
        System.out.println("3. GBP (Libra esterlina)");
        System.out.println("4. JPY (Yen japonés)");
        int opcionDestino = scanner.nextInt();  // Leer la selección del usuario para la divisa de destino

        // Usar un switch-case para asignar la divisa de destino
        switch (opcionDestino) {
            case 1 -> monedaDestino = "USD";  // Asignar USD si elige la opción 1
            case 2 -> monedaDestino = "EUR";  // Asignar EUR si elige la opción 2
            case 3 -> monedaDestino = "GBP";  // Asignar GBP si elige la opción 3
            case 4 -> monedaDestino = "JPY";  // Asignar JPY si elige la opción 4
            default -> {
                System.out.println("Opción no válida para la divisa de destino.");  // Mensaje si la opción no es válida
                return;  // Terminar el programa si la opción es inválida
            }
        }

        // Pedir al usuario que ingrese el monto a convertir
        System.out.print("Ingrese el monto a convertir: ");
        monto = scanner.nextDouble();  // Leer el monto ingresado por el usuario

        // Validar que el monto sea mayor a 0 usando una condicional if
        if (monto <= 0) {
            System.out.println("El monto debe ser mayor que 0.");  // Mostrar un mensaje si el monto es inválido
            return;  // Terminar el programa si el monto no es válido
        }

        // Realizar la conversión de divisas usando la API
        try {
            // Llamar al método convertirDivisa y obtener el monto convertido
            double montoConvertido = convertirDivisa(monedaOrigen, monedaDestino, monto);

            // Mostrar el resultado de la conversión
            System.out.println(monto + " " + monedaOrigen + " equivalen a " + montoConvertido + " " + monedaDestino);

        } catch (Exception e) {
            // Manejar cualquier excepción que ocurra durante la conversión
            System.out.println("Error al realizar la conversión: " + e.getMessage());
        }

        // Cerrar el objeto scanner
        scanner.close();
    }

    // Método para realizar la conversión de divisas llamando a una API
    public static double convertirDivisa(String monedaOrigen, String monedaDestino, double monto) throws Exception {
        // Definir la clave de la API (cambiar por una clave válida de un servicio de conversión)
        String apiKey = "c663748f69d1a2850b77f903";

        // Construir la URL de la API para obtener la tasa de conversión
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + monedaOrigen + "/" + monedaDestino;

        // Crear un cliente HTTP para enviar la solicitud
        HttpClient cliente = HttpClient.newHttpClient();

        // Crear la solicitud HTTP GET
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))  // Crear la URI (dirección) de la API
                .GET()                    // Indicar que la solicitud es de tipo GET
                .build();                 // Construir la solicitud

        // Enviar la solicitud y obtener la respuesta como un string
        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());

        // Comprobar si el código de respuesta HTTP es 200 (OK)
        if (respuesta.statusCode() != 200) {
            throw new Exception("Error en la solicitud HTTP: " + respuesta.statusCode());  // Lanzar excepción si falla la solicitud
        }

        // Parsear la respuesta JSON usando Gson
        JsonObject jsonRespuesta = JsonParser.parseString(respuesta.body()).getAsJsonObject();

        // Extraer la tasa de conversión del JSON
        double tasaConversion = jsonRespuesta.get("conversion_rate").getAsDouble();

        // Calcular el monto convertido multiplicando el monto por la tasa de conversión
        return monto * tasaConversion;
    }
}
