import brugerautorisation.transport.rmi.Brugeradmin;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.rmi.Naming;

public class GalgelegRESTServer {

    public static Javalin app;
    public static IGalgeLogik galgeLogik;

    public static void main(String[] args) throws Exception {
        app = Javalin.create().start(4000);
        galgeLogik = (IGalgeLogik) Naming.lookup("rmi://localhost/Hangman");

        setupResources();
    }

    public static void setupResources() throws Exception{

        app.get("", ctx -> ctx.redirect("/login"));
        app.get("/login", ctx -> login(ctx));
        app.get("/play", ctx -> hangman(ctx));
        app.get("/play/lives", ctx -> getLives(ctx, galgeLogik));
        app.get("/play/visibleWord", ctx -> getVisibleWord(ctx, galgeLogik));
        app.get("/play/usedLetters", ctx -> getUsedLetters(ctx, galgeLogik));
        app.get("/play/guess", ctx -> guessOnLetter(ctx, galgeLogik));
        app.get("/play/isFinished", ctx -> isGameFinished(ctx, galgeLogik));
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

    public static void getLives(Context ctx, IGalgeLogik galgeLogik) throws Exception {
        try {
            int lives = 7-galgeLogik.getAntalForkerteBogstaver();
            ctx.html("" + lives);

        } catch (Exception e){
            ctx.html("Could not access resource \"Lives\"");
        }
    }

    public static void getVisibleWord(Context ctx, IGalgeLogik galgeLogik) throws Exception {
        try {
            String visibleWord = galgeLogik.getSynligtOrd();
            ctx.html(visibleWord);

        } catch (Exception e){
            ctx.html("Could not access resource \"Visible Word\"");
        }
    }

    public static void getUsedLetters(Context ctx, IGalgeLogik galgeLogik) throws Exception{
        try {
            String usedLetters = galgeLogik.getBrugteBogstaver().toString();
            ctx.html(usedLetters);

        } catch (Exception e){
            ctx.html("Could not access resource \"Used Letters\"");
        }
    }

    public static void guessOnLetter (Context ctx, IGalgeLogik galgeLogik) throws Exception {
        try {
            String letter = ctx.queryParam("letter");
            galgeLogik.gætBogstav(letter);

            if (galgeLogik.erSidsteBogstavKorrekt()){
                ctx.html("correct letter");

            } else {
                ctx.html("incorrect letter");
            }

        } catch (Exception e){
            ctx.html("Could not submit query");
        }
    }

    public static void isGameFinished(Context ctx, IGalgeLogik galgeLogik) throws Exception{
        try {
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
