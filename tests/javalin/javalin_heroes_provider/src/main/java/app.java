import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.HashMap;


public class app {
    public static void main(String[] args) {
        // start a javalin server with cors allowed from all origins, listening from 7000
        Javalin app = Javalin.create().enableCorsForAllOrigins().start(7000);

        /*
        HINT : HTML file rendering like in node is possible with ctx.render()
         */
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/test/", ctx -> {
            System.out.println("requesting and sending heroes from source");
            HashMap<Integer,String> json = new HashMap<>();
            /*ArrayList<String> infos = new ArrayList<>();

            infos.add("Alexandre");
            infos.add("Vanini");
            infos.add("20 ans");
            infos.add("74 chemin des tuileries Bellevue");

            json.put("informations",infos);


 {id: 11, name: 'Mr. Nice'},
  {id: 12, name: 'Narco'},
  {id: 13, name: 'Bombasto'},
  {id: 14, name: 'Celeritas'},
  {id: 15, name: 'Magneta'},
  {id: 16, name: 'RubberMan'},
  {id: 17, name: 'Dynama'},
  {id: 18, name: 'Dr IQ'},
  {id: 19, name: 'Magma'},
  {id: 20, name: 'Tornado'}
            */
            json.put(11, "Mr. Nice");
            json.put(12, "Narco");
            json.put(13, "Bombasto");
            json.put(14, "Celeritas");

            ctx.json(json);
        });
    }
}
