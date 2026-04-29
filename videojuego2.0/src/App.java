import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

public class App {
    public static HashMap<String, Personaje> Personajes;
    public static ArrayList<Item> Items;
    public static Scanner sc = new Scanner(System.in);
    public static String key;
    private static Protagonista p;

    public static void main(String[] args) throws Exception {
        declararObjetos();
        mostrarPersonajes();
        mostrarMenu();
    }

    private static int leerEnteroRango(int min, int max) {
        while (true) {
            try {
                int num = Integer.parseInt(sc.nextLine());
                if (num >= min && num <= max) {
                    return num;
                }
                System.out.println("Error: El número debe estar entre " + min + " y " + max + ".");
            } catch (Exception e) {
                System.out.println("Error: Debes introducir un número entero válido.");
            }
        }
    }

    private static char leerCharValido(String opciones) {
        while (true) {
            String input = sc.nextLine().toUpperCase();
            if (!input.isEmpty()) {
                char c = input.charAt(0);
                if (opciones.toUpperCase().indexOf(c) != -1) {
                    return c;
                }
            }
            System.out.println("Opción no válida. Introduce una de estas: " + opciones);
        }
    }

    private static void mostrarMenu() {
        boolean salir = false;
        do {
            System.out.println("\nMenú de opciones:");
            System.out.println("1. Editar lista de personajes");
            System.out.println("2. Editar lista de objetos");
            System.out.println("3. Crear nuevo personaje");
            System.out.println("4. Crear nuevo objeto");
            System.out.println("5. Jugar");
            System.out.println("6. Mostrar personajes");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = leerEnteroRango(1, 7);
            switch (opcion) {
                case 1:
                    editarPersonajes();
                    break;
                case 2:
                    editarObjetos();
                    break;
                case 3:
                    crearPersonaje();
                    break;
                case 4:
                    crearObjeto();
                    break;
                case 5:
                    jugar();
                    break;
                case 6:
                    mostrarPersonajes();
                    break;
                case 7:
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
            }
        } while (!salir);
    }

    private static void jugar() {
        Random rand = new Random();
        p = seleccionarProta();
        System.out.println("¡Decidido, entonces serás " + p.getNombre()
                + "\nEste prota tiene incluido el item " + p.getArmadura()
                + " se sumarán sus atributos al protagonista");

        p.usarArmadura();
        System.out.println("Nuevas estadísticas:\n" + p);

        System.out.println("¿Quieres jugar modo fácil o difícil (F/D)");
        char dificultad = leerCharValido("FD");

        if (dificultad == 'F') {
            int nEchizos = 0;
            int nArmadura = 0;
            do {
                int nRandom = rand.nextInt(Items.size());
                Item temp = Items.get(nRandom);
                if (temp.getTipo() == Tipo.Armadura) {
                    if (nArmadura < 1) {
                        p.addBolsa(temp);
                        nArmadura++;
                    }
                } else {
                    if (nEchizos < 2) {
                        p.addBolsa(temp);
                        nEchizos++;
                    }
                }
            } while (nEchizos < 2 || nArmadura < 1);
        }

        boolean salirJuego = false;
        ArrayList<Enemigo> Enemigos = new ArrayList<>();
        for (Personaje p : Personajes.values()) {
            if (p instanceof Enemigo) {
                Enemigo e = (Enemigo) p;
                Enemigos.add(e);
            }
        }
        while (!salirJuego && !Enemigos.isEmpty() && p.getHP() > 0) {
            if (!p.getBolsa().isEmpty()) {
                System.out.println("Tienes " + p.getBolsa().size() + " objetos, ¿te gustaría usar alguno?(S/N)");
                char usar = leerCharValido("SN");
                if (usar == 'S') {
                    usarObjeto();
                }
            }

            if (Enemigos.isEmpty())
                break;

            int idxEnemigo = rand.nextInt(Enemigos.size());
            Enemigo enemigoActual = Enemigos.get(idxEnemigo);

            System.out.println("\n--- ¡OH DIOS MÍO! APARECE " + enemigoActual.getNombre() + " (HP: "
                    + enemigoActual.getHP() + ") ---");

            while (enemigoActual.getHP() > 0 && p.getHP() > 0 && !salirJuego) {
                System.out.println("\nTu HP: " + p.getHP() + " | Enemigo HP: " + enemigoActual.getHP());
                System.out.println("¿Qué vas a hacer?: \n1. Atacar\n2. Usar Item\n3. Salir");
                int opcion = leerEnteroRango(1, 3);

                switch (opcion) {
                    case 1:
                        enemigoActual.ataque(p.getATK());
                        if (enemigoActual.getHP() <= 0) {
                            enemigoActual.setHP(0);
                            System.out.println("¡HAS DERROTADO AL ENEMIGO!");
                            p.setOro(p.getOro() + enemigoActual.getDropOro());
                            System.out.println(
                                    "Has ganado " + enemigoActual.getDropOro() + " de oro. Total: " + p.getOro());
                            Enemigos.remove(idxEnemigo);
                            tienda(dificultad, p.getOro());
                        } else {
                            System.out.println("TOMA YA, ahora tiene " + enemigoActual.getHP() + " HP");
                        }
                        break;
                    case 2:
                        usarObjeto();
                        break;
                    case 3:
                        salirJuego = true;
                        break;
                }

                if (enemigoActual.getHP() > 0 && !salirJuego && p.getHP() > 0) {
                    System.out.println("¡AHORA TE ATACA ÉL!");
                    p.ataque(enemigoActual.getATK());

                    if (p.getHP() <= 0) {
                        p.setHP(0);
                        System.out.println("OH NO... HAS MUERTO. JUEGO TERMINADO.");
                    } else {
                        System.out.println("TE QUEDAN " + p.getHP() + " HP");
                        if (p.getArmadura() != null) {
                            int durabilidad = p.getArmadura().getDureza();
                            if (durabilidad > 1) {
                                p.getArmadura().setDureza(durabilidad - 1);
                                System.out.println("Tu armadura resiste. Usos: " + p.getArmadura().getDureza());
                            } else {
                                p.setArmadura(null);
                                System.out.println("¡CRACK! Tu armadura se ha roto.");
                            }
                        }
                    }
                }
            }
        }

        if (Enemigos.isEmpty() && p.getHP() > 0) {
            System.out.println("¡HAS ACABADO CON TODOS LOS ENEMIGOS! SUUUUUU");
        }
    }

