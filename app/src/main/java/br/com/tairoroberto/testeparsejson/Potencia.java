package br.com.tairoroberto.testeparsejson;

/**
 * Created by tairo on 03/01/15.
 */
public class Potencia {
    private double motor;
    private int cavalos;

    public Potencia() {
        super();
    }

    public Potencia(float motor, int cavalos) {
        super();
        this.motor = motor;
        this.cavalos = cavalos;
    }

    public double getMotor() {
        return motor;
    }

    public void setMotor(double motor) {
        this.motor = motor;
    }

    public int getCavalos() {
        return cavalos;
    }

    public void setCavalos(int cavalos) {
        this.cavalos = cavalos;
    }
}
