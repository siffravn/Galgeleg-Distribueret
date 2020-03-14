import brugerautorisation.transport.rmi.Brugeradmin;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.rmi.Naming;

public class GalgelegRESTServer {

    public static Javalin app;
    static IServerRMIController controller;


    public static void main(String[] args) throws Exception {
        app = Javalin.create().start(4000);
        controller = (IServerRMIController) Naming.lookup("rmi://localhost/Controller");


        setupResources();
    }

    public static void setupResources() throws Exception{

        app.get("", ctx -> ctx.redirect("/login"));
        app.get("/login", ctx -> login(ctx));
        app.get("/play", ctx -> hangman(ctx));
        app.get("/play/:id", ctx -> createGame(ctx, controller));
        app.get("/play/:id/terminate", ctx -> terminateGame(ctx, controller));
        app.get("/play/:id/lives", ctx -> getLives(ctx, controller));
        app.get("/play/:id/visibleWord", ctx -> getVisibleWord(ctx, controller));
        app.get("/play/:id/usedLetters", ctx -> getUsedLetters(ctx, controller));
        app.get("/play/:id/guess", ctx -> guessOnLetter(ctx, controller));
        app.get("/play/:id/isFinished", ctx -> isGameFinished(ctx, controller));
    }

    private static void createGame(Context ctx, IServerRMIController controller) throws Exception {
        String clientID = ctx.pathParam("id");

        try {
            controller.newGame(clientID);
            ctx.html("New game created");

        } catch (Exception e){
            ctx.html("Couldn't create new game");
        }

    }

    static void terminateGame(Context ctx, IServerRMIController controller) {
        String clientID = ctx.pathParam("id");
        try {
            controller.terminateGame(clientID);
            ctx.html("Terminated game " + clientID);

        } catch (Exception e){
            ctx.html("Couldn't terminate game");
        }
    }


    // Acting as rmi-client to Javabog.dk
    //http://localhost:4000/login?username=s173998&password=kk29
    private static void login(Context ctx) throws Exception{
        String userID = ctx.queryParam("username");
        String password = ctx.queryParam("password");
        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

        if (userID == null || password == null){
            ctx.html("please login");

        }else {
            try {
                brugeradmin.hentBruger(userID, password);
                ctx.html("login successful");
//            ctx.redirect("/playGame");

            }catch (Exception e){
                ctx.html("access denied");
            }
        }

    }

    public static void getLives(Context ctx, IServerRMIController controller) throws Exception {
        String clientID = ctx.pathParam("id");

        try {
            IGalgeLogik galgeLogik = controller.getGame(clientID);
            int lives = 7-galgeLogik.getAntalForkerteBogstaver();
            ctx.html("" + lives);

        } catch (Exception e){
            ctx.html("Could not access resource \"Lives\"");
        }
    }

    public static void getVisibleWord(Context ctx, IServerRMIController controller) throws Exception {
        String clientID = ctx.pathParam("id");

        try {
            IGalgeLogik galgeLogik = controller.getGame(clientID);
            String visibleWord = galgeLogik.getSynligtOrd();
            ctx.html(visibleWord);

        } catch (Exception e){
            ctx.html("Could not access resource \"Visible Word\"");
        }
    }

    public static void getUsedLetters(Context ctx, IServerRMIController controller) throws Exception{
        String clientID = ctx.pathParam("id");

        try {
            IGalgeLogik galgeLogik = controller.getGame(clientID);
            String usedLetters = galgeLogik.getBrugteBogstaver().toString();
            ctx.html(usedLetters);

        } catch (Exception e){
            ctx.html("Could not access resource \"Used Letters\"");
        }
    }

    public static void guessOnLetter (Context ctx, IServerRMIController controller) throws Exception {
        String clientID = ctx.pathParam("id");

        try {
            IGalgeLogik galgeLogik = controller.getGame(clientID);
            String letter = ctx.queryParam("letter");

            galgeLogik.guessLetter(letter);

            if (galgeLogik.erSidsteBogstavKorrekt()){
                ctx.html("correct letter");

            } else {
                ctx.html("incorrect letter");
            }

        } catch (Exception e){
            ctx.html("Could not submit query");
        }
    }

    public static void isGameFinished(Context ctx, IServerRMIController controller) throws Exception{
        String clientID = ctx.pathParam("id");

        try {
            IGalgeLogik galgeLogik = controller.getGame(clientID);
            boolean isFinished = galgeLogik.erSpilletSlut();
            ctx.html("" + isFinished);

        } catch (Exception e){
            ctx.html("Could not access resource \"Is Game Finished\"");
        }
    }

    public static void hangman(Context ctx) throws Exception{
        return;
    }
}