    private static void tienda(char dificultad, int oroActual) {
        Random rand = new Random();
        ArrayList<Item> curativos = new ArrayList<>();
        ArrayList<Item> armaduras = new ArrayList<>();

        for (Item i : Items) {
            if (i.getTipo() == Tipo.Armadura)
                armaduras.add(i);
            else
                curativos.add(i);
        }

        ArrayList<Item> escaparate = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            if (!curativos.isEmpty())
                escaparate.add(curativos.get(rand.nextInt(curativos.size())));
        }
        if (!armaduras.isEmpty())
            escaparate.add(armaduras.get(rand.nextInt(armaduras.size())));

        boolean enTienda = true;
        int compras = 0;
        boolean[] vendido = new boolean[escaparate.size()];

        System.out.println("\n--- BIENVENIDO A LA TIENDA ---");

        while (enTienda) {
            System.out.println("Oro: " + p.getOro());
            for (int i = 0; i < escaparate.size(); i++) {
                if (vendido[i])
                    System.out.println((i + 1) + ". [VENDIDO]");
                else
                    System.out.println((i + 1) + ". " + escaparate.get(i).getNombre() + " ("
                            + escaparate.get(i).getPrecio() + " oro)");
            }
            System.out.println("0. Salir");

            int opcion = leerEnteroRango(0, escaparate.size());

            if (opcion == 0) {
                enTienda = false;
            } else {
                int idx = opcion - 1;
                Item it = escaparate.get(idx);

                if (vendido[idx]) {
                    System.out.println("Ese objeto ya no está.");
                } else if (p.getOro() >= it.getPrecio()) {
                    p.setOro(p.getOro() - it.getPrecio());
                    p.addBolsa(it);
                    vendido[idx] = true;
                    compras++;
                    System.out.println("Comprado: " + it.getNombre());

                    if (dificultad == 'D') {
                        enTienda = false;
                    } else if (dificultad == 'F' && compras == escaparate.size()) {
                        System.out.println("¡Has vaciado la tienda!");
                        enTienda = false;
                    }
                } else {
                    System.out.println("No tienes suficiente oro.");
                }
            }
        }
    }

    private static void usarObjeto() {
        if (p.getBolsa().isEmpty()) {
            System.out.println("La bolsa está vacía.");
            return;
        }
        boolean salirObjeto = false;
        do {
            for (int i = 0; i < p.getBolsa().size(); i++) {
                System.out.println("Item nº" + (i + 1) + "\n" + p.getBolsa().get(i));
            }
            System.out.println("¿Cuál item quieres usar?(0 para cancelar)");
            int objeto = leerEnteroRango(0, 1000);

            if (objeto == 0) {
                System.out.println("Perfecto lo guardaremos para luego");
                salirObjeto = true;
            } else {
                try {
                    int indice = objeto - 1;
                    Item it = p.getBolsa().get(indice);

                    if (it.getTipo() == Tipo.Curativo) {
                        if (p.getHP() < p.getMAXHP()) {
                            System.out.println("HP: " + p.getHP() + "/" + p.getMAXHP() + ". Cura: " + it.getHP());
                            System.out.println("¿Seguro qué quieres usarla?(S/N)");
                            if (leerCharValido("SN") == 'S') {
                                p.usarObjeto(indice);
                                System.out.println("Nuevas estadísticas:\n" + p);
                                salirObjeto = true;
                            }
                        } else {
                            System.out.println("Ya tienes la vida al máximo.");
                            salirObjeto = true;
                        }
                    } else {
                        p.usarObjeto(indice);
                        System.out.println("Nuevas estadísticas:\n" + p);
                        salirObjeto = true;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Ese objeto no existe cabeza bolo, dime algo serio");
                }
            }
        } while (!salirObjeto);
    }

    private static void crearObjeto() {
        System.out.println("¿Cuál será el nombre del objeto?");
        String nombre = sc.nextLine();
        System.out.println("¿Tipo Armadura o Curativo? (A/C)");
        char tipo = leerCharValido("AC");
        if (tipo == 'A') {
            System.out.println("Defensa:");
            int def = leerEnteroRango(0, 1000);
            System.out.println("Precio:");
            int precio = leerEnteroRango(0, 1000);
            System.out.println("Durabilidad:");
            int dureza = leerEnteroRango(1, 100);
            Items.add(new Item(nombre, precio, def, dureza));
        } else {
            System.out.println("Cura HP:");
            int hp = leerEnteroRango(1, 1000);
            System.out.println("Precio:");
            int precio = leerEnteroRango(0, 1000);
            Items.add(new Item(nombre, Tipo.Curativo, precio, hp, 0));
        }
    }

    private static void crearPersonaje() {
        System.out.println("¿Protagonista o Enemigo? (P/E)");
        char tipo = leerCharValido("PE");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("ATK: ");
        int atk = leerEnteroRango(0, 1000);
        System.out.print("DEF: ");
        int def = leerEnteroRango(0, 1000);
        System.out.print("HP: ");
        int hp = leerEnteroRango(1, 1000);
        if (tipo == 'P') {
            Personajes.put(nombre, new Protagonista(nombre, atk, def, hp, null, new ArrayList<>(), 0));
        } else {
            System.out.print("Oro: ");
            int oro = leerEnteroRango(0, 1000);
            Personajes.put(nombre, new Enemigo(nombre, atk, def, hp, 0, oro));
        }
    }

    private static void editarObjetos() {
        if (Items.isEmpty())
            return;
        System.out.println("¿Editar o Borrar? (E/B)");
        char accion = leerCharValido("EB");
        for (int i = 0; i < Items.size(); i++)
            System.out.println((i + 1) + ". " + Items.get(i).getNombre());
        int index = leerEnteroRango(1, Items.size()) - 1;
        if (accion == 'E') {
            Item item = Items.get(index);
            System.out.println("1. ATK, 2. DEF, 3. HP, 4. Precio");
            int stat = leerEnteroRango(1, 4);
            int val = leerEnteroRango(0, 1000);
            if (stat == 1)
                item.setATK(val);
            else if (stat == 2)
                item.setDEF(val);
            else if (stat == 3)
                item.setHP(val);
            else
                item.setPrecio(val);
        } else {
            Items.remove(index);
        }
    }

    private static void editarPersonajes() {

        boolean valido = false;
        do {
            System.out.println("Qué personaje quieres editar? (nombre)");
            for (Personaje p : Personajes.values()) {
                    System.out.println("- " + p.getNombre()); 
            }
            key = sc.nextLine();
            if (Personajes.get(key) == null) {
                System.out.println("Error no se ha encontrado ningún protagonista con ese nombre");
            } else {
                valido = true;
            }
        } while (valido == false);
        if (Personajes.get(key).getClass().getName().equals("Protagonista")) {
            System.out.println("¿Quieres editar 1. Nombre 2. ATK 3. DEF 4. HP");
            int e = leerEnteroRango(1, 4);
            if (e != 1) {
                System.out.println("A cuánto lo quieres modificar");
                int a = leerEnteroRango(0, 500);
                if (e == 2) {
                    Personajes.get(key).setATK(a);
                } else if (e == 3) {
                    Personajes.get(key).setDEF(a);
                } else {
                    Personajes.get(key).setHP(a);
                }
            } else {
                System.out.println("Qué nombre le quieres dar al personaje?");
                String nombre = sc.nextLine();
                Personajes.get(key).setNombre(nombre);
                Personajes.put(nombre, Personajes.get(key));
                Personajes.remove(key);
            }
        } else {
            System.out.println("¿Quieres editar 1. Nombre 2. ATK 3. DEF 4. HP 5. Oro a soltar");
            int e = leerEnteroRango(1, 5);
            if (e != 1) {
                System.out.println("A cuánto lo quieres modificar");
                int a = leerEnteroRango(0, 500);
                if (e == 2) {
                    Personajes.get(key).setATK(a);
                } else if (e == 3) {
                    Personajes.get(key).setDEF(a);
                } else if (e == 4) {
                    Personajes.get(key).setHP(a);
                } else {
                    Enemigo en = (Enemigo) Personajes.get(key);
                    en.setDropOro(a);
                }
            } else {
                System.out.println("Qué nombre le quieres dar al personaje?");
                String nombre = sc.nextLine();
                Personajes.get(key).setNombre(nombre);
            }
        }
    }

    private static void mostrarPersonajes() {
        for (Personaje p : Personajes.values()) {
            System.out.println(p);
        }
    }

    public static void declararObjetos() {
        Personajes = new HashMap<>();
        Items = new ArrayList<>();
        Item Cinta = new Item("Cinta de pelo", 0, 15, 3);
        Protagonista Mark = new Protagonista("Mark Evans", 10, 35, 25, Cinta, new ArrayList<>(), 0);
        Enemigo Alien = new Enemigo("Xene", 55, 40, 50, 0, 30);
        Enemigo Ogro = new Enemigo("Bash Lancer", 60, 30, 40, 0, 20);
        Item Colgante = new Item("Colgante de Julia", 0, 15, 3);
        Protagonista Axel = new Protagonista("Axel Blaze", 35, 25, 25, Colgante, new ArrayList<>(), 0);
        Enemigo Dios = new Enemigo("Byron Love", 50, 35, 45, 0, 25);
        Items.add(new Item("Guantes del abuelo", 30, 25, 4));
        Items.add(new Item("Nectar de los dioses", Tipo.Curativo, 25, 20, 0));
        Items.add(new Item("Bola de arroz", Tipo.Curativo, 15, 10, 0));
        Items.add(new Item("Botas del Raimon", 25, 20, 3));
        Items.add(new Item("Muñequera de Victor Blade", 35, 30, 5));
        Personajes.put(Mark.getNombre(), Mark);
        Personajes.put(Alien.getNombre(), Alien);
        Personajes.put(Dios.getNombre(), Dios);
        Personajes.put(Ogro.getNombre(), Ogro);
        Personajes.put(Axel.getNombre(), Axel);
    }

    public static Protagonista seleccionarProta() {
        Protagonista Prota = null;
        System.out.println("¿Quieres 1.Elegir un protagonista 2. Que se te asigne uno aleatoriamente?");
        int opcion = leerEnteroRango(1, 2);
        if (opcion == 1) {
            boolean valido = false;
            do {
                System.out.println("¿Qué protagonista quieres ser? (Nombre prota)");
                for (Personaje p : Personajes.values()) {
                    if (p instanceof Protagonista) {
                        System.out.println("- " + p.getNombre());
                    }
                }
                key = sc.nextLine();
                if (Personajes.get(key) == null || Personajes.get(key).getClass().getSimpleName().equals("Enemigo")) {
                    System.out.println("Error no se ha encontrado ningún protagonista con ese nombre");
                } else {
                    valido = true;
                    Prota = (Protagonista) Personajes.get(key);
                }
            } while (valido == false);
        } else {
            int totalProtas = 0;
            for (Personaje p : Personajes.values()) {
                if (p instanceof Protagonista) {
                    totalProtas++;
                }
            }
            int indiceElegido = new Random().nextInt(totalProtas);
            int contador = 0;
            for (Personaje p : Personajes.values()) {
                if (p instanceof Protagonista) {
                    if (contador == indiceElegido) {
                        Prota = (Protagonista) p;
                    }
                    contador++;
                }
            }
        }
        return Prota;
    }
}