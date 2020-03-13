package brugerautorisation.server;
import java.io.*;

public class Serialisering
{
	public static void gem(Serializable obj, String filnavn) throws IOException
	{
		System.out.println("Gemmer filen "+filnavn);
		FileOutputStream datastrom = new FileOutputStream(filnavn);
		ObjectOutputStream objektstrom = new ObjectOutputStream(datastrom);
		objektstrom.writeObject(obj);
		objektstrom.close();
	}

	public static Serializable hent(String filnavn) throws Exception
	{
		System.out.println("Indlæser filen "+filnavn);
		FileInputStream datastrom = new FileInputStream(filnavn);
		ObjectInputStream objektstrom = new ObjectInputStream(datastrom);
		Serializable obj = (Serializable) objektstrom.readObject();
		objektstrom.close();
		return obj;
	}
}