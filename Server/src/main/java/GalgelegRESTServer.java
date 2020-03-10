import brugerautorisation.transport.rmi.Brugeradmin;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.rmi.Naming;

public class GalgelegRESTServer {

    public static Javalin app;

    public static void main(String[] args) {
        runServer();
    }

    public static void runServer(){
        if (app!=null) return;

        app = Javalin.create().start(4000);

        app.get("/login", ctx -> login(ctx));
        app.get("/play", ctx -> hangman(ctx));
    }

    // Acting as rmi-client to Javabog.dk
    private static void login(Context ctx) throws Exception{
        String userID = ctx.queryParam("username");
        String password = ctx.queryParam("password");
        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

        try {
            brugeradmin.hentBruger(userID, password);
            ctx.redirect("/playGame");

        }catch (Exception e){

        }

    }

    // Acting as rmi-client to GaglelegRMIServer (localhost)
    public static void hangman(Context ctx) throws Exception{
        IGalgeLogik galgeLogik = (IGalgeLogik) Naming.lookup("rmi://localhost/Hangman");

        ctx.html(galgeLogik.getSynligtOrd());

        while(!galgeLogik.erSpilletSlut()){
            String letter = ctx.queryParam("guess");
            galgeLogik.gætBogstav(letter);

            ctx.html(galgeLogik.getSynligtOrd());
        }
    }
}
