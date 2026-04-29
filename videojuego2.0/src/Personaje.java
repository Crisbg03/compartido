public abstract class Personaje implements Comparable<Personaje> {
    protected  String Nombre;
    protected  int ATK, DEF, HP;
    
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
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


    public Personaje(String nombre, int atk , int def, int hp) {
        this.Nombre= nombre;
        this.ATK = atk;
        this.DEF = def;
        this.HP = hp;
    }
    @Override
    public int compareTo(Personaje o) {
        return this.Nombre.compareTo(o.Nombre);
    }
    
    @Override
    public String toString() {
        return Nombre + " (ATK: " + ATK + ", DEF: " + DEF + ", HP: " + HP + ")";
    }

    

    /** 
     * Este método recibe un entero de daño que será el que reciba dicho personaje, primero se le resta la defensa que tenga el personaje y luego se comprueba si el daño es menor
     * que 0
     */
    public void ataque(int dano) {
        dano -= this.DEF;
        if (dano < 0) {
            dano = 0;
        }
        this.HP -= dano;
    }

    
}
