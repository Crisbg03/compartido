public class Item {
    private String Nombre;
    private Tipo tipo;
    private int Precio, ATK, DEF, HP, Dureza; // creo atributo dureza para establecer la durabilidad del item (armadura)

    public int getDureza() {
        return Dureza;
    }

    public void setDureza(int dureza) {
        Dureza = dureza;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int precio) {
        Precio = precio;
    }

    public int getATK() {
        return ATK;
    }

    public void setATK(int aTK) {
        ATK = aTK;
    }

    public int getDEF() {
        return DEF;
    }

    public void setDEF(int dEF) {
        DEF = dEF;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int hP) {
        HP = hP;
    }

    public Item(String nombre, int precio, int def, int dureza) {
        this.Nombre = nombre;
        this.tipo = Tipo.Armadura;
        this.Precio = precio;
        this.DEF = def;
        this.Dureza = dureza;
    }
    public Item(String nombre, Tipo tipo, int precio, int hp, int atk) {
        this.Nombre = nombre;
        this.tipo = tipo;
        this.Precio = precio;
        this.HP = hp;
    }
    
    public String toString () {
        return this.Nombre + ", " +  this.tipo;
    }

}
