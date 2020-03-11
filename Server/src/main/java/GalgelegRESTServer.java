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

        app.get("", ctx -> ctx.redirect("/login"));
        app.get("/login", ctx -> login(ctx));
        app.get("/play", ctx -> hangman(ctx));
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

    // Acting as rmi-client to GaglelegRMIServer (localhost)
    public static void hangman(Context ctx) throws Exception{
        IGalgeLogik galgeLogik = (IGalgeLogik) Naming.lookup("rmi://localhost/Hangman");
        String letter = ctx.queryParam("guess");

        if (letter == null){
            ctx.html(galgeLogik.getSynligtOrd());
        }else {
            try {
                while(!galgeLogik.erSpilletSlut()){
                    galgeLogik.gætBogstav(letter);
                    ctx.html(galgeLogik.getSynligtOrd());
                }
            }catch (Exception e){
                ctx.html("An error occurred");
            }
        }
    }
}
