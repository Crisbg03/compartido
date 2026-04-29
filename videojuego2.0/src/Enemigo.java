
public class Enemigo extends Personaje {
    private int Nderrotado;
    private int DropOro;

    public int getNderrotado() {
        return Nderrotado;
    }

    public void setNderrotado(int nderrotado) {
        Nderrotado = nderrotado;
    }

    public int getDropOro() {
        return DropOro;
    }

    public void setDropOro(int dropOro) {
        DropOro = dropOro;
    }

    public Enemigo(String nombre, int atk, int def, int hp, int nderrotado, int dropOro) {
        super(nombre, atk, def, hp);
        Nderrotado = nderrotado;
        DropOro = dropOro;
    }

    public void ataque (int dano) {
        this.HP -= dano;
    }

    @Override
    public String toString () {
        return this.getClass().getName() + ": " + super.toString() + " Oro a soltar: " + DropOro;
    }

}
