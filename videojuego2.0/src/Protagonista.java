import java.util.ArrayList;

public class Protagonista extends Personaje {
    private int MAXHP;
    private Item Armadura;
    private ArrayList<Item> Bolsa;
    private int Oro;

    

    public Protagonista(String nombre, int atk, int def, int hp, Item armadura, ArrayList<Item> bolsa,
            int oro) {
        super(nombre, atk, def, hp);
        MAXHP = hp;
        Armadura = armadura;
        Bolsa = bolsa;
        Oro = oro;
    }

    public int getMAXHP() {
        return MAXHP;
    }

    public void setMAXHP(int mAXHP) {
        MAXHP = mAXHP;
    }

    public Item getArmadura() {
        return Armadura;
    }

    public void setArmadura(Item armadura) {
        Armadura = armadura;
    }

    public ArrayList<Item> getBolsa() {
        return Bolsa;
    }

    public void setBolsa(ArrayList<Item> bolsa) {
        Bolsa = bolsa;
    }

    public int getOro() {
        return Oro;
    }

    public void setOro(int oro) {
        Oro = oro;
    }

    

    public void addBolsa(Item item) {
        Bolsa.add(item);
    }

    public String getNombreArmadura () {
        return this.Armadura.getNombre();
    }

    /** 
     * Este método recibe el objeto que el protagonista usará, si es armadura primero nos restará las estadísticas de la anterior y sumará la de la nueva, luego la armadura usada
     * se sustituirá por la nueva, y esta se borrará de la bolsa ya que está en uso, si es curativo se sumará los hp que otorga el objeto, sin superar dicho máximo
     */
    public void usarObjeto(int objeto) {
        if (this.Bolsa.get(objeto).getTipo() == Tipo.Armadura) {
            this.DEF -= this.Armadura.getDEF();
            this.DEF += this.Bolsa.get(objeto).getDEF();
            this.Armadura = this.Bolsa.get(objeto);
            this.Bolsa.remove(objeto);
        } else {
            if (this.HP + this.Bolsa.get(objeto).getHP() > this.MAXHP) {
                this.HP = this.MAXHP;
                this.Bolsa.remove(objeto);
            } else {
                this.HP += Bolsa.get(objeto).getHP();
                this.Bolsa.remove(objeto);
            }
        }
    }

    public void usarArmadura() {
        this.DEF += this.Armadura.getDEF();
    }
    @Override
    public String toString () {
        return this.getClass().getName() + ": " + super.toString() + " Item personal: " + Armadura;
    }


}
