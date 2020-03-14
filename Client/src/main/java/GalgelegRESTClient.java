
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.Scanner;

public class GalgelegRESTClient {
    static String domain = "http://localhost:4000";
    static String clientID;

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();

        run(client);
    }


    public static void run(Client client){

        login(client);

        createGame(client);

        System.out.println("Velkommen til Galgeleg!");

        ui(client);

        do {
            guessOnLetter(client);

            ui(client);

        } while (!isGameFinished(client));

    }

    public static void login(Client client){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Login:\nBrugernavn:");
        String userID = scanner.next();
        System.out.println("Adgangskode:");
        String password = scanner.next();

        String path = "/login";
        String url = domain + path + "?username=" + userID + "&password=" + password;

        Response response = client.target(url).request().get();
        String result = response.readEntity(String.class);

        if (result.equals("login successful")){
            clientID = userID;
            System.out.println(result);
        } else {
            System.out.println(result);
            login(client);
        }
    }

    public static void createGame(Client client){
        String path = "/play/" + clientID;
        String url = domain + path;

        Response response = client.target(url).request().get();
        String result = response.readEntity(String.class);

        System.out.println(result);
    }

    public static void guessOnLetter(Client client) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Indtast et bogstav");
        String letter = scanner.next();

        String path = "/play/"+ clientID +"/guess";
        String url = domain + path + "?letter=" + letter;

        Response response = client.target(url).request().get();
        String result = response.readEntity(String.class);

        System.out.println(result);
    }

    public static boolean isGameFinished(Client client) {
        String path = "/play/"+ clientID +"/isFinished";
        String url = domain + path;

        Response response = client.target(url).request().get();
        String result = response.readEntity(String.class);

        if (result.equals("false")){
            return false;

        } else {
            if (!result.equals("true")){
                System.out.println(result);
            }
            return true;
        }
    }

    public static void ui(Client client) {

        String lives = getLivesLeft(client);
        String visibleWord = getVisibleWord(client);
        String usedLetters = getUsedLetters(client);

        String string = "";
        string += "\n\nGæt ordet: " + visibleWord;
        string += "\t\tLiv tilbage: " + lives;
        string += "\t\tBrugte bogstaver" + usedLetters;

        System.out.println(string);
    }

    public static String getLivesLeft(Client client){
        String path = "/play/"+ clientID +"/lives";
        String url = domain + path;

        Response response = client.target(url).request().get();
        String result = response.readEntity(String.class);

        return result;
    }

    public static String getVisibleWord(Client client){
        String path = "/play/"+ clientID +"/visibleWord";
        String url = domain + path;

        Response response = client.target(url).request().get();
        String result = response.readEntity(String.class);

        return result;
    }

    public static String getUsedLetters(Client client){

        String path = "/play/"+ clientID +"/usedLetters";
        String url = domain + path;

        Response response = client.target(url).request().get();
        String result = response.readEntity(String.class);

        return result;
    }

}
